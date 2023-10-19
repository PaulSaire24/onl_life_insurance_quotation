package com.bbva.rbvd.lib.r304.service.dao.impl;

import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationDAO;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.commons.IdentityDocumentDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.InsurancePlanDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.TermDTO;
import com.bbva.rbvd.dto.lifeinsrc.dao.CommonsLifeDAO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.QuotationLifeBO;
import com.bbva.rbvd.dto.lifeinsrc.simulation.ContractDetailsDTO;
import com.bbva.rbvd.dto.lifeinsrc.simulation.ParticipantDTO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDValidation;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceQuotationDAO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import com.bbva.rbvd.lib.r304.transform.bean.InsuranceQuotationBean;
import com.bbva.rbvd.lib.r304.transform.map.InsuranceQuotationMap;
import com.bbva.rbvd.lib.r304.impl.util.ConstantUtils;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import static com.bbva.rbvd.lib.r304.impl.util.ConstantUtils.EMAIL;
import static com.bbva.rbvd.lib.r304.impl.util.ConstantUtils.MOBILE_NUMBER;
import static com.bbva.rbvd.lib.r304.impl.util.ValidationUtil.*;

public class InsuranceQuotationDAOImpl implements IInsuranceQuotationDAO {
    private final PISDR350 pisdR350;

    public InsuranceQuotationDAOImpl(PISDR350 pisdR350) {
        this.pisdR350 = pisdR350;
    }
    @Override
    public CommonsLifeDAO createQuotationParticipant(PayloadStore payloadStore, CustomerListASO customerInformation) {
        CommonsLifeDAO quotationParticipant = new CommonsLifeDAO();

        EasyesQuotationDAO quotationDao = payloadStore.getMyQuotation();
        QuotationLifeDTO input = payloadStore.getInput();

        BigDecimal periodNumber = safeBigDecimal(input, QuotationLifeDTO::getTerm, TermDTO::getNumber, BigDecimal.ZERO);
        BigDecimal totalReturnAmount = calculateTotalReturnAmount(input);

        InsurancePlanDTO plan = safeGetPlan(input);

        quotationParticipant.setPolicyQuotaInternalId(safeGetId(input));
        quotationParticipant.setInsuranceProductId(safeGetInsuranceProductId(quotationDao));
        quotationParticipant.setInsuranceModalityType(safeGetPlanId(plan));
        quotationParticipant.setInsuredAmount(safeGetInsuredAmount(input));
        quotationParticipant.setCurrencyId(safeGetInsuredCurrency(input));
        quotationParticipant.setPeriodType("A");
        quotationParticipant.setPeriodNumber(periodNumber);
        quotationParticipant.setRefundPer(safeGetRefundPercentage(input));
        quotationParticipant.setTotalReturnAmount(totalReturnAmount);
        quotationParticipant.setInsuredId(safeGetInsuredId(input));
        quotationParticipant.setPersonalDocumentId("");
        quotationParticipant.setCustomerEntryDate(safeGetEntryDate());

        ParticipantDTO participant = safeGetParticipant(input);
        if (Objects.nonNull(participant)) {
            setParticipantProperties(quotationParticipant, participant);
        } else {
            CustomerBO customerData = safeGetCustomerData(customerInformation, input);
            setCustomerDataProperties(quotationParticipant, input, customerData);
        }

        quotationParticipant.setParticipantRoleId(BigDecimal.valueOf(ConstantUtils.IS_INSURED));

        return quotationParticipant;
    }

    private BigDecimal safeBigDecimal(QuotationLifeDTO input, Function<QuotationLifeDTO, TermDTO> getTerm,
                                      Function<TermDTO, Integer> getNumber, BigDecimal defaultValue) {
        if (Objects.isNull(input)) {
            return defaultValue;
        }

        TermDTO term = getTerm.apply(input);
        if (Objects.isNull(term)) {
            return defaultValue;
        }

        Integer number = getNumber.apply(term);
        if (Objects.isNull(number)) {
            return defaultValue;
        }

        return new BigDecimal(number);
    }

    private BigDecimal calculateTotalReturnAmount(QuotationLifeDTO input) {
        return (Objects.nonNull(input) && Objects.nonNull(input.getInsuredAmount()) && Objects.nonNull(input.getInsuredAmount().getAmount()) &&
                !CollectionUtils.isEmpty(input.getRefunds()) && Objects.nonNull(input.getRefunds().get(0).getUnit())) ?
                input.getInsuredAmount().getAmount()
                        .multiply(input.getRefunds().get(0).getUnit().getPercentage())
                        .multiply(BigDecimal.valueOf(0.01)) : BigDecimal.ZERO;
    }

    private InsurancePlanDTO safeGetPlan(QuotationLifeDTO input) {
        return (Objects.nonNull(input) && Objects.nonNull(input.getProduct()) && !CollectionUtils.isEmpty(input.getProduct().getPlans())) ?
                input.getProduct().getPlans().get(0) : null;
    }

    private String safeGetId(QuotationLifeDTO input) {
        return (Objects.nonNull(input)) ? input.getId() : null;
    }

    private BigDecimal safeGetInsuranceProductId(EasyesQuotationDAO quotationDao) {
        return (Objects.nonNull(quotationDao)) ? quotationDao.getInsuranceProductId() : null;
    }

    private String safeGetPlanId(InsurancePlanDTO plan) {
        return (Objects.nonNull(plan)) ? plan.getId() : null;
    }

    private BigDecimal safeGetInsuredAmount(QuotationLifeDTO input) {
        return (Objects.nonNull(input) && Objects.nonNull(input.getInsuredAmount())) ? input.getInsuredAmount().getAmount() : null;
    }

    private String safeGetInsuredCurrency(QuotationLifeDTO input) {
        return (Objects.nonNull(input) && Objects.nonNull(input.getInsuredAmount())) ? input.getInsuredAmount().getCurrency() : null;
    }

    private BigDecimal safeGetRefundPercentage(QuotationLifeDTO input) {
        return (Objects.nonNull(input) && !CollectionUtils.isEmpty(input.getRefunds()) && Objects.nonNull(input.getRefunds().get(0).getUnit())) ?
                input.getRefunds().get(0).getUnit().getPercentage() : null;
    }

    private String safeGetInsuredId(QuotationLifeDTO input) {
        return (Objects.nonNull(input) && Objects.nonNull(input.getInsuredAmount())) ? input.getInsuredAmount().getId() : null;
    }

    private Date safeGetEntryDate() {
        return (Objects.nonNull(LocalDate.now())) ? Date.valueOf(LocalDate.now()) : null;
    }

    private ParticipantDTO safeGetParticipant(QuotationLifeDTO input) {
        return (Objects.nonNull(input) && !CollectionUtils.isEmpty(input.getParticipants())) ? input.getParticipants().get(0) : null;
    }

    private CustomerBO safeGetCustomerData(CustomerListASO customerInformation, QuotationLifeDTO input) {
        if (Objects.nonNull(customerInformation) && !CollectionUtils.isEmpty(customerInformation.getData())) {
            return customerInformation.getData().get(0);
        }
        return null;
    }

    private void setParticipantProperties(CommonsLifeDAO quotationParticipant, ParticipantDTO participant) {
        List<ContractDetailsDTO> tipoContratoEmail = getGroupedByTypeContactDetail(participant.getContactDetails(), EMAIL);
        List<ContractDetailsDTO> tipoContratoMov = getGroupedByTypeContactDetail(participant.getContactDetails(), MOBILE_NUMBER);
        String lastName = (participant.getLastName() != null ? participant.getLastName() : "") +
                "-" + (participant.getSecondLastName() != null ? participant.getSecondLastName() : "");

        quotationParticipant.setIsBbvaCustomerType(isBBVAClient(participant.getId()) ? ConstantUtils.YES_S : ConstantUtils.NO_N);
        quotationParticipant.setInsuredCustomerName(participant.getName());
        quotationParticipant.setCustomerDocumentType((participant.getIdentityDocument() != null && participant.getIdentityDocument().getDocumentType() != null) ?
                participant.getIdentityDocument().getDocumentType().getId() : null);
        quotationParticipant.setClientLastName(lastName);
        quotationParticipant.setUserEmailPersonalDesc((!CollectionUtils.isEmpty(tipoContratoEmail) && tipoContratoEmail.get(0).getContact() != null) ?
                tipoContratoEmail.get(0).getContact().getAddress() : null);
        quotationParticipant.setPhoneDesc((!CollectionUtils.isEmpty(tipoContratoMov) && tipoContratoMov.get(0).getContact() != null) ?
                tipoContratoMov.get(0).getContact().getNumber() : null);
        quotationParticipant.setCustomerBirthDate(participant.getBirthDate());
        quotationParticipant.setCreationUser(participant.getCreationUser());
        quotationParticipant.setUserAudit(participant.getUserAudit());
        quotationParticipant.setGenderId((participant.getGender() != null) ? participant.getGender().getId() : null);

    }

    private void setCustomerDataProperties(CommonsLifeDAO quotationParticipant, QuotationLifeDTO input, CustomerBO customerData) {
        String lastName = "";

        if (customerData != null) {
            if (customerData.getLastName() != null) {
                lastName += customerData.getLastName();
            }

            lastName += "-";

            if (customerData.getSecondLastName() != null) {
                lastName += customerData.getSecondLastName();
            }
        }
        quotationParticipant.setCustomerDocumentType((Objects.nonNull(customerData)&&!CollectionUtils.isEmpty(customerData.getIdentityDocuments()) &&
                customerData.getIdentityDocuments().size() > 0 && customerData.getIdentityDocuments().get(0).getDocumentType() != null) ?
                customerData.getIdentityDocuments().get(0).getDocumentType().getId() : null);
        if (Objects.nonNull(customerData) && !customerData.getFirstName().equals(null)) {
            quotationParticipant.setInsuredCustomerName(customerData.getFirstName());
        }
        quotationParticipant.setIsBbvaCustomerType(isBBVAClient(input.getHolder().getId()) ? ConstantUtils.YES_S : ConstantUtils.NO_N);
        quotationParticipant.setClientLastName(lastName);
        quotationParticipant.setPhoneDesc((Objects.nonNull(customerData)&&!CollectionUtils.isEmpty(customerData.getContactDetails()) && customerData.getContactDetails().size() > 1 &&
                customerData.getContactDetails().get(1) != null) ? customerData.getContactDetails().get(1).getContact() : null);
        quotationParticipant.setUserEmailPersonalDesc((Objects.nonNull(customerData)&&(!CollectionUtils.isEmpty(customerData.getContactDetails()) && customerData.getContactDetails().size() > 2 &&
                customerData.getContactDetails().get(2) != null) ? customerData.getContactDetails().get(2).getContact() : null));
        quotationParticipant.setCreationUser(input.getHolder().getCreationUser());
        quotationParticipant.setUserAudit(input.getHolder().getUserAudit());
        quotationParticipant.setGenderId((Objects.nonNull(customerData)&&(customerData.getGender() != null) ? customerData.getGender().getId() : null));

    }
    @Override
    public void insertSimulationParticipant(Map<String, Object> argumentForSaveParticipant) {
        int idNewSimulation = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_INSERT_INSURANCE_QUOTATION.getValue(),argumentForSaveParticipant);
        if(idNewSimulation != 1){
            throw RBVDValidation.build(RBVDErrors.QUOTATION_INSERTION_WAS_WRONG);
        }
    }

    @Override
    public void executeInsertQuotationQuery(PayloadStore payloadStore) {
        InsuranceQuotationDAO insuranceQuotationDao = InsuranceQuotationBean.createInsuranceQuotationDAO(payloadStore.getMyQuotation(), payloadStore.getInput());
        Map<String, Object> argumentsQuotationDao = InsuranceQuotationMap.createArgumentsQuotationDao(insuranceQuotationDao);
        Integer quotationResult = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_INSERT_INSURANCE_QUOTATION.getValue(), argumentsQuotationDao);
        validateInsertionQueries(quotationResult, RBVDErrors.QUOTATION_INSERTION_WAS_WRONG);
    }
}

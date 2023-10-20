package com.bbva.rbvd.lib.r304.service.dao.impl;

import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.pisd.dto.insurance.bo.ContactDetailsBO;
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

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
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
    public CommonsLifeDAO createQuotationParticipant(PayloadStore payloadStore) {
        CommonsLifeDAO quotationParticipant = new CommonsLifeDAO();
        CustomerListASO customerInformation=payloadStore.getCustomerInformation();
        EasyesQuotationDAO quotationDao = payloadStore.getMyQuotation();
        QuotationLifeDTO input = payloadStore.getInput();

        BigDecimal periodNumber = safeBigDecimal(input, QuotationLifeDTO::getTerm, TermDTO::getNumber, BigDecimal.ZERO);
        BigDecimal totalReturnAmount = calculateTotalReturnAmount(input);

        InsurancePlanDTO plan = input.getProduct().getPlans().get(0);

        quotationParticipant.setPolicyQuotaInternalId(input.getId());
        quotationParticipant.setInsuranceProductId(quotationDao.getInsuranceProductId());
        quotationParticipant.setInsuranceModalityType(plan.getId());
        quotationParticipant.setInsuredAmount(safeGetInsuredAmount(input));
        quotationParticipant.setCurrencyId(safeGetInsuredCurrency(input));
        quotationParticipant.setPeriodType("A");
        quotationParticipant.setPeriodNumber(periodNumber);
        quotationParticipant.setRefundPer(input.getRefunds().get(0).getUnit().getPercentage());
        quotationParticipant.setTotalReturnAmount(totalReturnAmount);
        quotationParticipant.setInsuredId(safeGetInsuredId(input));
        quotationParticipant.setCustomerEntryDate(LocalDate.now());

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



    private BigDecimal safeGetInsuredAmount(QuotationLifeDTO input) {
        return (Objects.nonNull(input.getInsuredAmount())) ? input.getInsuredAmount().getAmount() : null;
    }

    private String safeGetInsuredCurrency(QuotationLifeDTO input) {
        return (Objects.nonNull(input.getInsuredAmount())) ? input.getInsuredAmount().getCurrency() : null;
    }


    private String safeGetInsuredId(QuotationLifeDTO input) {
        return (Objects.nonNull(input.getInsuredAmount())) ? input.getInsuredAmount().getId() : null;
    }
    private ParticipantDTO safeGetParticipant(QuotationLifeDTO input) {
        return (!CollectionUtils.isEmpty(input.getParticipants())) ? input.getParticipants().get(0) : null;
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

        Instant instant = participant.getBirthDate().toInstant();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();

        quotationParticipant.setIsBbvaCustomerType(isBBVAClient(participant.getId()) ? ConstantUtils.YES_S : ConstantUtils.NO_N);
        quotationParticipant.setInsuredCustomerName(participant.getName());
        quotationParticipant.setPersonalId(participant.getIdentityDocument().getDocumentNumber());
        quotationParticipant.setCustomerDocumentType((participant.getIdentityDocument() != null && participant.getIdentityDocument().getDocumentType() != null) ?
                participant.getIdentityDocument().getDocumentType().getId() : null);
        quotationParticipant.setClientLastName(lastName);
        quotationParticipant.setUserEmailPersonalDesc((!CollectionUtils.isEmpty(tipoContratoEmail) && tipoContratoEmail.get(0).getContact() != null) ?
                tipoContratoEmail.get(0).getContact().getAddress() : null);
        quotationParticipant.setPhoneId((!CollectionUtils.isEmpty(tipoContratoMov) && tipoContratoMov.get(0).getContact() != null) ?
                tipoContratoMov.get(0).getContact().getNumber() : null);
        quotationParticipant.setCustomerBirthDate(localDate);
        quotationParticipant.setCreationUser(participant.getCreationUser());
        quotationParticipant.setUserAudit(participant.getUserAudit());
        quotationParticipant.setGenderId((participant.getGender() != null) ? participant.getGender().getId() : null);

    }

    private void setCustomerDataProperties(CommonsLifeDAO quotationParticipant, QuotationLifeDTO input, CustomerBO customerData) {
        List<ContactDetailsBO> tipoContratoEmail = getGroupedByTypeContactDetailBO(customerData.getContactDetails(), EMAIL);
        List<ContactDetailsBO> tipoContratoMov = getGroupedByTypeContactDetailBO(customerData.getContactDetails(), MOBILE_NUMBER);

        quotationParticipant.setCustomerDocumentType((Objects.nonNull(customerData)&&!CollectionUtils.isEmpty(customerData.getIdentityDocuments()) &&
                customerData.getIdentityDocuments().size() > 0 && customerData.getIdentityDocuments().get(0).getDocumentType() != null) ?
                customerData.getIdentityDocuments().get(0).getDocumentType().getId() : null);

        quotationParticipant.setInsuredCustomerName(Objects.nonNull(customerData) && (customerData.getFirstName())!=null?
                customerData.getFirstName() : null);

        quotationParticipant.setIsBbvaCustomerType(isBBVAClient(input.getHolder().getId()) ? ConstantUtils.YES_S : ConstantUtils.NO_N);
        quotationParticipant.setClientLastName(getLastName(customerData));
        quotationParticipant.setPersonalId(customerData.getIdentityDocuments().get(0).getDocumentNumber());
        quotationParticipant.setPhoneId((!CollectionUtils.isEmpty(tipoContratoMov) && tipoContratoMov.get(0).getContact() != null) ? tipoContratoMov.get(0).getContact() : null);
        quotationParticipant.setUserEmailPersonalDesc((!CollectionUtils.isEmpty(tipoContratoEmail) && tipoContratoEmail.get(0).getContact() != null) ? tipoContratoEmail.get(0).getContact() : null);
        quotationParticipant.setCreationUser(input.getHolder().getCreationUser());
        quotationParticipant.setUserAudit(input.getHolder().getUserAudit());
        quotationParticipant.setGenderId((Objects.nonNull(customerData)&&(customerData.getGender() != null) ? customerData.getGender().getId() : null));

    }
    private String getLastName(CustomerBO customerData){

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
        return lastName;
    }
    @Override
    public void insertSimulationParticipant(Map<String, Object> argumentForSaveParticipant) {
        int idNewSimulation = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_INSERT_INSURANCE_QUOTATION.getValue(),argumentForSaveParticipant);
        if(idNewSimulation != 1){
            throw RBVDValidation.build(RBVDErrors.QUOTATION_INSERTION_WAS_WRONG);
        }
    }
    @Override
    public void updateSimulationParticipant(Map<String, Object> argumentForSaveParticipant) {
        int idNewSimulation = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_UPDATE_INSURANCE_QUOTATION.getValue(),argumentForSaveParticipant);
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

package com.bbva.rbvd.lib.r304.service.dao.impl;

import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationDAO;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.commons.InsurancePlanDTO;
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

import static com.bbva.rbvd.lib.r304.impl.util.ConstantUtils.EMAIL;
import static com.bbva.rbvd.lib.r304.impl.util.ConstantUtils.MOBILE_NUMBER;
import static com.bbva.rbvd.lib.r304.impl.util.ValidationUtil.*;

public class InsuranceQuotationDAOImpl implements IInsuranceQuotationDAO {
    private final PISDR350 pisdR350;

    public InsuranceQuotationDAOImpl(PISDR350 pisdR350) {
        this.pisdR350 = pisdR350;
    }
    public CommonsLifeDAO createQuotationParticipant(PayloadStore payloadStore, CustomerListASO customerInformation) {
        CommonsLifeDAO quotationParticipant = new CommonsLifeDAO();

        EasyesQuotationDAO quotationDao = payloadStore.getMyQuotation();
        QuotationLifeDTO input = payloadStore.getInput();

        BigDecimal periodNumber = (Objects.nonNull(input) && Objects.nonNull(input.getTerm()) && Objects.nonNull(input.getTerm().getNumber())) ?
                new BigDecimal(input.getTerm().getNumber()) : BigDecimal.ZERO;

        BigDecimal totalReturnAmount = BigDecimal.ZERO;
        if (Objects.nonNull(input) && Objects.nonNull(input.getInsuredAmount()) && Objects.nonNull(input.getInsuredAmount().getAmount()) &&
                !CollectionUtils.isEmpty(input.getRefunds()) && Objects.nonNull(input.getRefunds().get(0).getUnit())) {
            totalReturnAmount = input.getInsuredAmount().getAmount()
                    .multiply(input.getRefunds().get(0).getUnit().getPercentage())
                    .multiply(new BigDecimal(0.01));
        }

        InsurancePlanDTO plan = (Objects.nonNull(input) && Objects.nonNull(input.getProduct()) && !input.getProduct().getPlans().isEmpty()) ?
                input.getProduct().getPlans().get(0) : null;

        quotationParticipant.setPolicyQuotaInternalId((Objects.nonNull(input)) ? input.getId() : null);
        quotationParticipant.setInsuranceProductId((Objects.nonNull(quotationDao)) ? quotationDao.getInsuranceProductId() : null);
        quotationParticipant.setInsuranceModalityType((Objects.nonNull(plan)) ? plan.getId() : null);
        quotationParticipant.setInsuredAmount((Objects.nonNull(input) && Objects.nonNull(input.getInsuredAmount())) ? input.getInsuredAmount().getAmount() : null);
        quotationParticipant.setCurrencyId((Objects.nonNull(input) && Objects.nonNull(input.getInsuredAmount())) ? input.getInsuredAmount().getCurrency() : null);
        quotationParticipant.setPeriodType("A");
        quotationParticipant.setPeriodNumber(periodNumber);

        if (Objects.nonNull(input) && !CollectionUtils.isEmpty(input.getRefunds()) && Objects.nonNull(input.getRefunds().get(0).getUnit())) {
            quotationParticipant.setRefundPer(input.getRefunds().get(0).getUnit().getPercentage());
        }
        quotationParticipant.setTotalReturnAmount(totalReturnAmount);
        quotationParticipant.setInsuredId((Objects.nonNull(input) && Objects.nonNull(input.getInsuredAmount())) ? input.getInsuredAmount().getId() : null);
        quotationParticipant.setCustomerEntryDate(Date.valueOf(LocalDate.now()));

        if (!CollectionUtils.isEmpty(input.getParticipants())) {
            ParticipantDTO participant = input.getParticipants().get(0);

            List<ContractDetailsDTO> tipoContratoEmail = getGroupedByTypeContactDetail(participant.getContactDetails(), EMAIL);
            List<ContractDetailsDTO> tipoContratoMov = getGroupedByTypeContactDetail(participant.getContactDetails(), MOBILE_NUMBER);

            String lastName = (Objects.nonNull(participant.getLastName()) ? participant.getLastName() : "") +
                    "-" + (Objects.nonNull(participant.getSecondLastName()) ? participant.getSecondLastName() : "");

            quotationParticipant.setIsBbvaCustomerType(isBBVAClient(participant.getId()) ? ConstantUtils.YES_S : ConstantUtils.NO_N);
            quotationParticipant.setInsuredCustomerName(participant.getName());
            quotationParticipant.setCustomerDocumentType((Objects.nonNull(participant.getIdentityDocument()) &&
                    Objects.nonNull(participant.getIdentityDocument().getDocumentType())) ?
                    participant.getIdentityDocument().getDocumentType().getId() : null);
            quotationParticipant.setClientLastName(lastName);
            quotationParticipant.setUserEmailPersonalDesc((!CollectionUtils.isEmpty(tipoContratoEmail) &&
                    Objects.nonNull(tipoContratoEmail.get(0).getContact())) ?
                    tipoContratoEmail.get(0).getContact().getAddress() : null);
            quotationParticipant.setPhoneDesc((!CollectionUtils.isEmpty(tipoContratoMov) &&
                    Objects.nonNull(tipoContratoMov.get(0).getContact())) ?
                    tipoContratoMov.get(0).getContact().getNumber() : null);
            quotationParticipant.setCustomerBirthDate(participant.getBirthDate());
            quotationParticipant.setCreationUser(participant.getCreationUser());
            quotationParticipant.setUserAudit(participant.getUserAudit());
            quotationParticipant.setGenderId((Objects.nonNull(participant.getGender())) ? participant.getGender().getId() : null);
        } else if (Objects.nonNull(customerInformation) && !CollectionUtils.isEmpty(customerInformation.getData())) {
            CustomerBO customerData = customerInformation.getData().get(0);
            String lastName = (Objects.nonNull(customerData.getLastName()) ? customerData.getLastName() : "") +
                    "-" + (Objects.nonNull(customerData.getSecondLastName()) ? customerData.getSecondLastName() : "");

            quotationParticipant.setCustomerDocumentType((!CollectionUtils.isEmpty(customerData.getIdentityDocuments()) &&
                    Objects.nonNull(customerData.getIdentityDocuments().get(0)) &&
                    Objects.nonNull(customerData.getIdentityDocuments().get(0).getDocumentType())) ?
                    customerData.getIdentityDocuments().get(0).getDocumentType().getId() : null);
            quotationParticipant.setInsuredCustomerName(customerData.getFirstName());
            quotationParticipant.setIsBbvaCustomerType(isBBVAClient(input.getHolder().getId()) ? ConstantUtils.YES_S : ConstantUtils.NO_N);
            quotationParticipant.setClientLastName(lastName);
            quotationParticipant.setPhoneDesc((!CollectionUtils.isEmpty(customerData.getContactDetails()) && customerData.getContactDetails().size() > 1 &&
                    Objects.nonNull(customerData.getContactDetails().get(1))) ?
                    customerData.getContactDetails().get(1).getContact() : null);
            quotationParticipant.setUserEmailPersonalDesc((!CollectionUtils.isEmpty(customerData.getContactDetails()) && customerData.getContactDetails().size() > 2 &&
                    Objects.nonNull(customerData.getContactDetails().get(2))) ?
                    customerData.getContactDetails().get(2).getContact() : null);
            quotationParticipant.setCreationUser(input.getHolder().getCreationUser());
            quotationParticipant.setUserAudit(input.getHolder().getUserAudit());
            quotationParticipant.setGenderId((Objects.nonNull(customerData.getGender())) ? customerData.getGender().getId() : null);
        }

        quotationParticipant.setParticipantRoleId(BigDecimal.valueOf(ConstantUtils.IS_INSURED));

        return quotationParticipant;
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

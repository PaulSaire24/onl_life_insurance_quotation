package com.bbva.rbvd.lib.r304.transform.map;


import com.bbva.rbvd.dto.lifeinsrc.dao.InsuredLifeDAO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class QuotationParticipantMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuotationParticipantMap.class);

    private QuotationParticipantMap(){}


    public static Map<String, Object> createArgumentsForSaveParticipant(InsuredLifeDAO quotationParticipant){
        LOGGER.info("QuotationParticipantMap - createArgumentsForSaveParticipant() - start");
        Map<String, Object> arguments = mapForCreateArguments(quotationParticipant);
        arguments.put(RBVDProperties.FIELD_CREATION_USER_ID.getValue(),quotationParticipant.getCreationUser());
        return arguments;
    }
    public static Map<String, Object> createArgumentsForUpdateParticipant(InsuredLifeDAO quotationParticipant){
        LOGGER.info("QuotationParticipantMap - createArgumentsForUpdateParticipant() - start");
        return  mapForCreateArguments(quotationParticipant);
    }
    public static Map<String, Object> mapForCreateArguments(InsuredLifeDAO quotationParticipant) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(), quotationParticipant.getPolicyQuotaInternalId());
        LOGGER.info("QuotationParticipantMap - mapForCreateArguments() PolicyQuotaInternalId {}",quotationParticipant.getPolicyQuotaInternalId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue(), quotationParticipant.getInsuranceProductId());
        LOGGER.info("QuotationParticipantMap - mapForCreateArguments() InsuranceProductId {}",quotationParticipant.getInsuranceProductId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue(), quotationParticipant.getInsuranceModalityType());
        LOGGER.info("QuotationParticipantMap - mapForCreateArguments() InsuranceModalityType {}",quotationParticipant.getInsuranceModalityType());
        arguments.put(RBVDProperties.FIELD_INSURED_AMOUNT.getValue(), quotationParticipant.getInsuredAmount());
        LOGGER.info("QuotationParticipantMap - mapForCreateArguments() InsuredAmount {}",quotationParticipant.getInsuranceProductId());
        arguments.put(RBVDProperties.FIELD_CURRENCY_ID.getValue(), quotationParticipant.getRefunds().getCurrencyId());
        LOGGER.info("QuotationParticipantMap - mapForCreateArguments() CurrencyId {}",quotationParticipant.getRefunds().getCurrencyId());
        arguments.put(RBVDProperties.FIELD_PERIOD_TYPE.getValue(), quotationParticipant.getTerm().getPeriodType());
        LOGGER.info("QuotationParticipantMap - mapForCreateArguments() PeriodType {}",quotationParticipant.getTerm().getPeriodType());
        arguments.put(RBVDProperties.FIELD_PERIOD_NUMBER.getValue(), quotationParticipant.getTerm().getPeriodNumber());
        LOGGER.info("QuotationParticipantMap - mapForCreateArguments() PeriodNumber() {}",quotationParticipant.getTerm().getPeriodNumber());
        arguments.put(RBVDProperties.FIELD_REFUND_PER.getValue(), quotationParticipant.getRefunds().getRefundPer());
        LOGGER.info("QuotationParticipantMap - mapForCreateArguments() RefundPer() {}",quotationParticipant.getRefunds().getRefundPer());
        arguments.put(RBVDProperties.FIELD_TOTAL_RETURN_AMOUNT.getValue(), quotationParticipant.getRefunds().getTotalReturnAmount());
        LOGGER.info("QuotationParticipantMap - mapForCreateArguments() TotalReturnAmount {}",quotationParticipant.getRefunds().getTotalReturnAmount());
        arguments.put(RBVDProperties.FIELD_INSURED_ID.getValue(), quotationParticipant.getParticipant().getInsuredId());
        LOGGER.info("QuotationParticipantMap - mapForCreateArguments() InsuredId {}",quotationParticipant.getParticipant().getInsuredId());
        arguments.put(RBVDProperties.FIELD_CUSTOMER_DOCUMENT_TYPE.getValue(), quotationParticipant.getParticipant().getCustomerDocumentType());
        LOGGER.info("QuotationParticipantMap - mapForCreateArguments() CustomerDocumentType {}",quotationParticipant.getParticipant().getCustomerDocumentType());
        arguments.put(RBVDProperties.FIELD_IS_BBVA_CUSTOMER_TYPE.getValue(), quotationParticipant.getParticipant().getIsBbvaCustomerType());
        LOGGER.info("QuotationParticipantMap - mapForCreateArguments() getIsBbvaCustomerType {}",quotationParticipant.getParticipant().getIsBbvaCustomerType());
        arguments.put(RBVDProperties.FIELD_PERSONAL_ID.getValue(), quotationParticipant.getParticipant().getPersonalId());
        LOGGER.info("QuotationParticipantMap - mapForCreateArguments() PersonalId {}",quotationParticipant.getParticipant().getPersonalId());
        arguments.put(RBVDProperties.FIELD_PHONE_ID.getValue(),
                Objects.nonNull(quotationParticipant.getParticipant().getContactDetails())
                ? quotationParticipant.getParticipant().getContactDetails().getPhoneId() : null);

        arguments.put(RBVDProperties.FIELD_CUSTOMER_ENTRY_DATE.getValue(),quotationParticipant.getParticipant().getCustomerEntryDate());
        arguments.put(RBVDProperties.FIELD_PARTICIPANT_ROLE_ID.getValue(),quotationParticipant.getParticipant().getParticipantRoleId());
        arguments.put(RBVDProperties.FIELD_INSURED_CUSTOMER_NAME.getValue(),quotationParticipant.getParticipant().getInsuredCustomerName());
        arguments.put(RBVDProperties.FIELD_CLIENT_LAST_NAME.getValue(),quotationParticipant.getParticipant().getClientLastName());
        arguments.put(RBVDProperties.FIELD_USER_EMAIL_PERSONAL_DESC.getValue(),
                Objects.nonNull(quotationParticipant.getParticipant().getContactDetails())
                ? quotationParticipant.getParticipant().getContactDetails().getUserEmailPersonalDesc() : null);
        arguments.put(RBVDProperties.FIELD_CUSTOMER_BIRTH_DATE.getValue(),quotationParticipant.getParticipant().getCustomerBirthDate());
        arguments.put(RBVDProperties.FIELD_USER_AUDIT_ID.getValue(),quotationParticipant.getUserAudit());
        arguments.put(RBVDProperties.FIELD_GENDER_ID.getValue(),quotationParticipant.getParticipant().getGenderId());

        LOGGER.info("QuotationParticipantMap - mapForCreateArguments() - End");
        return arguments;
    }
}

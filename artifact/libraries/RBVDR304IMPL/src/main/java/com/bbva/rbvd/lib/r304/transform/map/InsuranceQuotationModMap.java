package com.bbva.rbvd.lib.r304.transform.map;

import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationModDAO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceQuotationModDAOImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class InsuranceQuotationModMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceQuotationModMap.class);
    private InsuranceQuotationModMap(){}
    public static Map<String, Object> createUpdateQuotationModArguments(InsuranceQuotationModDAO updateInsuranceQuotationModDao) {
        final Map<String, Object> arguments = new HashMap<>();
        arguments.put(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(), updateInsuranceQuotationModDao.getPolicyQuotaInternalId());
        LOGGER.info(" ************ createUpdateQuotationModArguments - PolicyQuotaInternalId {}",updateInsuranceQuotationModDao.getPolicyQuotaInternalId());
        LOGGER.info(" ************ createUpdateQuotationModArguments - FIELD_POLICY_QUOTA_INTERNAL_ID {}",RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue(), updateInsuranceQuotationModDao.getInsuranceProductId());
        LOGGER.info(" ************ createUpdateQuotationModArguments - FIELD_OR_FILTER_INSURANCE_PRODUCT_ID {}",RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue());
        LOGGER.info(" ************ createUpdateQuotationModArguments - getInsuranceProductId {}",updateInsuranceQuotationModDao.getInsuranceProductId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue(), updateInsuranceQuotationModDao.getInsuranceModalityType());
        LOGGER.info(" ************ createUpdateQuotationModArguments - FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE {}",RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue());
        LOGGER.info(" ************ createUpdateQuotationModArguments - getInsuranceModalityType {}",updateInsuranceQuotationModDao.getInsuranceModalityType());
        arguments.put(RBVDProperties.FIELD_PREMIUM_AMOUNT.getValue(), updateInsuranceQuotationModDao.getPremiumAmount());
        LOGGER.info(" ************ createUpdateQuotationModArguments - getPremiumAmount {}",updateInsuranceQuotationModDao.getPremiumAmount());
        LOGGER.info(" ************ createUpdateQuotationModArguments - FIELD_PREMIUM_AMOUNT {}",RBVDProperties.FIELD_PREMIUM_AMOUNT.getValue());
        arguments.put(RBVDProperties.FIELD_LAST_CHANGE_BRANCH_ID.getValue(), updateInsuranceQuotationModDao.getLastChangeBranchId());
        LOGGER.info(" ************ createUpdateQuotationModArguments - getLastChangeBranchId {}",updateInsuranceQuotationModDao.getLastChangeBranchId());
        LOGGER.info(" ************ createUpdateQuotationModArguments - FIELD_LAST_CHANGE_BRANCH_ID {}",RBVDProperties.FIELD_LAST_CHANGE_BRANCH_ID.getValue());
        return arguments;
    }

    public static Map<String, Object> createArgumentsQuotationModDao(InsuranceQuotationModDAO insuranceQuotationModDao) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(), insuranceQuotationModDao.getPolicyQuotaInternalId());
        LOGGER.info(" ************ createArgumentsQuotationModDao - PolicyQuotaInternalId {}",insuranceQuotationModDao.getPolicyQuotaInternalId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue(), insuranceQuotationModDao.getInsuranceProductId());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getInsuranceProductId {}",insuranceQuotationModDao.getInsuranceProductId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue(), insuranceQuotationModDao.getInsuranceModalityType());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getInsuranceModalityType {}",insuranceQuotationModDao.getInsuranceModalityType());
        arguments.put(RBVDProperties.FIELD_SALE_CHANNEL_ID.getValue(), insuranceQuotationModDao.getSaleChannelId());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getSaleChannelId {}",insuranceQuotationModDao.getSaleChannelId());
        arguments.put(RBVDProperties.FIELD_PAYMENT_TERM_NUMBER.getValue(), insuranceQuotationModDao.getPaymentTermNumber());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getPaymentTermNumber {}",insuranceQuotationModDao.getPaymentTermNumber());
        arguments.put(RBVDProperties.FIELD_POLICY_PAYMENT_FREQUENCY_TYPE.getValue(), insuranceQuotationModDao.getPolicyPaymentFrequencyType());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getPolicyPaymentFrequencyType {}",insuranceQuotationModDao.getPolicyPaymentFrequencyType());
        arguments.put(RBVDProperties.FIELD_FINANCING_START_DATE.getValue(), insuranceQuotationModDao.getFinancingStartDate());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getFinancingStartDate {}",insuranceQuotationModDao.getFinancingStartDate());
        arguments.put(RBVDProperties.FIELD_FINANCING_END_DATE.getValue(), insuranceQuotationModDao.getFinancingEndDate());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getFinancingEndDate {}",insuranceQuotationModDao.getFinancingEndDate());
        arguments.put(RBVDProperties.FIELD_PREMIUM_AMOUNT.getValue(), insuranceQuotationModDao.getPremiumAmount());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getPremiumAmount {}",insuranceQuotationModDao.getPremiumAmount());
        arguments.put(RBVDProperties.FIELD_PREMIUM_CURRENCY_ID.getValue(), insuranceQuotationModDao.getPremiumCurrencyId());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getPremiumCurrencyId {}",insuranceQuotationModDao.getPremiumCurrencyId());
        arguments.put(RBVDProperties.FIELD_SAVED_QUOTATION_IND_TYPE.getValue(), insuranceQuotationModDao.getSaveQuotationIndType());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getSaveQuotationIndType {}",insuranceQuotationModDao.getSaveQuotationIndType());
        arguments.put(RBVDProperties.FIELD_LAST_CHANGE_BRANCH_ID.getValue(), insuranceQuotationModDao.getLastChangeBranchId());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getLastChangeBranchId {}",insuranceQuotationModDao.getLastChangeBranchId());
        arguments.put(RBVDProperties.FIELD_SOURCE_BRANCH_ID.getValue(), insuranceQuotationModDao.getSourceBranchId());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getSourceBranchId {}",insuranceQuotationModDao.getSourceBranchId());
        arguments.put(RBVDProperties.FIELD_CREATION_USER_ID.getValue(), insuranceQuotationModDao.getCreationUser());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getCreationUser {}",insuranceQuotationModDao.getCreationUser());
        arguments.put(RBVDProperties.FIELD_USER_AUDIT_ID.getValue(), insuranceQuotationModDao.getUserAudit());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getUserAudit {}",insuranceQuotationModDao.getUserAudit());
        arguments.put(RBVDProperties.FIELD_CONTACT_EMAIL_DESC.getValue(), insuranceQuotationModDao.getContactEmailDesc());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getContactEmailDesc {}",insuranceQuotationModDao.getContactEmailDesc());
        arguments.put(RBVDProperties.FIELD_CUSTOMER_PHONE_DESC.getValue(), insuranceQuotationModDao.getCustomerPhoneDesc());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getCustomerPhoneDesc {}",insuranceQuotationModDao.getCustomerPhoneDesc());
        arguments.put(RBVDProperties.FIELD_DATA_TREATMENT_IND_TYPE.getValue(), insuranceQuotationModDao.getDataTreatmentIndType());
        LOGGER.info(" ************ createArgumentsQuotationModDao - getDataTreatmentIndType {}",insuranceQuotationModDao.getDataTreatmentIndType());
        return arguments;
    }
}

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
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue(), updateInsuranceQuotationModDao.getInsuranceProductId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue(), updateInsuranceQuotationModDao.getInsuranceModalityType());
        arguments.put(RBVDProperties.FIELD_PREMIUM_AMOUNT.getValue(), updateInsuranceQuotationModDao.getPremiumAmount());
        arguments.put(RBVDProperties.FIELD_LAST_CHANGE_BRANCH_ID.getValue(), updateInsuranceQuotationModDao.getLastChangeBranchId());
        return arguments;
    }

    public static Map<String, Object> createArgumentsQuotationModDao(InsuranceQuotationModDAO insuranceQuotationModDao) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(), insuranceQuotationModDao.getPolicyQuotaInternalId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue(), insuranceQuotationModDao.getInsuranceProductId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue(), insuranceQuotationModDao.getInsuranceModalityType());
        arguments.put(RBVDProperties.FIELD_SALE_CHANNEL_ID.getValue(), insuranceQuotationModDao.getSaleChannelId());
        arguments.put(RBVDProperties.FIELD_PAYMENT_TERM_NUMBER.getValue(), insuranceQuotationModDao.getPaymentTermNumber());
        arguments.put(RBVDProperties.FIELD_POLICY_PAYMENT_FREQUENCY_TYPE.getValue(), insuranceQuotationModDao.getPolicyPaymentFrequencyType());
        arguments.put(RBVDProperties.FIELD_FINANCING_START_DATE.getValue(), insuranceQuotationModDao.getFinancingStartDate());
        arguments.put(RBVDProperties.FIELD_FINANCING_END_DATE.getValue(), insuranceQuotationModDao.getFinancingEndDate());
        arguments.put(RBVDProperties.FIELD_PREMIUM_AMOUNT.getValue(), insuranceQuotationModDao.getPremiumAmount());
        arguments.put(RBVDProperties.FIELD_PREMIUM_CURRENCY_ID.getValue(), insuranceQuotationModDao.getPremiumCurrencyId());
        arguments.put(RBVDProperties.FIELD_SAVED_QUOTATION_IND_TYPE.getValue(), insuranceQuotationModDao.getSaveQuotationIndType());
        arguments.put(RBVDProperties.FIELD_LAST_CHANGE_BRANCH_ID.getValue(), insuranceQuotationModDao.getLastChangeBranchId());
        arguments.put(RBVDProperties.FIELD_SOURCE_BRANCH_ID.getValue(), insuranceQuotationModDao.getSourceBranchId());
        arguments.put(RBVDProperties.FIELD_CREATION_USER_ID.getValue(), insuranceQuotationModDao.getCreationUser());
        arguments.put(RBVDProperties.FIELD_USER_AUDIT_ID.getValue(), insuranceQuotationModDao.getUserAudit());
        arguments.put(RBVDProperties.FIELD_CONTACT_EMAIL_DESC.getValue(), insuranceQuotationModDao.getContactEmailDesc());
        arguments.put(RBVDProperties.FIELD_CUSTOMER_PHONE_DESC.getValue(), insuranceQuotationModDao.getCustomerPhoneDesc());
        arguments.put(RBVDProperties.FIELD_DATA_TREATMENT_IND_TYPE.getValue(), insuranceQuotationModDao.getDataTreatmentIndType());
        return arguments;
    }
}

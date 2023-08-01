package com.bbva.rbvd.lib.r304.transform.map;

import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationModDAO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;

import java.util.HashMap;
import java.util.Map;

public class QuotationModifiArgumentMap {
    private QuotationModifiArgumentMap(){}
    public Map<String, Object> createUpdateQuotationModArguments(final InsuranceQuotationModDAO updateInsuranceQuotationModDao) {
        final Map<String, Object> arguments = new HashMap<>();
        arguments.put(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(), updateInsuranceQuotationModDao.getPolicyQuotaInternalId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue(), updateInsuranceQuotationModDao.getInsuranceProductId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue(), updateInsuranceQuotationModDao.getInsuranceModalityType());
        arguments.put(RBVDProperties.FIELD_PREMIUM_AMOUNT.getValue(), updateInsuranceQuotationModDao.getPremiumAmount());
        arguments.put(RBVDProperties.FIELD_LAST_CHANGE_BRANCH_ID.getValue(), updateInsuranceQuotationModDao.getLastChangeBranchId());
        return arguments;
    }
}

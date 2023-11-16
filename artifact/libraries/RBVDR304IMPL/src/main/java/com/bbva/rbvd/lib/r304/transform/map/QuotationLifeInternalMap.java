package com.bbva.rbvd.lib.r304.transform.map;

import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;

public class QuotationLifeInternalMap {
    private QuotationLifeInternalMap(){}
    public static Map<String, Object> createArgumentForValidateQuotation(String policyQuotaInternalId) {
        return singletonMap(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(), policyQuotaInternalId);
    }
    public static Map<String, Object> createArgumentForValidateQuotationLife(String policyQuotaInternalId, BigDecimal insuranceProductId, String insuranceModalityType) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(),policyQuotaInternalId);
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue(), insuranceProductId);
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue(), insuranceModalityType);
        return arguments;
    }
    public static Map<String, Object> createArgumentForDeleteQuotation(String policyQuotaInternalId) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(),policyQuotaInternalId);
        return arguments;
    }
}

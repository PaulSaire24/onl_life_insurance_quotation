package com.bbva.rbvd.lib.r304.transform.map;

import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;

import java.util.Map;

import static java.util.Collections.singletonMap;

public class PolicyQuotaInternalMap {
    private  PolicyQuotaInternalMap(){}
    public static Map<String, Object> createArgumentForValidateQuotation(String policyQuotaInternalId) {
        return singletonMap(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(), policyQuotaInternalId);
    }
}

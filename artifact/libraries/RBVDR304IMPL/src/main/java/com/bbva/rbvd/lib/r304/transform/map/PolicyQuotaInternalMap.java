package com.bbva.rbvd.lib.r304.transform.map;

import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;

import java.util.Map;

import static java.util.Collections.singletonMap;

public class PolicyQuotaInternalMap {
    public Map<String, Object> createArgumentForValidateQuotation(final String policyQuotaInternalId) {
        return singletonMap(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(), policyQuotaInternalId);
    }
}

package com.bbva.rbvd.lib.r304.service.dao.impl;

import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.service.dao.IInsurancePolicy;
import com.bbva.rbvd.lib.r304.transform.map.PolicyQuotaInternalMap;

import java.util.Map;

public class InsurancePolicyDAO implements IInsurancePolicy {
    private PISDR350 pisdR350;
    private PolicyQuotaInternalMap policyQuotaInternalMap;
    @Override
    public static Map<String, Object> executeValidateQuotation(String policyQuotaInternalId) {
        Map<String, Object> argument = this.policyQuotaInternalMap.createArgumentForValidateQuotation(policyQuotaInternalId);
        return this.pisdR350.executeGetASingleRow(RBVDProperties.QUERY_VALIDATE_IF_QUOTATION_EXISTS.getValue(), argument);
    }
}

package com.bbva.rbvd.lib.r304.service.dao.impl;

import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.service.dao.IInsurancePolicy;
import com.bbva.rbvd.lib.r304.transform.map.PolicyQuotaInternalMap;

import java.math.BigDecimal;
import java.util.Map;

public class InsurancePolicyDAOImpl implements IInsurancePolicy {
    private final PISDR350 pisdR350;

    public InsurancePolicyDAOImpl(PISDR350 pisdR350) {
        this.pisdR350 = pisdR350;
    }

    @Override
    public Map<String, Object> executeValidateQuotation(String policyQuotaInternalId) {
        Map<String, Object> argument = PolicyQuotaInternalMap.createArgumentForValidateQuotation(policyQuotaInternalId);
        return this.pisdR350.executeGetASingleRow(RBVDProperties.QUERY_VALIDATE_IF_QUOTATION_EXISTS.getValue(), argument);
    }
    @Override
    public Map<String, Object> executeValidateQuotationLife(String policyQuotaInternalId, BigDecimal insuranceProductId, String insuranceModalityType) {
        Map<String, Object> argument = PolicyQuotaInternalMap.createArgumentForValidateQuotationMod(policyQuotaInternalId,insuranceProductId,insuranceModalityType);
        return this.pisdR350.executeGetASingleRow("PISD.VALIDATE_IF_QUOTATION_LIFE_EXISTS", argument);
    }

}

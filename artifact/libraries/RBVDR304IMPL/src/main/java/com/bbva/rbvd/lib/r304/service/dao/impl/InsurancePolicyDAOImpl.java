package com.bbva.rbvd.lib.r304.service.dao.impl;

import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.impl.util.ConstantUtils;
import com.bbva.rbvd.lib.r304.service.dao.IInsurancePolicy;
import com.bbva.rbvd.lib.r304.transform.map.QuotationLifeInternalMap;

import java.math.BigDecimal;
import java.util.Map;

public class InsurancePolicyDAOImpl implements IInsurancePolicy {
    private final PISDR350 pisdR350;

    public InsurancePolicyDAOImpl(PISDR350 pisdR350) {
        this.pisdR350 = pisdR350;
    }

    @Override
    public Map<String, Object> executeValidateQuotation(String policyQuotaInternalId) {
        Map<String, Object> argument = QuotationLifeInternalMap.createArgumentForValidateQuotation(policyQuotaInternalId);
        return this.pisdR350.executeGetASingleRow(RBVDProperties.QUERY_VALIDATE_IF_QUOTATION_EXISTS.getValue(), argument);
    }
    @Override
    public Map<String, Object> executeValidateQuotationLife(String policyQuotaInternalId, BigDecimal insuranceProductId, String insuranceModalityType) {
        Map<String, Object> argument = QuotationLifeInternalMap.createArgumentForValidateQuotationLife(policyQuotaInternalId,insuranceProductId,insuranceModalityType);
        return this.pisdR350.executeGetASingleRow(ConstantUtils.VALIDATE_IF_QUOTATION_LIFE_EXISTS, argument);
    }

}

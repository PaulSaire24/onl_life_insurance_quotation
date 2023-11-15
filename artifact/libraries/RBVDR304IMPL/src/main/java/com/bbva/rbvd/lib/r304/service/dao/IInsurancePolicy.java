package com.bbva.rbvd.lib.r304.service.dao;

import java.math.BigDecimal;
import java.util.Map;

public interface IInsurancePolicy {
    Map<String, Object> executeValidateQuotation(String policyQuotaInternalId);
    public Map<String, Object> executeValidateQuotationMod(String policyQuotaInternalId, BigDecimal insuranceProductId, String insuranceModalityType) ;

    }

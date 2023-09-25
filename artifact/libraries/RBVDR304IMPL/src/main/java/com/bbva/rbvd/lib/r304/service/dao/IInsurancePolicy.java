package com.bbva.rbvd.lib.r304.service.dao;

import java.util.Map;

public interface IInsurancePolicy {
    Map<String, Object> executeValidateQuotation(String policyQuotaInternalId);
}

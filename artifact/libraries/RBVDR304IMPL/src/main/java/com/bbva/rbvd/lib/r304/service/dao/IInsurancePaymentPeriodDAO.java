package com.bbva.rbvd.lib.r304.service.dao;

import java.util.Map;

public interface IInsurancePaymentPeriodDAO {
    Map<String, Object> executeGetPaymentFrequencyName(String frequencyTypeId);
}

package com.bbva.rbvd.lib.r304.service.dao;

import java.util.Map;

public interface IInsuranceProductModalityDAO {
    Map<String, Object> executeGetRequiredInformation(final String productType, final String planId);
}

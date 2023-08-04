package com.bbva.rbvd.lib.r304.service.dao;

import java.util.Map;

public interface IInsuranceSimulationDAO {
    Map<String, Object> executeGetSimulationInformation(final String externalSimulationId);
}

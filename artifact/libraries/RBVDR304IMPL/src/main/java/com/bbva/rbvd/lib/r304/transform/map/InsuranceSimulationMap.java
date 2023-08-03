package com.bbva.rbvd.lib.r304.transform.map;

import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;

import java.util.HashMap;
import java.util.Map;

public class InsuranceSimulationMap {
    private InsuranceSimulationMap() {}

    public static Map<String, Object> getRequiredInformation(final String productType, final String planId) {
        final Map<String, Object> argumentsGetRequiredInformation = new HashMap<>();
        argumentsGetRequiredInformation.put(RBVDProperties.FILTER_INSURANCE_PRODUCT_TYPE.getValue(), productType);
        argumentsGetRequiredInformation.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue(), planId);

        return argumentsGetRequiredInformation;
    }
}

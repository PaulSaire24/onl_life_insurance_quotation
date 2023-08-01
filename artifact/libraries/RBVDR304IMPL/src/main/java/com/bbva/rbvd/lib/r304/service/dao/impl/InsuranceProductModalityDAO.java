package com.bbva.rbvd.lib.r304.service.dao.impl;

import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceProductModalityDAO;

import java.util.Map;

import static com.bbva.rbvd.lib.r304.impl.util.ValidationUtil.validateSelectionQueries;

public class InsuranceProductModalityDAO implements IInsuranceProductModalityDAO {
    private PISDR350 pisdR350;

    @Override
    public Map<String, Object> executeGetRequiredInformation(String productType, String planId, Map<String, Object> argumentsGetRequiredInformation) {
        Map<String, Object> responseGetRequiredInformation = this.pisdR350.executeGetASingleRow(
                RBVDProperties.QUERY_GET_REQUIRED_INFORMATION_FOR_EASYES_QUOTATION.getValue(), argumentsGetRequiredInformation);
        validateSelectionQueries(responseGetRequiredInformation, RBVDErrors.INVALID_PRODUCT_TYPE_AND_MODALITY_TYPE);
        return responseGetRequiredInformation;
    }
}

package com.bbva.rbvd.lib.r304.service.dao.impl;

import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceSimulationDAO;

import java.util.Map;

import static com.bbva.rbvd.lib.r304.impl.util.ValidationUtil.validateSelectionQueries;
import static java.util.Collections.singletonMap;

public class InsuranceSimulationDAO implements IInsuranceSimulationDAO {
    PISDR350 pisdR350;

    public InsuranceSimulationDAO(PISDR350 pisdR350) {
        this.pisdR350 = pisdR350;
    }

    @Override
    public Map<String, Object> executeGetSimulationInformation(String externalSimulationId) {
        final Map<String, Object> responseGetSimulationIdAndExpirationDate = this.pisdR350.executeGetASingleRow(
                RBVDProperties.QUERY_GET_SIMULATION_INFORMATION_FOR_EASYES_QUOTATION.getValue(),
                singletonMap(RBVDProperties.FIELD_INSRNC_COMPANY_SIMULATION_ID.getValue(),externalSimulationId)
        );

        validateSelectionQueries(responseGetSimulationIdAndExpirationDate, RBVDErrors.INVALID_RIMAC_QUOTATION_ID);

        return responseGetSimulationIdAndExpirationDate;
    }
}

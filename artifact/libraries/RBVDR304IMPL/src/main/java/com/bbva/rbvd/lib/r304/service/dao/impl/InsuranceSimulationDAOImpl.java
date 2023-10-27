package com.bbva.rbvd.lib.r304.service.dao.impl;

import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceSimulationDAO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import com.bbva.rbvd.lib.r304.impl.util.ValidationUtil;

import static java.util.Collections.singletonMap;

public class InsuranceSimulationDAOImpl implements IInsuranceSimulationDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceSimulationDAOImpl.class);
    private final PISDR350 pisdR350;

    public InsuranceSimulationDAOImpl(PISDR350 pisdR350) {
        this.pisdR350 = pisdR350;
    }

    @Override
    public Map<String, Object> executeGetSimulationInformation(String externalSimulationId) {

        LOGGER.info("***** InsuranceSimulationDAOImpl - executeGetSimulationInformation START *****");

        final Map<String, Object> responseGetSimulationIdAndExpirationDate = this.pisdR350.executeGetASingleRow(
                RBVDProperties.QUERY_GET_SIMULATION_INFORMATION_FOR_EASYES_QUOTATION.getValue(),
                singletonMap(RBVDProperties.FIELD_INSRNC_COMPANY_SIMULATION_ID.getValue(),externalSimulationId)
        );

        ValidationUtil.validateSelectionQueries(responseGetSimulationIdAndExpirationDate, RBVDErrors.INVALID_RIMAC_QUOTATION_ID);

        return responseGetSimulationIdAndExpirationDate;
    }
}

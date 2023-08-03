package com.bbva.rbvd.lib.r304.service.dao.impl;

import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.service.dao.IInsurancePaymentPeriodDAO;

import java.util.Map;

import static java.util.Collections.singletonMap;

public class InsurancePaymentPeriodDAO implements IInsurancePaymentPeriodDAO {

    private PISDR350 pisdR350;

    public InsurancePaymentPeriodDAO(PISDR350 pisdR350) {
        this.pisdR350 = pisdR350;
    }
    @Override
    public Map<String, Object> executeGetPaymentFrequencyName(String frequencyTypeId) {
        return this.pisdR350.executeGetASingleRow(RBVDProperties.QUERY_GET_PAYMENT_FREQUENCY_NAME.getValue(),
                singletonMap(RBVDProperties.FIELD_POLICY_PAYMENT_FREQUENCY_TYPE.getValue(), frequencyTypeId));
    }
}

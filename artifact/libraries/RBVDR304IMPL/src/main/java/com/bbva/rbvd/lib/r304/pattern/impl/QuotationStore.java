package com.bbva.rbvd.lib.r304.pattern.impl;

import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.pattern.PostQuotation;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsurancePolicyDAO;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceQuotationDAO;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceQuotationModDAO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;

import java.math.BigDecimal;
import java.util.Map;

public class QuotationStore implements PostQuotation {
    private final PISDR350 pisdR350;

    public QuotationStore(PISDR350 pisdR350) {
        this.pisdR350 = pisdR350;
    }

    @Override
    public void end(PayloadStore payloadStore) {
        BigDecimal resultCount = this.getQuotationIdFromDB(payloadStore);
        this.save(payloadStore, resultCount);
    }


    private BigDecimal getQuotationIdFromDB(PayloadStore payloadStore){
        InsurancePolicyDAO insurancePolicy = new InsurancePolicyDAO(this.pisdR350);
        Map<String, Object> responseValidateQuotation = insurancePolicy.executeValidateQuotation(payloadStore.getInput().getId());
        return (BigDecimal) responseValidateQuotation.get(RBVDProperties.FIELD_RESULT_NUMBER.getValue());
    }

    private void save(PayloadStore payloadStore, BigDecimal resultCount){
        InsuranceQuotationModDAO insuranceQuotationMod = new InsuranceQuotationModDAO(pisdR350);

        if(BigDecimal.ONE.compareTo(resultCount) == 0) {
            insuranceQuotationMod.executeUpdateQuotationModQuery(payloadStore.getMyQuotation(), payloadStore.getInput());
        } else {
            InsuranceQuotationDAO insuranceQuotation = new InsuranceQuotationDAO(pisdR350);
            insuranceQuotation.executeQuotationQuery(payloadStore.getMyQuotation(), payloadStore.getInput());
            insuranceQuotationMod.executeQuotationModQuery(payloadStore.getMyQuotation(), payloadStore.getInput(), payloadStore.getRimacQuotationResponse());
        }
    }
}

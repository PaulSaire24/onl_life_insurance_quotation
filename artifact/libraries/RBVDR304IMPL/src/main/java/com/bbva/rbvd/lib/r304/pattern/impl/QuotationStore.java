package com.bbva.rbvd.lib.r304.pattern.impl;

import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.pattern.PostQuotation;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceModalityTypeUpdateDAO;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsurancePolicyDAO;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceQuotationInsertModDAO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import com.bbva.rbvd.lib.r304.transform.bean.InsuranceQuotationModBean;

import java.math.BigDecimal;
import java.util.Map;

public class QuotationStore implements PostQuotation {
    private PISDR350 pisdR350;

    public QuotationStore(PISDR350 pisdR350) {
        this.pisdR350 = pisdR350;
    }

    @Override
    public void end(PayloadStore payloadStore) {
        BigDecimal resultCount = this.resultCountNumber(payloadStore);
        this.compareTO(payloadStore, resultCount);
    }

    private BigDecimal resultCountNumber(PayloadStore payloadStore){
        InsurancePolicyDAO insurancePolicy = new InsurancePolicyDAO();
        Map<String, Object> responseValidateQuotation = insurancePolicy.executeValidateQuotation(payloadStore.getPayloadConfig().getQuotation().getId());
        return (BigDecimal) responseValidateQuotation.get(RBVDProperties.FIELD_RESULT_NUMBER.getValue());
    }

    private void compareTO(PayloadStore payloadStore, BigDecimal resultCount){
        if(BigDecimal.ONE.compareTo(resultCount) == 0) {
            InsuranceModalityTypeUpdateDAO insuranceModalityTypeUpdate = new InsuranceModalityTypeUpdateDAO(pisdR350);
            insuranceModalityTypeUpdate.executeUpdateQuotationModQuery(payloadStore.getPayloadConfig().getQuotationDao(),
                                                                       payloadStore.getPayloadConfig().getQuotation());
        } else {
            InsuranceQuotationInsertModDAO insuranceQuotationInsertMod = new InsuranceQuotationInsertModDAO(pisdR350);
            insuranceQuotationInsertMod.executeQuotationQuery(payloadStore.getPayloadConfig().getQuotationDao(),
                                                              payloadStore.getPayloadConfig().getQuotation());

            InsuranceQuotationModBean.createQuotationModDao(payloadStore.getPayloadConfig().getQuotationDao(),
                                                            payloadStore.getPayloadConfig().getQuotation(),
                                                            payloadStore.getPayloadConfig().getEasyesQuotationBO());
        }
    }
}

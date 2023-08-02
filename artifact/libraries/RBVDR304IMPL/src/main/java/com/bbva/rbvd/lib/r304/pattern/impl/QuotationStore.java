package com.bbva.rbvd.lib.r304.pattern.impl;
import com.bbva.rbvd.lib.r304.pattern.PostQuotation;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceModalityTypeUpdateDAO;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceModalityTypeUpdateDAO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import java.math.BigDecimal;

public class QuotationStore implements PostQuotation {
    private PayloadStore payloadStore;

    @Override
    public void end(PayloadStore payloadStore) {

        IInsuranceModalityTypeUpdateDAO insuranceModalityTypeUpdate = new InsuranceModalityTypeUpdateDAO();


        if(BigDecimal.ONE.compareTo(payloadStore.getResultCount()) == 0) {
           insuranceModalityTypeUpdate.executeUpdateQuotationModQuery(payloadConfig, easyesQuotation);
         } else {
            insuranceModalityTypeUpdate.executeQuotationQuery(easyesQuotationDao, easyesQuotation);
            insuranceModalityTypeUpdate.executeQuotationModQuery(easyesQuotationDao, easyesQuotation, rimacQuotationResponse);
         }

        //this.mapperHelper.mappingOutputFields(easyesQuotation, easyesQuotationDao);
    }
}

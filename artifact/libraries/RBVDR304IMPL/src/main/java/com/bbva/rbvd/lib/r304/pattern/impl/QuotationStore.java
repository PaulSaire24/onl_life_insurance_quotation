package com.bbva.rbvd.lib.r304.pattern.impl;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
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


        EasyesQuotationDTO easyesQuotation = null;
        Object rimacQuotationResponse = null;
        if (BigDecimal.ONE.compareTo(payloadStore.getResultCount()) == 0) {
            insuranceModalityTypeUpdate.executeUpdateQuotationModQuery(payloadStore.getResponseValidateQuotation(), easyesQuotation, rimacQuotationResponse);
        } else {
            EasyesQuotationDAO easyesQuotationDao = null;
            insuranceModalityTypeUpdate.executeUpdateQuotationModQuery(easyesQuotationDao, easyesQuotation, rimacQuotationResponse);
            rimacQuotationResponse = null;
            insuranceModalityTypeUpdate.executeUpdateQuotationModQuery(easyesQuotationDao, easyesQuotation, rimacQuotationResponse);
        }

        //this.mapperHelper.mappingOutputFields(easyesQuotation, easyesQuotationDao);
    }
}

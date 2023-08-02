package com.bbva.rbvd.lib.r304.pattern.impl;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.pattern.PostQuotation;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceModalityTypeUpdateDAO;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceQuotationInsertModDAO;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsurancePolicyDAO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import com.bbva.rbvd.lib.r304.transform.bean.InsuranceQuotationModBean;

import java.math.BigDecimal;
import java.util.Map;

public class QuotationStore implements PostQuotation {

    private IInsuranceModalityTypeUpdateDAO InsuranceModalityTypeUpdateDAO;
    private IInsuranceQuotationInsertModDAO daoService;
    private EasyesQuotationDAO easyesQuotationDao;
    private EasyesQuotationDTO easyesQuotation;
    private EasyesQuotationBO easyesQuotationBO;

    @Override
    public void end(PayloadStore payloadStore) {
        BigDecimal resultCount = this.resultCountNumber(payloadStore);
        this.compareTO(payloadStore, resultCount);
    }

    private BigDecimal resultCountNumber(PayloadStore payloadStore){
        Map<String, Object> responseValidateQuotation = InsurancePolicyDAO.executeValidateQuotation(payloadStore.getPayloadConfig().getQuotation().getId());
        return (BigDecimal) responseValidateQuotation.get(RBVDProperties.FIELD_RESULT_NUMBER.getValue());
    }

    private void compareTO(PayloadStore payloadStore, BigDecimal resultCount){
        if(BigDecimal.ONE.compareTo(resultCount) == 0) {

            InsuranceModalityTypeUpdateDAO.executeUpdateQuotationModQuery(payloadStore.getPayloadConfig().getEasyesQuotationDao(), payloadStore.getPayloadConfig().getQuotation());
        } else {
            this.daoService.executeQuotationQuery(easyesQuotationDao, easyesQuotation);
            InsuranceQuotationModBean.createQuotationModDao(easyesQuotationDao, easyesQuotation, easyesQuotationBO);
        }
    }
}

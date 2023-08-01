package com.bbva.rbvd.lib.r304.transfer;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;

import java.util.Map;

public class PayloadConfig {

    private EasyesQuotationDAO easyesQuotationDao;
    private PayloadProperties payloadProperties;

    public EasyesQuotationDAO getEasyesQuotationDao() {
        return easyesQuotationDao;
    }

    public void setEasyesQuotationDao(EasyesQuotationDAO easyesQuotationDao) {
        this.easyesQuotationDao = easyesQuotationDao;
    }

    public PayloadProperties getPayloadProperties() {
        return payloadProperties;
    }

    public void setPayloadProperties(PayloadProperties payloadProperties) {
        this.payloadProperties = payloadProperties;
    }
}

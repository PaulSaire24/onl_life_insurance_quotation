package com.bbva.rbvd.lib.r304.transfer;

import com.bbva.pisd.dto.insurance.aso.gifole.GifoleInsuranceRequestASO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;

public class PayloadConfig {

    private EasyesQuotationDAO easyesQuotationDao;
    private String PolicyQuotaid;
    private PayloadProperties payloadProperties;

    public EasyesQuotationDAO getEasyesQuotationDao() {
        return easyesQuotationDao;
    }

    public void setEasyesQuotationDao(EasyesQuotationDAO easyesQuotationDao) {
        this.easyesQuotationDao = easyesQuotationDao;
    }

    public String getPolicyQuotaid() {
        return PolicyQuotaid;
    }

    public void setPolicyQuotaid(String policyQuotaid) {
        PolicyQuotaid = policyQuotaid;
    }

    public PayloadProperties getPayloadProperties() {
        return payloadProperties;
    }

    public void setPayloadProperties(PayloadProperties payloadProperties) {
        this.payloadProperties = payloadProperties;
    }

    @Override
    public String toString() {
        return "PayloadConfig{" +
                "easyesQuotationDao=" + easyesQuotationDao +
                ", PolicyQuotaid='" + PolicyQuotaid + '\'' +
                ", payloadProperties=" + payloadProperties +
                '}';
    }

    public GifoleInsuranceRequestASO getInput() {
        return null;
    }
}

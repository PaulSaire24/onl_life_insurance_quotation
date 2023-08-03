package com.bbva.rbvd.lib.r304.transfer;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;

public class PayloadConfig {

    private EasyesQuotationDAO myQuotation;
    private String policyQuotaId;
    private EasyesQuotationDTO input;
    private PayloadProperties payloadProperties;

    public EasyesQuotationDAO getMyQuotation() {
        return myQuotation;
    }

    public void setMyQuotation(EasyesQuotationDAO myQuotation) {
        this.myQuotation = myQuotation;
    }

    public String getPolicyQuotaId() {
        return policyQuotaId;
    }

    public void setPolicyQuotaId(String policyQuotaId) {
        this.policyQuotaId = policyQuotaId;
    }

    public EasyesQuotationDTO getInput() {
        return input;
    }

    public void setInput(EasyesQuotationDTO input) {
        this.input = input;
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
                "myQuotation=" + myQuotation +
                ", policyQuotaId='" + policyQuotaId + '\'' +
                ", input=" + input +
                ", payloadProperties=" + payloadProperties +
                '}';
    }

//public GifoleInsuranceRequestASO getInput() {return null;}
}

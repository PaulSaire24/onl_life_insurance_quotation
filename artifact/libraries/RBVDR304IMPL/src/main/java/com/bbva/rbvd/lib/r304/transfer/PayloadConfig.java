package com.bbva.rbvd.lib.r304.transfer;

import com.bbva.pisd.dto.insurance.aso.gifole.GifoleInsuranceRequestASO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;

public class PayloadConfig {

    private EasyesQuotationDAO quotationDao;
    private EasyesQuotationBO easyesQuotationBO;
    private String PolicyQuotaid;
    private EasyesQuotationDTO Quotation;
    private PayloadProperties payloadProperties;

    public EasyesQuotationDAO getQuotationDao() {
        return quotationDao;
    }

    public void setQuotationDao(EasyesQuotationDAO quotationDao) {
        this.quotationDao = quotationDao;
    }

    public EasyesQuotationBO getEasyesQuotationBO() {
        return easyesQuotationBO;
    }

    public void setEasyesQuotationBO(EasyesQuotationBO easyesQuotationBO) {
        this.easyesQuotationBO = easyesQuotationBO;
    }

    public String getPolicyQuotaid() {
        return PolicyQuotaid;
    }

    public void setPolicyQuotaid(String policyQuotaid) {
        PolicyQuotaid = policyQuotaid;
    }

    public EasyesQuotationDTO getQuotation() {
        return Quotation;
    }

    public void setQuotation(EasyesQuotationDTO quotation) {
        Quotation = quotation;
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
                "quotationDao=" + quotationDao +
                ", easyesQuotationBO=" + easyesQuotationBO +
                ", PolicyQuotaid='" + PolicyQuotaid + '\'' +
                ", Quotation=" + Quotation +
                ", payloadProperties=" + payloadProperties +
                '}';
    }

    public GifoleInsuranceRequestASO getInput() {
        return null;
    }
}

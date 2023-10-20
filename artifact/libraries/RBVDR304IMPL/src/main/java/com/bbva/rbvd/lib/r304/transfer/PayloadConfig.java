package com.bbva.rbvd.lib.r304.transfer;

import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;

public class PayloadConfig {

    private EasyesQuotationDAO myQuotation;
    private CustomerListASO customerInformation;
    private String policyQuotaId;
    private QuotationLifeDTO input;
    private PayloadProperties payloadProperties;

    public EasyesQuotationDAO getMyQuotation() {
        return myQuotation;
    }

    public void setMyQuotation(EasyesQuotationDAO myQuotation) {
        this.myQuotation = myQuotation;
    }

    public CustomerListASO getCustomerInformation() {
        return customerInformation;
    }

    public void setCustomerInformation(CustomerListASO customerInformation) {
        this.customerInformation = customerInformation;
    }

    public String getPolicyQuotaId() {
        return policyQuotaId;
    }

    public void setPolicyQuotaId(String policyQuotaId) {
        this.policyQuotaId = policyQuotaId;
    }

    public QuotationLifeDTO getInput() {
        return input;
    }

    public void setInput(QuotationLifeDTO input) {
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
                ", customerListASO=" + customerInformation +
                ", policyQuotaId='" + policyQuotaId + '\'' +
                ", input=" + input +
                ", payloadProperties=" + payloadProperties +
                '}';
    }
}

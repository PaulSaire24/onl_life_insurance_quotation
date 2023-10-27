package com.bbva.rbvd.lib.r304.transfer;

import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.QuotationLifeBO;

public class PayloadStore {
    private CustomerListASO customerInformation;
    private QuotationLifeDTO input;
    private EasyesQuotationDAO myQuotation;
    private QuotationLifeBO rimacResponse;
    private String frequencyType;

    public CustomerListASO getCustomerInformation() {
        return customerInformation;
    }

    public void setCustomerInformation(CustomerListASO customerInformation) {
        this.customerInformation = customerInformation;
    }

    public QuotationLifeDTO getInput() {
        return input;
    }

    public void setInput(QuotationLifeDTO input) {
        this.input = input;
    }

    public EasyesQuotationDAO getMyQuotation() {
        return myQuotation;
    }

    public void setMyQuotation(EasyesQuotationDAO myQuotation) {
        this.myQuotation = myQuotation;
    }

    public QuotationLifeBO getRimacResponse() {
        return rimacResponse;
    }

    public void setRimacResponse(QuotationLifeBO rimacResponse) {
        this.rimacResponse = rimacResponse;
    }

    public String getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }

}

package com.bbva.rbvd.lib.r304.transfer;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;

public class PayloadStore {
    private QuotationLifeDTO input;
    private EasyesQuotationDAO myQuotation;
    private EasyesQuotationBO rimacResponse;
    private String frequencyType;

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

    public EasyesQuotationBO getRimacResponse() {
        return rimacResponse;
    }

    public void setRimacResponse(EasyesQuotationBO rimacResponse) {
        this.rimacResponse = rimacResponse;
    }

    public String getFrequencyType() {
        return frequencyType;
    }

    public void setFrequencyType(String frequencyType) {
        this.frequencyType = frequencyType;
    }

}

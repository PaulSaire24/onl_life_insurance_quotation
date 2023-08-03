package com.bbva.rbvd.lib.r304.transfer;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;

public class PayloadStore {
    private EasyesQuotationDTO input;
    private EasyesQuotationDAO myQuotation;
    private EasyesQuotationBO rimacQuotationResponse;
    public PayloadStore(){}

    public EasyesQuotationDTO getInput() {
        return input;
    }

    public void setInput(EasyesQuotationDTO input) {
        this.input = input;
    }

    public EasyesQuotationDAO getMyQuotation() {
        return myQuotation;
    }

    public void setMyQuotation(EasyesQuotationDAO myQuotation) {
        this.myQuotation = myQuotation;
    }

    public EasyesQuotationBO getRimacQuotationResponse() {
        return rimacQuotationResponse;
    }

    public void setRimacQuotationResponse(EasyesQuotationBO rimacQuotationResponse) {
        this.rimacQuotationResponse = rimacQuotationResponse;
    }

    @Override
    public String toString() {
        return "PayloadStore{" +
                "quotationDTO=" + input +
                ", quotationDAO=" + myQuotation +
                ", rimacQuotationResponse=" + rimacQuotationResponse +
                '}';
    }
}

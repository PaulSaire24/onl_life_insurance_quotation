package com.bbva.rbvd.lib.r304.transfer;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;

import java.math.BigDecimal;
import java.util.Map;

public class PayloadStore {
    private Map<String, Object> responseValidateQuotation;
    private PayloadConfig payloadConfig;
    private BigDecimal resultCount;

    public PayloadStore(Map<String, Object> responseValidateQuotation, PayloadConfig payloadConfig, BigDecimal resultCount) {
        this.responseValidateQuotation = responseValidateQuotation;
        this.payloadConfig = payloadConfig;
        this.resultCount = resultCount;
    }

    public Map<String, Object> getResponseValidateQuotation() {
        return responseValidateQuotation;
    }

    public void setResponseValidateQuotation(Map<String, Object> responseValidateQuotation) {
        this.responseValidateQuotation = responseValidateQuotation;
    }

    public PayloadConfig getPayloadConfig() {
        return payloadConfig;
    }

    public void setPayloadConfig(PayloadConfig payloadConfig) {
        this.payloadConfig = payloadConfig;
    }

    public BigDecimal getResultCount() {
        return resultCount;
    }

    public void setResultCount(BigDecimal resultCount) {
        this.resultCount = resultCount;
    }
}

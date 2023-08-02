package com.bbva.rbvd.lib.r304.transfer;
import java.math.BigDecimal;
import java.util.Map;

public class PayloadStore {
    private Map<String, Object> responseValidateQuotation;

    private BigDecimal resultCount;

    public PayloadStore(Map<String, Object> responseValidateQuotation) {
        this.responseValidateQuotation = responseValidateQuotation;
    }

    public PayloadStore(Map<String, Object> responseValidateQuotation, BigDecimal resultCount) {
        this.responseValidateQuotation = responseValidateQuotation;
        this.resultCount = resultCount;
    }

    public Map<String, Object> getResponseValidateQuotation() {
        return responseValidateQuotation;
    }

    public void setResponseValidateQuotation(Map<String, Object> responseValidateQuotation) {
        this.responseValidateQuotation = responseValidateQuotation;
    }

    public BigDecimal getResultCount() {
        return resultCount;
    }

    public void setResultCount(BigDecimal resultCount) {
        this.resultCount = resultCount;
    }

    @Override
    public String toString() {
        return "PayloadStore{" +
                "responseValidateQuotation=" + responseValidateQuotation +
                ", resultCount=" + resultCount +
                '}';
    }
}

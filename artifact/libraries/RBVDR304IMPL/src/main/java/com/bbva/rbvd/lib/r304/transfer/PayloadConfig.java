package com.bbva.rbvd.lib.r304.transfer;

import java.util.Map;

public class PayloadConfig {

    private Map<String, Object> responseGetSimulationInformation;
    private Map<String, Object> responseGetRequiredInformation;
    private Map<String, Object> responsePaymentFrequencyName;
    private PayloadProperties payloadProperties;

    public Map<String, Object> getResponseGetSimulationInformation() {
        return responseGetSimulationInformation;
    }

    public void setResponseGetSimulationInformation(Map<String, Object> responseGetSimulationInformation) {
        this.responseGetSimulationInformation = responseGetSimulationInformation;
    }

    public Map<String, Object> getResponseGetRequiredInformation() {
        return responseGetRequiredInformation;
    }

    public void setResponseGetRequiredInformation(Map<String, Object> responseGetRequiredInformation) {
        this.responseGetRequiredInformation = responseGetRequiredInformation;
    }

    public Map<String, Object> getResponsePaymentFrequencyName() {
        return responsePaymentFrequencyName;
    }

    public void setResponsePaymentFrequencyName(Map<String, Object> responsePaymentFrequencyName) {
        this.responsePaymentFrequencyName = responsePaymentFrequencyName;
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
                "responseGetSimulationInformation=" + responseGetSimulationInformation +
                ", responseGetRequiredInformation=" + responseGetRequiredInformation +
                ", responsePaymentFrequencyName=" + responsePaymentFrequencyName +
                ", payloadProperties=" + payloadProperties +
                '}';
    }
}

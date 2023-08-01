package com.bbva.rbvd.lib.r304.transfer;

public class PayloadConfig {

    private PayloadProperties properties;


    public PayloadProperties getProperties() {
        return properties;
    }

    public void setProperties(PayloadProperties properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return "PayloadConfig{" +
                "properties=" + properties +
                '}';
    }

}


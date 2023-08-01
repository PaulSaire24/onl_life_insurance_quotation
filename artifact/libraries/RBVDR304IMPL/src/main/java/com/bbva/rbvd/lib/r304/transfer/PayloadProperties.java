package com.bbva.rbvd.lib.r304.transfer;

public class PayloadProperties {
    private String productType;
    private String selectedPlanId;
    private String periodId;
    private String frequencyTypeId;

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getSelectedPlanId() {
        return selectedPlanId;
    }

    public void setSelectedPlanId(String selectedPlanId) {
        this.selectedPlanId = selectedPlanId;
    }

    public String getPeriodId() {
        return periodId;
    }

    public void setPeriodId(String periodId) {
        this.periodId = periodId;
    }

    public String getFrequencyTypeId() {
        return frequencyTypeId;
    }

    public void setFrequencyTypeId(String frequencyTypeId) {
        this.frequencyTypeId = frequencyTypeId;
    }

    @Override
    public String toString() {
        return "PayloadProperties{" +
                "productType='" + productType + '\'' +
                ", selectedPlanId='" + selectedPlanId + '\'' +
                ", periodId='" + periodId + '\'' +
                ", frequencyTypeId='" + frequencyTypeId + '\'' +
                '}';
    }
}

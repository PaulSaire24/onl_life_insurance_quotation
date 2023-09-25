package com.bbva.rbvd.lib.r304.transfer;

public class PayloadProperties {
    private String productType;
    private String selectedPlanId;
    private String periodId;
    private String frequencyTypeId;

    public void setProductType(String productType) {
        this.productType = productType;
    }
    public void setSelectedPlanId(String selectedPlanId) {
        this.selectedPlanId = selectedPlanId;
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

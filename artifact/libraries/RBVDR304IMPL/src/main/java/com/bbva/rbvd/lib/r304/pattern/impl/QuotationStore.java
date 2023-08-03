package com.bbva.rbvd.lib.r304.pattern.impl;

import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.pattern.PostQuotation;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceQuotationDAO;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceQuotationModDAO;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsurancePolicyDAO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;

import java.math.BigDecimal;
import java.util.Map;

import static java.util.Objects.nonNull;

public class QuotationStore implements PostQuotation {
    private final PISDR350 pisdR350;
    private final RBVDR303 rbvdR303;



    public QuotationStore(PISDR350 pisdR350, RBVDR303 rbvdR303) {
        this.pisdR350 = pisdR350;
        this.rbvdR303 = rbvdR303;
    }

    @Override
    public void end(PayloadStore payloadStore) {
        BigDecimal resultCount = this.getQuotationIdFromDB(payloadStore);
        this.save(payloadStore, resultCount);
        this.mappingOutputFields(payloadStore.getInput(),payloadStore.getMyQuotation());

    }


    private BigDecimal getQuotationIdFromDB(PayloadStore payloadStore){
        InsurancePolicyDAO insurancePolicy = new InsurancePolicyDAO(this.pisdR350);
        Map<String, Object> responseValidateQuotation = insurancePolicy.executeValidateQuotation(payloadStore.getInput().getId());
        return (BigDecimal) responseValidateQuotation.get(RBVDProperties.FIELD_RESULT_NUMBER.getValue());
    }

    private void save(PayloadStore payloadStore, BigDecimal resultCount){
        InsuranceQuotationModDAO insuranceQuotationMod = new InsuranceQuotationModDAO(pisdR350);

        if(BigDecimal.ONE.compareTo(resultCount) == 0) {
            insuranceQuotationMod.executeUpdateQuotationModQuery(payloadStore.getMyQuotation(), payloadStore.getInput());
        } else {
            InsuranceQuotationDAO insuranceQuotation = new InsuranceQuotationDAO(pisdR350);
            insuranceQuotation.executeQuotationQuery(payloadStore.getMyQuotation(), payloadStore.getInput());
            insuranceQuotationMod.executeQuotationModQuery(payloadStore.getMyQuotation(), payloadStore.getInput(), payloadStore.getRimacQuotationResponse());
        }
    }

    private void mappingOutputFields(EasyesQuotationDTO input, EasyesQuotationDAO myQuotation) {

        final String defaultValue = "";

        CustomerBO customerInformation = this.rbvdR303.executeListCustomerService(input.getHolder().getId());

        if(nonNull(customerInformation)) {
            input.getHolder().setFirstName(customerInformation.getFirstName());
            input.getHolder().setLastName(customerInformation.getLastName());
            final String fullName = customerInformation.getFirstName().concat(" ").
                    concat(customerInformation.getLastName()).concat(" ").concat(customerInformation.getSecondLastName());
            input.getHolder().setFullName(fullName);
        } else {
            input.getHolder().setFirstName(defaultValue);
            input.getHolder().setLastName(defaultValue);
            input.getHolder().setFullName(defaultValue);
        }

        input.getProduct().getPlans().get(0).getInstallmentPlans().get(0).getPeriod()
                .setName(myQuotation.getPaymentFrequencyName());
    }

}

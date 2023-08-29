package com.bbva.rbvd.lib.r304.pattern.impl;

import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.pattern.PostQuotation;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsurancePolicyDAOImpl;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceQuotationDAOImpl;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceQuotationModDAOImpl;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Map;

public class QuotationStore implements PostQuotation {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuotationStore.class);
    private final PISDR350 pisdR350;

    public QuotationStore(PISDR350 pisdR350) {
        this.pisdR350 = pisdR350;
    }

    @Override
    public void end(PayloadStore payloadStore) {
        BigDecimal resultCount = this.getQuotationIdFromDB(payloadStore);
        this.save(payloadStore, resultCount);
    }


    private BigDecimal getQuotationIdFromDB(PayloadStore payloadStore){

        LOGGER.info("***** QuotationStore - getQuotationIdFromDB START *****");
        LOGGER.info("***** QuotationStore - getQuotationIdFromDB START, parameter payloadStore: {} *****", payloadStore);

        InsurancePolicyDAOImpl insurancePolicy = new InsurancePolicyDAOImpl(this.pisdR350);
        Map<String, Object> responseValidateQuotation = insurancePolicy.executeValidateQuotation(payloadStore.getInput().getId());

        LOGGER.info("***** QuotationStore - getQuotationIdFromDB | responseValidateQuotation: {} *****",responseValidateQuotation);

        return (BigDecimal) responseValidateQuotation.get(RBVDProperties.FIELD_RESULT_NUMBER.getValue());
    }

    private void save(PayloadStore payloadStore, BigDecimal resultCount){

        LOGGER.info("***** QuotationStore - SaveQuotation START - arguments: payloadStore {} *****",payloadStore);

        InsuranceQuotationModDAOImpl insuranceQuotationMod = new InsuranceQuotationModDAOImpl(pisdR350);

        LOGGER.info("***** QuotationStore - SaveQuotation - insuranceQuotationMod {} *****",insuranceQuotationMod);

        if(BigDecimal.ONE.compareTo(resultCount) == 0) {
            LOGGER.info("***** QuotationStore - SaveQuotation - argumentsForUpdateQuotationMod 1 {} *****",payloadStore.getMyQuotation());
            LOGGER.info("***** QuotationStore - SaveQuotation - argumentsForUpdateQuotationMod 2 {} *****",payloadStore.getInput());
            insuranceQuotationMod.executeUpdateQuotationModQuery(payloadStore.getMyQuotation(), payloadStore.getInput());
        } else {
            InsuranceQuotationDAOImpl insuranceQuotation = new InsuranceQuotationDAOImpl(pisdR350);
            LOGGER.info("***** QuotationStore - SaveQuotation - argumentsForInsertQuotation {} *****",payloadStore);
            insuranceQuotation.executeInsertQuotationQuery(payloadStore);
            LOGGER.info("***** QuotationStore - SaveQuotation - argumentsForInsertQuotationMod {} *****",payloadStore);
            insuranceQuotationMod.executeInsertQuotationModQuery(payloadStore);
        }
    }
}

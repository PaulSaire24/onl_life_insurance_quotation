package com.bbva.rbvd.lib.r304.pattern.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.dao.InsuredLifeDAO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.pattern.PostQuotation;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceQuotationDAO;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsurancePolicyDAOImpl;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceQuotationDAOImpl;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceQuotationModDAOImpl;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import com.bbva.rbvd.lib.r304.transform.bean.InsuranceQuotationInsuredBean;
import com.bbva.rbvd.lib.r304.transform.map.QuotationParticipantMap;
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
    public void end(PayloadStore payloadStore,ApplicationConfigurationService applicationConfigurationService) {
        BigDecimal resultCount = this.getQuotationIdFromDB(payloadStore);

        this.save(payloadStore, resultCount,applicationConfigurationService);
    }


    private BigDecimal getQuotationIdFromDB(PayloadStore payloadStore){

        LOGGER.info("***** QuotationStore - getQuotationIdFromDB START *****");
        LOGGER.info("***** QuotationStore - getQuotationIdFromDB START, parameter payloadStore: {} *****", payloadStore);

        InsurancePolicyDAOImpl insurancePolicy = new InsurancePolicyDAOImpl(this.pisdR350);
        Map<String, Object> responseValidateQuotation = insurancePolicy.executeValidateQuotation(payloadStore.getInput().getId());

        LOGGER.info("***** QuotationStore - getQuotationIdFromDB | responseValidateQuotation: {} *****",responseValidateQuotation);

        return (BigDecimal) responseValidateQuotation.get(RBVDProperties.FIELD_RESULT_NUMBER.getValue());
    }

    private void save(PayloadStore payloadStore, BigDecimal resultCount,ApplicationConfigurationService applicationConfigurationService){
        InsuranceQuotationModDAOImpl insuranceQuotationMod = new InsuranceQuotationModDAOImpl(pisdR350);

        LOGGER.info("***** QuotationStore - SaveQuotation START - arguments: payloadStore {} *****",payloadStore);

        InsuranceQuotationDAOImpl insuranceQuotation = new InsuranceQuotationDAOImpl(pisdR350);
        InsuredLifeDAO quotationInsured = InsuranceQuotationInsuredBean.createQuotationParticipant(payloadStore,applicationConfigurationService);
        LOGGER.info("***** QuotationStore - saveParticipantInformation - QuotationParticipantDAO {} *****",quotationInsured);
        Map<String, Object> argumentForSaveParticipant = QuotationParticipantMap.createArgumentsForSaveParticipant(quotationInsured);
        Map<String, Object> argumentForUpdateParticipant = QuotationParticipantMap.createArgumentsForUpdateParticipant(quotationInsured);

        IInsuranceQuotationDAO insuranceSimulationDao= new InsuranceQuotationDAOImpl(pisdR350);

        if(BigDecimal.ONE.compareTo(resultCount) == 0) {
            LOGGER.info("***** QuotationStore - SaveQuotation - argumentsForUpdateQuotationMod 1 {} *****",payloadStore.getMyQuotation());
            insuranceQuotationMod.executeUpdateQuotationModQuery(payloadStore.getMyQuotation(), payloadStore.getInput());
            LOGGER.info("***** QuotationStore - SaveQuotation - argumentForUpdateParticipant  {} *****",argumentForUpdateParticipant);
            insuranceQuotation.updateQuotationInsured(argumentForUpdateParticipant);
            ;
         } else {
            LOGGER.info("***** QuotationStore - SaveQuotation - argumentsForInsertQuotation payloadstore {} *****", payloadStore);
            insuranceQuotation.executeInsertQuotationQuery(payloadStore);
            insuranceQuotationMod.executeInsertQuotationModQuery(payloadStore);
            insuranceSimulationDao.insertQuotationInsured(argumentForSaveParticipant);
        }
    }

}

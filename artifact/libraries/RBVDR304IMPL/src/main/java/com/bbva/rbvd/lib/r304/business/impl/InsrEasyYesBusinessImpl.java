package com.bbva.rbvd.lib.r304.business.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.business.IInsrEasyYesBusiness;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import com.bbva.rbvd.lib.r304.transform.bean.QuotationRimacBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InsrEasyYesBusinessImpl implements IInsrEasyYesBusiness {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsrEasyYesBusinessImpl.class);
    private final RBVDR303 rbvdR303;
    private final ApplicationConfigurationService applicationConfigurationService;

    public InsrEasyYesBusinessImpl(RBVDR303 rbvdR303, ApplicationConfigurationService applicationConfigurationService) {
        this.rbvdR303 = rbvdR303;
        this.applicationConfigurationService = applicationConfigurationService;
    }

    @Override
    public PayloadStore doEasyYes(PayloadConfig payloadConfig) {
        LOGGER.info("***** InsrEasyYesBusinessImpl - doEasyYes | argument payloadConfig: {} *****",payloadConfig);

        EasyesQuotationBO responseRimar;
    }

    private EasyesQuotationBO callQuotationRimacService(EasyesQuotationDAO input, String policyQuotaInternalId){

        LOGGER.info("***** InsrEasyYesBusinessImpl - callQuotationRimacService START *****");
        EasyesQuotationBO requestRimac = QuotationRimacBean.createRimacQuotationRequest(input,policyQuotaInternalId);
        //requestRimac.getPayload().

        LOGGER.info("***** InsrEasyYesBusinessImpl - callQuotationRimacService | requestRimac: {} *****",requestRimac);

        EasyesQuotationBO responseRimac =

        return null;
    }
}

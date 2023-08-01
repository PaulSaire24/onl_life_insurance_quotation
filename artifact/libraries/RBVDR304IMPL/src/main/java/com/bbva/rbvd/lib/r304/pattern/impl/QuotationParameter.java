package com.bbva.rbvd.lib.r304.pattern.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.pattern.PreQuotation;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuotationParameter implements PreQuotation {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuotationParameter.class);

    private final PISDR350 pisdR350;

    private final RBVDR303 rbvdR303;

    private final ApplicationConfigurationService applicationConfigurationService;

    public QuotationParameter(PISDR350 pisdR350,RBVDR303 rbvdR303, ApplicationConfigurationService applicationConfigurationService) {
        this.applicationConfigurationService = applicationConfigurationService;
        this.pisdR350 = pisdR350;
        this.rbvdR303 = rbvdR303;
    }

    @Override
    public PayloadConfig getConfig(EasyesQuotationDTO input) {
        LOGGER.info("***** QuotationParameter getConfig START *****");
        LOGGER.info("***** QuotationParameter getConfig - input : {} *****",input);
        PayloadConfig payloadConfig = new PayloadConfig();

        LOGGER.info("***** QuotationParameter getConfig - END  payloadConfig: {} *****",payloadConfig);
        return payloadConfig;
    }

}

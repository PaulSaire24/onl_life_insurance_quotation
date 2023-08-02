package com.bbva.rbvd.lib.r304.pattern.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.lib.r304.RBVDR304;
import com.bbva.rbvd.lib.r304.pattern.PostQuotation;
import com.bbva.rbvd.lib.r304.pattern.PreQuotation;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuotationEasyYes extends QuotationDecorator{

    private static final Logger LOGGER = LoggerFactory.getLogger(QuotationEasyYes.class);

    public QuotationEasyYes(PreQuotation preQuotation, PostQuotation postQuotation) {
        super(preQuotation, postQuotation);
    }

    @Override
    public EasyesQuotationDTO start(EasyesQuotationDTO input, RBVDR304 rbvdr304, ApplicationConfigurationService applicationConfigurationService) {
        LOGGER.info("***** QuotationEasyYes - start - START *****");
        LOGGER.info("***** QuotationEasyYes - start - input : {} *****",input);

        PayloadConfig payloadConfig = this.getPreQuotation().getConfig(input);

    }
}


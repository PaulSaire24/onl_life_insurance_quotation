package com.bbva.rbvd.lib.r304.pattern.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.RBVDR304;
import com.bbva.rbvd.lib.r304.business.impl.InsrVidaDinamicoBusinessImpl;
import com.bbva.rbvd.lib.r304.pattern.PostQuotation;
import com.bbva.rbvd.lib.r304.pattern.PreQuotation;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuotationVidaDinamico extends QuotationDecorator{

    private static final Logger LOGGER = LoggerFactory.getLogger(QuotationVidaDinamico.class);

    public QuotationVidaDinamico(PreQuotation preQuotation, PostQuotation postQuotation) {
        super(preQuotation, postQuotation );
    }

    @Override
    public EasyesQuotationDTO start(EasyesQuotationDTO input, RBVDR303 rbvdr303) {
        return null;
    }
}

package com.bbva.rbvd.lib.r304.pattern.product;


import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.business.IInsrEasyYesBusiness;
import com.bbva.rbvd.lib.r304.business.impl.InsrEasyYesBusinessImpl;
import com.bbva.rbvd.lib.r304.pattern.PostQuotation;
import com.bbva.rbvd.lib.r304.pattern.PreQuotation;
import com.bbva.rbvd.lib.r304.pattern.impl.QuotationDecorator;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuotationEasyYes extends QuotationDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuotationEasyYes.class);

    public QuotationEasyYes(PreQuotation preQuotation, PostQuotation postQuotation) {
        super(preQuotation, postQuotation);
    }

    @Override
    public QuotationLifeDTO start(QuotationLifeDTO input, RBVDR303 rbvdr303, ApplicationConfigurationService applicationConfigurationService) {
        LOGGER.info("***** QuotationEasyYes - start - START *****");
        LOGGER.info("***** QuotationEasyYes - start - input : {} *****",input);

        PayloadConfig payloadConfig = this.getPreQuotation().getConfig(input);
        IInsrEasyYesBusiness seguroEasyYes = new InsrEasyYesBusinessImpl(rbvdr303);

        PayloadStore payloadStore = seguroEasyYes.doEasyYes(payloadConfig);

        this.getPostQuotation().end(payloadStore,applicationConfigurationService);

        QuotationLifeDTO response = seguroEasyYes.mappingOutputFieldsEasyes(payloadStore);
        LOGGER.info("***** QuotationEasyYes - response : {} *****",response);

        return response;
    }
}


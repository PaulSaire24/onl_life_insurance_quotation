package com.bbva.rbvd.lib.r304.pattern.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.RBVDR304;
import com.bbva.rbvd.lib.r304.business.IInsrEasyYesBusiness;
import com.bbva.rbvd.lib.r304.business.impl.InsrEasyYesBusinessImpl;
import com.bbva.rbvd.lib.r304.pattern.PostQuotation;
import com.bbva.rbvd.lib.r304.pattern.PreQuotation;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuotationEasyYes extends QuotationDecorator{

    private static final Logger LOGGER = LoggerFactory.getLogger(QuotationEasyYes.class);

    public QuotationEasyYes(PreQuotation preQuotation, PostQuotation postQuotation) {
        super(preQuotation, postQuotation);
    }

    @Override
    public EasyesQuotationDTO start(EasyesQuotationDTO input, RBVDR303 rbvdr303) {
        LOGGER.info("***** QuotationEasyYes - start - START *****");
        LOGGER.info("***** QuotationEasyYes - start - input : {} *****",input);

        //llamar a la configuracion previa: getConfig
        PayloadConfig payloadConfig = this.getPreQuotation().getConfig(input);
        IInsrEasyYesBusiness seguroEasyYes = new InsrEasyYesBusinessImpl(rbvdr303);

        //llamar al servicio de rimac
        PayloadStore payloadStore = seguroEasyYes.doEasyYes(payloadConfig);

        //guardar en la bd
        this.getPostQuotation().end(payloadStore);

        //respuesta de trx
        EasyesQuotationDTO response = seguroEasyYes.mappingOutputFields(payloadStore);
        LOGGER.info("***** QuotationEasyYes - response : {} *****",response);

        //retornar la rspuesta
        return response;
    }
}


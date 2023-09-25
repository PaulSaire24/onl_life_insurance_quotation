package com.bbva.rbvd.lib.r304.pattern.product;


import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.business.IInsrDynamicLifeBusiness;
import com.bbva.rbvd.lib.r304.business.impl.InsrVidaDinamicoBusinessImpl;
import com.bbva.rbvd.lib.r304.pattern.PostQuotation;
import com.bbva.rbvd.lib.r304.pattern.PreQuotation;
import com.bbva.rbvd.lib.r304.pattern.impl.QuotationDecorator;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuotationVidaDinamico extends QuotationDecorator {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuotationVidaDinamico.class);

    public QuotationVidaDinamico(PreQuotation preQuotation, PostQuotation postQuotation) {
        super(preQuotation, postQuotation );
    }

    @Override
    public QuotationLifeDTO start(QuotationLifeDTO input, RBVDR303 rbvdr303) {
        LOGGER.info("***** QuotationVidaDinamico - start - START *****");
        LOGGER.info("***** QuotationVidaDinamico - start - input : {} *****",input);

        PayloadConfig payloadConfig = this.getPreQuotation().getConfig(input);
        IInsrDynamicLifeBusiness seguroVidaDinamico = new InsrVidaDinamicoBusinessImpl(rbvdr303);

        PayloadStore payloadStore = seguroVidaDinamico.doDynamicLife(payloadConfig);

        this.getPostQuotation().end(payloadStore);

        QuotationLifeDTO response = seguroVidaDinamico.mappingOutputFieldsDynamic(payloadStore);
        LOGGER.info("***** QuotationVidaDinamico - response : {} *****",response);

        return response;
    }
}

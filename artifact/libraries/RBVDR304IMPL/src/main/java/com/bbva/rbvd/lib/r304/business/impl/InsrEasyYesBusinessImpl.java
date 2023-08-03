package com.bbva.rbvd.lib.r304.business.impl;

import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDValidation;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.business.IInsrEasyYesBusiness;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import com.bbva.rbvd.lib.r304.transform.bean.QuotationRimacBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.isNull;

public class InsrEasyYesBusinessImpl implements IInsrEasyYesBusiness {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsrEasyYesBusinessImpl.class);
    private final RBVDR303 rbvdR303;

    public InsrEasyYesBusinessImpl(RBVDR303 rbvdR303) {
        this.rbvdR303 = rbvdR303;
    }

    @Override
    public PayloadStore doEasyYes(PayloadConfig payloadConfig) {
        LOGGER.info("***** InsrEasyYesBusinessImpl - doEasyYes | argument payloadConfig: {} *****",payloadConfig);

        EasyesQuotationBO responseRimar;
        return null;
    }

    private EasyesQuotationBO callQuotationRimacService(EasyesQuotationDTO input, String policyQuotaInternalId, PayloadStore payloadStore){

        LOGGER.info("***** InsrEasyYesBusinessImpl - callQuotationRimacService START *****");
        EasyesQuotationBO requestRimac = QuotationRimacBean.createRimacQuotationRequest(payloadStore.getPayloadConfig().getQuotationDao(), policyQuotaInternalId);

        LOGGER.info("***** InsrEasyYesBusinessImpl - callQuotationRimacService | requestRimac: {} *****",requestRimac);

        EasyesQuotationBO responseRimac = this.rbvdR303.executeEasyesQuotationRimac(requestRimac, input.getExternalSimulationId(),input.getTraceId());
        if (isNull(responseRimac)){
            throw RBVDValidation.build(RBVDErrors.COULDNT_SELECT_MODALITY_RIMAC_ERROR);
        }

        return responseRimac;
    }
}

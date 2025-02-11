package com.bbva.rbvd.lib.r304.business.impl;

import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.QuotationLifeBO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDValidation;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.business.IInsrDynamicLifeBusiness;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import com.bbva.rbvd.lib.r304.transform.bean.QuotationRimacBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class InsrVidaDinamicoBusinessImpl implements IInsrDynamicLifeBusiness {

    private static final Logger LOGGER = LoggerFactory.getLogger(InsrVidaDinamicoBusinessImpl.class);

    private final RBVDR303 rbvdR303;

    public InsrVidaDinamicoBusinessImpl(RBVDR303 rbvdR303) {
        this.rbvdR303 = rbvdR303;
    }

    @Override
    public PayloadStore doDynamicLife(PayloadConfig payloadConfig) {
        LOGGER.info("***** InsrVidaDinamicoBusinessImpl - doDynamicLife | argument payloadConfig: {} *****", payloadConfig);

        QuotationLifeBO responseRimac = this.callQuotationRimacService(payloadConfig);

        PayloadStore payloadStoreDynamic = new PayloadStore();
        payloadStoreDynamic.setCustomerInformation(payloadConfig.getCustomerInformation());
        payloadStoreDynamic.setRimacResponse(responseRimac);
        payloadStoreDynamic.setMyQuotation(payloadConfig.getMyQuotation());
        payloadStoreDynamic.setInput(payloadConfig.getInput());
        payloadStoreDynamic.setFrequencyType(payloadConfig.getPayloadProperties().getFrequencyTypeId());

        return payloadStoreDynamic;
    }

    @Override
    public QuotationLifeDTO mappingOutputFieldsDynamic(PayloadStore payloadStore) {

        LOGGER.info("***** InsrVidaDinamicoBusinessImpl - callQuotationRimacService START *****");

        QuotationLifeDTO response = payloadStore.getInput();

        CustomerListASO customerInformation = this.rbvdR303.executeGetCustomerHost(payloadStore.getInput().getHolder().getId());

        this.fillHolderData(customerInformation,response);
        this.fillDataProduct(response, payloadStore);

        return response;
    }

    private QuotationLifeBO callQuotationRimacService(PayloadConfig payload) {

        LOGGER.info("***** InsrVidaDinamicoBusinessImpl - callQuotationRimacService START *****");
        QuotationLifeBO requestRimac = QuotationRimacBean.createRimacQuotationRequest(payload.getMyQuotation(), payload.getPolicyQuotaId());

        LOGGER.info("***** InsrVidaDinamicoBusinessImpl - callQuotationRimacService | requestRimac: {} *****", requestRimac);

        QuotationLifeBO responseRimac = this.rbvdR303.executeQuotationRimac(requestRimac, payload.getInput().getExternalSimulationId(), payload.getInput().getTraceId());

        if (isNull(responseRimac)) {
            throw RBVDValidation.build(RBVDErrors.COULDNT_SELECT_MODALITY_RIMAC_ERROR);
        }
        LOGGER.info("***** InsrVidaDinamicoBusinessImpl - callQuotationRimacService | responseRimac: {} *****", responseRimac);

        return responseRimac;
    }

    private void fillHolderData (CustomerListASO customerInformation,QuotationLifeDTO response){

        LOGGER.info("***** InsrVidaDinamicoBusinessImpl - fillHolderData START *****");
        final String defaultValue = "";

        if (nonNull(customerInformation)) {
            response.getHolder().setFirstName(customerInformation.getData().get(0).getFirstName());
            response.getHolder().setLastName(customerInformation.getData().get(0).getLastName());
            final String fullName = customerInformation.getData().get(0).getFirstName().concat(" ").
                    concat(customerInformation.getData().get(0).getLastName()).concat(" ").concat(customerInformation.getData().get(0).getSecondLastName()!= null? customerInformation.getData().get(0).getSecondLastName() : "");
            response.getHolder().setFullName(fullName);

        } else {
            response.getHolder().setFirstName(defaultValue);
            response.getHolder().setLastName(defaultValue);
            response.getHolder().setFullName(defaultValue);
        }

        LOGGER.info("***** InsrVidaDinamicoBusinessImpl - fillHolderData | response.holder: {} *****", response.getHolder());
    }

    private void fillDataProduct(QuotationLifeDTO response, PayloadStore payloadStore){

        LOGGER.info("***** InsrVidaDinamicoBusinessImpl - fillHolderData START *****");

        response.getProduct().setName(payloadStore.getMyQuotation().getInsuranceProductDescription());
        response.getProduct().getPlans().get(0).setName(payloadStore.getMyQuotation().getInsuranceModalityName());
        response.getProduct().getPlans().get(0).getInstallmentPlans().get(0).getPeriod()
                .setName(payloadStore.getMyQuotation().getPaymentFrequencyName());

        LOGGER.info("***** InsrVidaDinamicoBusinessImpl - fillHolderData | response.product: {} *****", response.getProduct());
    }
}

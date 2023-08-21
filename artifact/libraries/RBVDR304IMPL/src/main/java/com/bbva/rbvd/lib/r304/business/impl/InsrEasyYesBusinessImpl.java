package com.bbva.rbvd.lib.r304.business.impl;

import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.pisd.dto.insurance.commons.HolderDTO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDValidation;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.business.IInsrEasyYesBusiness;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import com.bbva.rbvd.lib.r304.transform.bean.QuotationRimacBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class InsrEasyYesBusinessImpl implements IInsrEasyYesBusiness {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsrEasyYesBusinessImpl.class);
    private final RBVDR303 rbvdR303;

    public InsrEasyYesBusinessImpl(RBVDR303 rbvdR303) {
        this.rbvdR303 = rbvdR303;
    }

    @Override
    public PayloadStore doEasyYes(PayloadConfig payloadConfig) {
        LOGGER.info("***** InsrEasyYesBusinessImpl - doEasyYes | argument payloadConfig: {} *****",payloadConfig);

        EasyesQuotationBO responseRimac = this.callQuotationRimacService(payloadConfig);

        PayloadStore payloadStoreEasyes = new PayloadStore();
        payloadStoreEasyes.setRimacResponse(responseRimac);
        payloadStoreEasyes.setMyQuotation(payloadConfig.getMyQuotation());
        payloadStoreEasyes.setInput(payloadConfig.getInput());
        payloadStoreEasyes.setFrequencyType(payloadConfig.getPayloadProperties().getFrequencyTypeId());

        return payloadStoreEasyes;
    }

    @Override
    public EasyesQuotationDTO mappingOutputFieldsEasyes(PayloadStore payloadStore) {
        EasyesQuotationDTO response = payloadStore.getInput();

        CustomerBO customerInformation = this.rbvdR303.executeListCustomerService(payloadStore.getInput().getHolder().getId());

        this.fillHolderData(customerInformation);
        this.fillDataProduct(response, payloadStore);

        return response;
    }

    private EasyesQuotationBO callQuotationRimacService(PayloadConfig payload){

        LOGGER.info("***** InsrEasyYesBusinessImpl - callQuotationRimacService START *****");
        EasyesQuotationBO requestRimac = QuotationRimacBean.createRimacQuotationRequest(payload.getMyQuotation(),payload.getPolicyQuotaId());

        LOGGER.info("***** InsrEasyYesBusinessImpl - callQuotationRimacService | requestRimac: {} *****",requestRimac);

        EasyesQuotationBO responseRimac = this.rbvdR303.executeQuotationRimac(requestRimac,payload.getInput().getExternalSimulationId(),payload.getInput().getTraceId());

        if (isNull(responseRimac)){
            throw RBVDValidation.build(RBVDErrors.COULDNT_SELECT_MODALITY_RIMAC_ERROR);
        }

        return responseRimac;
    }

    private void fillHolderData (CustomerBO customerInformation){

        LOGGER.info("***** InsrVidaDinamicoBusinessImpl - fillHolderData START *****");

        final String defaultValue = "";
        HolderDTO holderDTO = new HolderDTO();

        if (nonNull(customerInformation)) {
            holderDTO.setFirstName(customerInformation.getFirstName());
            holderDTO.setLastName(customerInformation.getLastName());
            final String fullName = customerInformation.getFirstName().concat(" ").
                    concat(customerInformation.getLastName()).concat(" ").concat(customerInformation.getSecondLastName() != null? customerInformation.getSecondLastName() : "");
            holderDTO.setFullName(fullName);

            LOGGER.info("***** InsrVidaDinamicoBusinessImpl - fillHolderData | holderDTO: {} *****", holderDTO);

        } else {
            holderDTO.setFirstName(defaultValue);
            holderDTO.setLastName(defaultValue);
            holderDTO.setFullName(defaultValue);

            LOGGER.info("***** InsrVidaDinamicoBusinessImpl - fillHolderData | holderDTO: {} *****", holderDTO);
        }
    }

    private void fillDataProduct(EasyesQuotationDTO response, PayloadStore payloadStore){

        LOGGER.info("***** InsrVidaDinamicoBusinessImpl - fillHolderData START *****");

        response.getProduct().setName(payloadStore.getMyQuotation().getInsuranceProductDescription());
        response.getProduct().getPlans().get(0).setName(payloadStore.getMyQuotation().getInsuranceModalityName());
        response.getProduct().getPlans().get(0).getInstallmentPlans().get(0).getPeriod()
                .setName(payloadStore.getMyQuotation().getPaymentFrequencyName());

        LOGGER.info("***** InsrVidaDinamicoBusinessImpl - fillHolderData | response: {} *****", response);
    }
}

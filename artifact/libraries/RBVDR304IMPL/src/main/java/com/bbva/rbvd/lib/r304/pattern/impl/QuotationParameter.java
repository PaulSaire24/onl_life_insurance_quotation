package com.bbva.rbvd.lib.r304.pattern.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.commons.InsurancePlanDTO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.lib.r304.pattern.PreQuotation;
import com.bbva.rbvd.lib.r304.service.dao.IInsurancePaymentPeriodDAO;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceProductModalityDAO;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceSimulationDAO;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsurancePaymentPeriodDAO;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceProductModalityDAO;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceSimulationDAO;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadProperties;
import com.bbva.rbvd.lib.r304.transform.bean.QuotationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class QuotationParameter implements PreQuotation {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuotationParameter.class);

    private final PISDR350 pisdR350;

    private final ApplicationConfigurationService applicationConfigurationService;

    public QuotationParameter(PISDR350 pisdR350, ApplicationConfigurationService applicationConfigurationService) {
        this.applicationConfigurationService = applicationConfigurationService;
        this.pisdR350 = pisdR350;
    }

    @Override
    public PayloadConfig getConfig(EasyesQuotationDTO input) {
        LOGGER.info("***** QuotationParameter getConfig START *****");
        LOGGER.info("***** QuotationParameter getConfig - input : {} *****",input);

        PayloadConfig payloadConfig = new PayloadConfig();
        payloadConfig.setQuotation(input);

        PayloadProperties properties = this.getProperties(payloadConfig.getQuotation());

        Map<String, Object> simulation = this.getSimulacion(input.getExternalSimulationId());
        Map<String, Object> product = this.getProduct(properties.getProductType(), properties.getSelectedPlanId());
        Map<String, Object> paymentFrequency = this.getPaymentFrequency(properties.getFrequencyTypeId());

        EasyesQuotationDAO quotation = this.getQuotationDao(simulation,product,paymentFrequency);
        String policyQuotaid = this.getGeneratePolicyQuotaid(quotation);

        payloadConfig.getQuotation().setId(policyQuotaid);

        payloadConfig.setEasyesQuotationDao(quotation);
        payloadConfig.setPolicyQuotaid(policyQuotaid);
        payloadConfig.setPayloadProperties(properties);


        LOGGER.info("***** QuotationParameter getConfig - END  payloadConfig: {} *****",payloadConfig);

        return payloadConfig;
    }

    public EasyesQuotationDAO getQuotationDao(Map<String, Object> responseGetSimulationIdAndExpirationDate,
                                              Map<String, Object> responseGetRequiredInformation,
                                              Map<String, Object> responseGetPaymentFrequencyName)
    {
        LOGGER.info("***** QuotationParameter getEasYesQuotationDao START - responseGetSimulationIdAndExpirationDate: {} *****",responseGetSimulationIdAndExpirationDate);
        LOGGER.info("***** QuotationParameter getEasYesQuotationDao START - responseGetRequiredInformation: {} *****",responseGetRequiredInformation);
        LOGGER.info("***** QuotationParameter getEasYesQuotationDao START - responseGetPaymentFrequencyName: {} *****",responseGetPaymentFrequencyName);

        QuotationBean quotationBean = new QuotationBean();
        EasyesQuotationDAO quotation = quotationBean.createQuotationDao(responseGetSimulationIdAndExpirationDate, responseGetRequiredInformation, responseGetPaymentFrequencyName);

        LOGGER.info("***** QuotationParameter getEasYesQuotationDao END - EasyesQuotation: {} *****",quotation);

        return  quotation;
    }

    public String getGeneratePolicyQuotaid(EasyesQuotationDAO easyesQuotationDao){

        final int requiredSize = 9;

        final StringBuilder policyQuotaInternalId = new StringBuilder("0814");
        final int sizeToComplete = requiredSize - easyesQuotationDao.getInsuranceSimulationId().toString().length();

        for(int i = 0; i < sizeToComplete; i++) {
            policyQuotaInternalId.append("0");
        }
        policyQuotaInternalId.append(easyesQuotationDao.getInsuranceSimulationId());

        return policyQuotaInternalId.toString();
    }

    public PayloadProperties getProperties(EasyesQuotationDTO input){

        LOGGER.info("***** QuotationParameter getProperties START *****");

        PayloadProperties properties = new PayloadProperties();
        InsurancePlanDTO selectedPlan = input.getProduct().getPlans().get(0);

        properties.setProductType(input.getProduct().getId());
        properties.setSelectedPlanId(selectedPlan.getId());
        properties.setPeriodId(selectedPlan.getInstallmentPlans().get(0).getId());
        properties.setFrequencyTypeId(this.applicationConfigurationService.getProperty(properties.getPeriodId()));

        return  properties;
    }

    private Map<String, Object> getSimulacion(String externalSimulationId){

        LOGGER.info("***** QuotationParameter getSimnulacion START - insuranceProductId: {} *****",externalSimulationId);

        IInsuranceSimulationDAO simulationDAO =new InsuranceSimulationDAO(this.pisdR350);
        Map<String, Object>simulation = simulationDAO.executeGetSimulationInformation(externalSimulationId);

        LOGGER.info("***** QuotationParameter getSimnulacion END - simulation: {} *****",simulation);
        return simulation;
    }

    private Map<String, Object> getProduct(String productType, String planId){

        LOGGER.info("***** QuotationParameter getProduct START - productType: {} *****",productType);
        LOGGER.info("***** QuotationParameter getProduct START - planId: {} *****",planId);

        IInsuranceProductModalityDAO productDAO = new InsuranceProductModalityDAO(this.pisdR350);
        Map<String, Object> product = productDAO.executeGetRequiredInformation(productType,planId);

        LOGGER.info("***** QuotationParameter getProduct END - product: {} *****",product);

        return product;
    }

    private Map<String, Object> getPaymentFrequency(String frequencyTypeId){

        LOGGER.info("***** QuotationParameter getPaymentFrequency START - frequencyTypeId: {} *****",frequencyTypeId);

        IInsurancePaymentPeriodDAO paymentPeriodDAO = new InsurancePaymentPeriodDAO(this.pisdR350);
        Map<String, Object> paymentFrequency = paymentPeriodDAO.executeGetPaymentFrequencyName(frequencyTypeId);

        LOGGER.info("***** QuotationParameter getPaymentFrequency END - paymentFrequency: {} *****",paymentFrequency);

        return paymentFrequency;
    }
}

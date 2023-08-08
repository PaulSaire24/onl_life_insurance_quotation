package com.bbva.rbvd.lib.r304.pattern.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.commons.InsurancePlanDTO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.lib.r304.pattern.PreQuotation;
import com.bbva.rbvd.lib.r304.service.api.CustomerService;
import com.bbva.rbvd.lib.r304.service.dao.IInsurancePaymentPeriodDAO;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceProductModalityDAO;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceSimulationDAO;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsurancePaymentPeriodDAOImpl;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceProductModalityDAOImpl;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceSimulationDAOImpl;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadProperties;
import com.bbva.rbvd.lib.r304.transform.bean.QuotationBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static java.util.Objects.nonNull;

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
        //payloadConfig.setInput(input);

        PayloadProperties properties = this.getProperties(input);

        payloadConfig.setPayloadProperties(properties);

        Map<String, Object> simulation = this.getSimulacion(input.getExternalSimulationId());
        Map<String, Object> product = this.getProduct(input.getProduct().getId(),input.getProduct().getPlans().get(0).getId());
        Map<String, Object> paymentFrequency = this.getPaymentFrequency(properties.getFrequencyTypeId());

        EasyesQuotationDAO myQuotation = QuotationBean.createQuotationDao(simulation, product, paymentFrequency);

        String policyQuotaid = this.getGeneratePolicyQuotaid(myQuotation);

        //EasyesQuotationDTO customer = this.mappingOutputFields(input,myQuotation);
        input.setId(policyQuotaid);

        payloadConfig.setMyQuotation(myQuotation);
        payloadConfig.setPolicyQuotaId(policyQuotaid);
        payloadConfig.setInput(input);

        LOGGER.info("***** QuotationParameter getConfig - END  payloadConfig: {} *****",payloadConfig);

        return payloadConfig;
    }

    public String getGeneratePolicyQuotaid(EasyesQuotationDAO myQuotation){

        int requiredSize = 9;

        StringBuilder policyQuotaInternalId = new StringBuilder("0814");
        int sizeToComplete = requiredSize - myQuotation.getInsuranceSimulationId().toString().length();

        for(int i = 0; i < sizeToComplete; i++) {
            policyQuotaInternalId.append("0");
        }
        policyQuotaInternalId.append(myQuotation.getInsuranceSimulationId());

        return policyQuotaInternalId.toString();
    }

    public PayloadProperties getProperties(EasyesQuotationDTO input){

        LOGGER.info("***** QuotationParameter getProperties START *****");

        PayloadProperties properties = new PayloadProperties();
        InsurancePlanDTO selectedPlan = input.getProduct().getPlans().get(0);
        String periodId = selectedPlan.getInstallmentPlans().get(0).getPeriod().getId();

        properties.setProductType(input.getProduct().getId());
        properties.setSelectedPlanId(selectedPlan.getId());
        properties.setPeriodId(periodId);
        properties.setFrequencyTypeId(this.applicationConfigurationService.getProperty(periodId));

        return properties;
    }

    private Map<String, Object> getSimulacion(String externalSimulationId){

        LOGGER.info("***** QuotationParameter getSimnulacion START - insuranceProductId: {} *****",externalSimulationId);

        IInsuranceSimulationDAO simulationDAO = new InsuranceSimulationDAOImpl(this.pisdR350);
        Map<String, Object> simulation = simulationDAO.executeGetSimulationInformation(externalSimulationId);

        LOGGER.info("***** QuotationParameter getSimnulacion END - simulation: {} *****",simulation);
        return simulation;
    }

    private Map<String, Object> getProduct(String productType, String planId){

        LOGGER.info("***** QuotationParameter getProduct START - productType: {} *****",productType);
        LOGGER.info("***** QuotationParameter getProduct START - planId: {} *****",planId);

        IInsuranceProductModalityDAO productDAO = new InsuranceProductModalityDAOImpl(this.pisdR350);
        Map<String, Object> product = productDAO.executeGetRequiredInformation(productType,planId);

        LOGGER.info("***** QuotationParameter getProduct END - product: {} *****",product);

        return product;
    }

    private Map<String, Object> getPaymentFrequency(String frequencyTypeId){

        LOGGER.info("***** QuotationParameter getPaymentFrequency START - frequencyTypeId: {} *****",frequencyTypeId);

        IInsurancePaymentPeriodDAO paymentPeriodDAO = new InsurancePaymentPeriodDAOImpl(this.pisdR350);
        Map<String, Object> paymentFrequency = paymentPeriodDAO.executeGetPaymentFrequencyName(frequencyTypeId);

        LOGGER.info("***** QuotationParameter getPaymentFrequency END - paymentFrequency: {} *****",paymentFrequency);

        return paymentFrequency;
    }

    /*private EasyesQuotationDTO mappingOutputFields(EasyesQuotationDTO input, EasyesQuotationDAO myQuotation) {

        final String defaultValue = "";

        CustomerBO customerInformation = CustomerService.listCustomerService(input.getHolder().getId());

        if(nonNull(customerInformation)) {
            input.getHolder().setFirstName(customerInformation.getFirstName());
            input.getHolder().setLastName(customerInformation.getLastName());
            final String fullName = customerInformation.getFirstName().concat(" ").
                    concat(customerInformation.getLastName()).concat(" ").concat(customerInformation.getSecondLastName());
            input.getHolder().setFullName(fullName);
        } else {
            input.getHolder().setFirstName(defaultValue);
            input.getHolder().setLastName(defaultValue);
            input.getHolder().setFullName(defaultValue);
        }

        input.getProduct().getPlans().get(0).getInstallmentPlans().get(0).getPeriod()
                .setName(myQuotation.getPaymentFrequencyName());

        return input;
    }*/
}

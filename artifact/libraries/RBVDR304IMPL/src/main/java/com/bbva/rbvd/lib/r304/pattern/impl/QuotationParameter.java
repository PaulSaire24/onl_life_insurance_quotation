package com.bbva.rbvd.lib.r304.pattern.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.commons.InsurancePlanDTO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.pattern.PreQuotation;
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

import java.util.Collections;
import java.util.Map;

public class QuotationParameter implements PreQuotation {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuotationParameter.class);

    private final PISDR350 pisdR350;
    private final RBVDR303 rbvdr303;

    private final ApplicationConfigurationService applicationConfigurationService;

    public QuotationParameter(PISDR350 pisdR350, ApplicationConfigurationService applicationConfigurationService,RBVDR303 rbvdr303) {
        this.applicationConfigurationService = applicationConfigurationService;
        this.pisdR350 = pisdR350;
        this.rbvdr303=rbvdr303;
    }

    @Override
    public PayloadConfig getConfig(QuotationLifeDTO input) {
        LOGGER.info("***** QuotationParameter getConfig START *****");
        LOGGER.info("***** QuotationParameter getConfig - input : {} *****",input);

        PayloadConfig payloadConfig = new PayloadConfig();

        PayloadProperties properties = this.getProperties(input);

        payloadConfig.setPayloadProperties(properties);

        Map<String, Object> simulation = this.getSimulacion(input.getExternalSimulationId());
        Map<String, Object> product = this.getProduct(input.getProduct().getId(),input.getProduct().getPlans().get(0).getId());
        Map<String, Object> paymentFrequency = this.getPaymentFrequency(properties.getFrequencyTypeId());

        EasyesQuotationDAO myQuotation = QuotationBean.createQuotationDao(simulation, product, paymentFrequency);
        CustomerListASO customerResponse = this.getCustomer(input.getHolder().getId());
        LOGGER.info("***** QuotationParameter: customerResponse {} *****",customerResponse);
        String policyQuotaid = this.getGeneratePolicyQuotaid(myQuotation);

        input.setId(policyQuotaid);
        payloadConfig.setCustomerInformation(getCustomer(input.getHolder().getId()));
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

    public PayloadProperties getProperties(QuotationLifeDTO input){

        LOGGER.info("***** QuotationParameter getProperties START *****");

        PayloadProperties properties = new PayloadProperties();
        InsurancePlanDTO selectedPlan = input.getProduct().getPlans().get(0);
        String periodId = selectedPlan.getInstallmentPlans().get(0).getPeriod().getId();

        properties.setProductType(input.getProduct().getId());
        properties.setSelectedPlanId(selectedPlan.getId());
        properties.setPeriodId(periodId);
        properties.setFrequencyTypeId(this.applicationConfigurationService.getProperty(periodId));

        LOGGER.info("***** QuotationParameter getProperties - END  properties: {} *****",properties);

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
    public CustomerListASO getCustomer(String customerId) {

        LOGGER.info("***** SimulationParameter getCustomer START *****");
        CustomerListASO customer = this.rbvdr303.executeGetCustomerHost(customerId);

        LOGGER.info("***** SimulationParameter CustomerListASO {} *****",customer);

        LOGGER.info("***** SimulationParameter getCustomer END *****");

        return customer;
    }

    private Map<String, Object> getPaymentFrequency(String frequencyTypeId){

        LOGGER.info("***** QuotationParameter getPaymentFrequency START - frequencyTypeId: {} *****",frequencyTypeId);

        IInsurancePaymentPeriodDAO paymentPeriodDAO = new InsurancePaymentPeriodDAOImpl(this.pisdR350);
        Map<String, Object> paymentFrequency = paymentPeriodDAO.executeGetPaymentFrequencyName(frequencyTypeId);

        LOGGER.info("***** QuotationParameter getPaymentFrequency END - paymentFrequency: {} *****",paymentFrequency);

        return paymentFrequency;
    }
}

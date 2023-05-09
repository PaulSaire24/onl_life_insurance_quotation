package com.bbva.rbvd.lib.r304.impl.dao.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;

import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationDAO;
import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationModDAO;

import com.bbva.pisd.lib.r350.PISDR350;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;

import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;

import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;

import com.bbva.rbvd.lib.r304.impl.dao.DAOService;

import com.bbva.rbvd.lib.r304.impl.util.MapperHelper;

import java.util.HashMap;
import java.util.Map;

import static com.bbva.rbvd.lib.r304.impl.util.ValidationUtil.validateSelectionQueries;
import static com.bbva.rbvd.lib.r304.impl.util.ValidationUtil.validateInsertionQueries;
import static java.util.Collections.singletonMap;

public class DAOServiceImpl implements DAOService {

    protected ApplicationConfigurationService applicationConfigurationService;
    protected PISDR350 pisdR350;
    protected MapperHelper mapperHelper;

    @Override
    public Map<String, Object> executeGetSimulationInformation(final String externalSimulationId) {
        final Map<String, Object> responseGetSimulationIdAndExpirationDate = this.pisdR350.executeGetASingleRow(RBVDProperties.QUERY_GET_SIMULATION_INFORMATION_FOR_EASYES_QUOTATION.getValue(),
                singletonMap(RBVDProperties.FIELD_INSRNC_COMPANY_SIMULATION_ID.getValue(), externalSimulationId));
        validateSelectionQueries(responseGetSimulationIdAndExpirationDate, RBVDErrors.INVALID_RIMAC_QUOTATION_ID);
        return responseGetSimulationIdAndExpirationDate;
    }

    @Override
    public Map<String, Object> executeGetRequiredInformation(final String productType, final String planId) {
        final Map<String, Object> argumentsGetRequiredInformation = new HashMap<>();
        argumentsGetRequiredInformation.put(RBVDProperties.FILTER_INSURANCE_PRODUCT_TYPE.getValue(), productType);
        argumentsGetRequiredInformation.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue(), planId);

        Map<String, Object> responseGetRequiredInformation = this.pisdR350.executeGetASingleRow(
                RBVDProperties.QUERY_GET_REQUIRED_INFORMATION_FOR_EASYES_QUOTATION.getValue(), argumentsGetRequiredInformation);
        validateSelectionQueries(responseGetRequiredInformation, RBVDErrors.INVALID_PRODUCT_TYPE_AND_MODALITY_TYPE);
        return responseGetRequiredInformation;
    }

    @Override
    public Map<String, Object> executeGetPaymentFrequencyName(final String frequencyTypeId) {
        return this.pisdR350.executeGetASingleRow(RBVDProperties.QUERY_GET_PAYMENT_FREQUENCY_NAME.getValue(),
                singletonMap(RBVDProperties.FIELD_POLICY_PAYMENT_FREQUENCY_TYPE.getValue(), frequencyTypeId));
    }

    @Override
    public void executeQuotationQuery(final EasyesQuotationDAO easyesQuotationDAO, final EasyesQuotationDTO easyesQuotationDTO) {
        InsuranceQuotationDAO insuranceQuotationDao = this.mapperHelper.createInsuranceQuotationDAO(easyesQuotationDAO, easyesQuotationDTO);
        Map<String, Object> argumentsQuotationDao = this.mapperHelper.createArgumentsQuotationDao(insuranceQuotationDao);
        Integer quotationResult = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_INSERT_INSURANCE_QUOTATION.getValue(), argumentsQuotationDao);
        validateInsertionQueries(quotationResult, RBVDErrors.QUOTATION_INSERTION_WAS_WRONG);
    }

    @Override
    public void executeQuotationModQuery(final EasyesQuotationDAO easyesQuotationDAO, final EasyesQuotationDTO easyesQuotationDTO,
                                         final EasyesQuotationBO easyesQuotationBO) {
        InsuranceQuotationModDAO insuranceQuotationModDao = this.mapperHelper.createQuotationModDao(easyesQuotationDAO, easyesQuotationDTO, easyesQuotationBO);
        Map<String, Object> argumentsQuotationModDao = this.mapperHelper.createArgumentsQuotationModDao(insuranceQuotationModDao);
        Integer quotationModResult = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_INSERT_INSURANCE_QUOTATION_MOD.getValue(), argumentsQuotationModDao);
        validateInsertionQueries(quotationModResult, RBVDErrors.QUOTATION_MOD_INSERTION_WAS_WRONG);
    }

    public void setApplicationConfigurationService(ApplicationConfigurationService applicationConfigurationService) {this.applicationConfigurationService = applicationConfigurationService;}

    public void setPisdR350(PISDR350 pisdR350) {this.pisdR350 = pisdR350;}

    public void setMapperHelper(MapperHelper mapperHelper) {this.mapperHelper = mapperHelper;}

}

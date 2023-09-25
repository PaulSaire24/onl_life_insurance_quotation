package com.bbva.rbvd.lib.r304.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.mock.MockData;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.QuotationLifeBO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RBVDR304ImplTest {

    private final RBVDR304Impl rbvdr304 = new RBVDR304Impl();
    private PISDR350 pisdR350;
    private QuotationLifeDTO input;
    private Map<String,Object> mapInformation;
    @Mock
    private ApplicationConfigurationService applicationConfigurationService;

    @Before
    public void setUp() throws Exception {
        ThreadContext.set(new Context());

        input = MockData.getInstance().getEasyesInsuranceQuotationRequest();

        EasyesQuotationDAO myQuotation = mock(EasyesQuotationDAO.class);


        pisdR350 = mock(PISDR350.class);
        RBVDR303 rbvdr303 = mock(RBVDR303.class);
        applicationConfigurationService = mock(ApplicationConfigurationService.class);

        when(applicationConfigurationService.getProperty("MONTHLY")).thenReturn("M");

        PayloadProperties properties = new PayloadProperties();
        properties.setFrequencyTypeId("M");
        properties.setSelectedPlanId("02");
        properties.setProductType("840");
        properties.setPeriodId("MONTHLY");
        PayloadConfig payloadConfig = new PayloadConfig();
        payloadConfig.setInput(input);
        payloadConfig.setPayloadProperties(properties);
        payloadConfig.setMyQuotation(myQuotation);
        payloadConfig.setPolicyQuotaId("8523654");

        rbvdr304.setRbvdR303(rbvdr303);
        rbvdr304.setApplicationConfigurationService(applicationConfigurationService);
        rbvdr304.setPisdR350(pisdR350);

        QuotationLifeBO rimacResponse = MockData.getInstance().getInsuranceRimacQuotationResponse();

        when(rbvdr303.executeQuotationRimac(anyObject(), anyString(), anyString())).thenReturn(rimacResponse);

        mapInformation = new HashMap<>();

        mapInformation.put(RBVDProperties.FIELD_INSURANCE_SIMULATION_ID.getValue(),new BigDecimal("1"));
        mapInformation.put(RBVDProperties.FIELD_CUST_SIMULATION_EXPIRED_DATE.getValue(),Timestamp.valueOf("2018-12-12 01:02:03.123456789"));
        mapInformation.put(RBVDProperties.FIELD_INSURANCE_MODALITY_NAME.getValue(),"PLAN 1");
        mapInformation.put(RBVDProperties.FIELD_INSURANCE_COMPANY_MODALITY_ID.getValue(),"568904");
        mapInformation.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue(),new BigDecimal("10"));
        mapInformation.put(RBVDProperties.FIELD_INSURANCE_PRODUCT_DESC.getValue(),"Seguro vida");
        mapInformation.put("PRODUCT_SHORT_DESC","EASYYES");
        mapInformation.put(RBVDProperties.FIELD_PAYMENT_FREQUENCY_NAME.getValue(),"MENSUAL");

        when(pisdR350.executeInsertSingleRow(anyString(),anyMap())).thenReturn(1);

    }

    @Test
    public void testExecuteBusinessLogicEasyesQuotationInsertQuotation_OK() {

        mapInformation.put(RBVDProperties.FIELD_RESULT_NUMBER.getValue(),new BigDecimal(0));
        when(pisdR350.executeGetASingleRow(anyString(), anyMap())).thenReturn(mapInformation);

        QuotationLifeDTO validation = this.rbvdr304.executeBusinessLogicQuotation(input);
        assertNotNull(validation);
    }

    @Test
    public void testExecuteBusinessLogicEasyesQuotationUpdateQuotation_OK() {

        mapInformation.put(RBVDProperties.FIELD_RESULT_NUMBER.getValue(),new BigDecimal(1));
        when(pisdR350.executeGetASingleRow(anyString(), anyMap())).thenReturn(mapInformation);

        QuotationLifeDTO validation = this.rbvdr304.executeBusinessLogicQuotation(input);
        assertNotNull(validation);
    }

    @Test
    public void testExecuteBusinessLogicDynamicLifeInsertQuotation_OK() {
        input.getProduct().setId("841");

        mapInformation.put(RBVDProperties.FIELD_RESULT_NUMBER.getValue(),new BigDecimal(0));
        when(pisdR350.executeGetASingleRow(anyString(), anyMap())).thenReturn(mapInformation);

        QuotationLifeDTO validation = this.rbvdr304.executeBusinessLogicQuotation(input);
        assertNotNull(validation);

    }

    @Test
    public void testExecuteBusinessLogicDynamicLifeUpdateQuotation_OK() {
        input.getProduct().setId("841");
         mapInformation.put(RBVDProperties.FIELD_RESULT_NUMBER.getValue(),new BigDecimal(1));
        when(pisdR350.executeGetASingleRow(anyString(), anyMap())).thenReturn(mapInformation);

        QuotationLifeDTO validation = this.rbvdr304.executeBusinessLogicQuotation(input);
        assertNotNull(validation);
    }

}
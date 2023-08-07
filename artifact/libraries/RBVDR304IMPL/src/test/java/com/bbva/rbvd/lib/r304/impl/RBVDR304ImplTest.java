package com.bbva.rbvd.lib.r304.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;

import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.mock.MockData;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;

import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;

import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r303.RBVDR303;

import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadProperties;
import org.apache.commons.collections.map.HashedMap;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RBVDR304ImplTest {

    private final RBVDR304Impl rbvdr304 = new RBVDR304Impl();
    private RBVDR303 rbvdr303;
    private PISDR350 pisdR350;
    private EasyesQuotationDTO input;
    private EasyesQuotationDAO myQuotation;
    private EasyesQuotationBO rimacResponse;
    private PayloadProperties properties;
    private PayloadConfig payloadConfig;
    @Mock
    private ApplicationConfigurationService applicationConfigurationService;

    @Before
    public void setUp() throws Exception {
        ThreadContext.set(new Context());

        input = MockData.getInstance().getEasyesInsuranceQuotationRequest();

        myQuotation = mock(EasyesQuotationDAO.class);
//        applicationConfigurationService = mock(ApplicationConfigurationService.class);

        when(myQuotation.getInsuranceSimulationId()).thenReturn(BigDecimal.ONE);

        pisdR350 = mock(PISDR350.class);
        applicationConfigurationService = mock(ApplicationConfigurationService.class);

        when(applicationConfigurationService.getProperty(anyString())).thenReturn("M");

        //Map<String,Object> map = new HashMap();
        //map.put("INSRNC_COMPANY_SIMULATION_ID",new HashMap<>());

        when(this.pisdR350.executeInsertSingleRow(
                RBVDProperties.QUERY_GET_SIMULATION_INFORMATION_FOR_EASYES_QUOTATION.getValue(),
                singletonMap(
                        RBVDProperties.FIELD_INSRNC_COMPANY_SIMULATION_ID.getValue(),
                        "491561561"))
        ).thenReturn(1);

        properties = new PayloadProperties();
        properties.setFrequencyTypeId("M");
        properties.setSelectedPlanId("02");
        properties.setProductType("840");
        properties.setPeriodId("MONTHLY");
        payloadConfig = new PayloadConfig();
        payloadConfig.setInput(input);
        payloadConfig.setPayloadProperties(properties);
        payloadConfig.setMyQuotation(myQuotation);
        payloadConfig.setPolicyQuotaId("8523654");


        //daoService = mock(DAOService.class);
        //rbvdr304.setDaoService(daoService);

        //mapperHelper = mock(MapperHelper.class);
        //rbvdr304.setMapperHelper(mapperHelper);

        //when(QuotationBean.createQuotationDao(anyMap(), anyMap(), anyMap())).thenReturn(easyesQuotationDao);

        rbvdr303 = mock(RBVDR303.class);
        rbvdr304.setRbvdR303(rbvdr303);
        rbvdr304.setApplicationConfigurationService(applicationConfigurationService);
        rbvdr304.setPisdR350(pisdR350);

        rimacResponse = MockData.getInstance().getInsuranceRimacQuotationResponse();

        when(rbvdr303.executeEasyesQuotationRimac(anyObject(), anyString(), anyString())).thenReturn(rimacResponse);

        //when(this.insurancePolicy.executeValidateQuotation(anyString())).
        //        thenReturn(singletonMap(RBVDProperties.FIELD_RESULT_NUMBER.getValue(), BigDecimal.ZERO));

    }

    @Test
    public void testExecuteBusinessLogicEasyesQutation_OK() {
        //IInsuranceSimulationDAO simulationDAO =new InsuranceSimulationDAO(this.pisdR350);

        //input.setExternalSimulationId("256c3b09-eca6-4430-a74b-e99ff907dff6");

        //when(simulationDAO.executeGetSimulationInformation(input.getExternalSimulationId())).
        //        thenThrow(build(RBVDErrors.INVALID_RIMAC_QUOTATION_ID));
        //assertEquals(RBVDErrors.INVALID_RIMAC_QUOTATION_ID.getAdviceCode(), this.rbvdr304.getAdvice().getCode());
        //when(this.pisdR350.executeInsertSingleRow(anyString(), anyMap())).thenReturn(1);
        EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(input);

        assertNotNull(validation);

    }
}
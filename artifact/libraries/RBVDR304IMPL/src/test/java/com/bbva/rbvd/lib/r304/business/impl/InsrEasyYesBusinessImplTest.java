package com.bbva.rbvd.lib.r304.business.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.mock.MockData;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.QuotationLifeBO;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.impl.RBVDR304Impl;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadProperties;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class InsrEasyYesBusinessImplTest {
    private final RBVDR304Impl rbvdr304 = new RBVDR304Impl();
    private RBVDR303 rbvdr303;
    private PayloadStore payloadStore;
    private CustomerBO customerBO;
    private CustomerListASO customerBO1;
    @Mock
    private ApplicationConfigurationService applicationConfigurationService;

    @Before
    public void setUp() throws Exception {

        QuotationLifeDTO input = MockData.getInstance().getEasyesInsuranceQuotationRequest();

        EasyesQuotationDAO myQuotation = mock(EasyesQuotationDAO.class);
        when(myQuotation.getInsuranceSimulationId()).thenReturn(BigDecimal.ONE);

        PISDR350 pisdR350 = mock(PISDR350.class);
        rbvdr303 = mock(RBVDR303.class);
        applicationConfigurationService = mock(ApplicationConfigurationService .class);

        when(applicationConfigurationService.getProperty("MONTHLY")).thenReturn("M");

        customerBO = new CustomerBO();
        customerBO1 = new CustomerListASO();

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

        payloadStore = new PayloadStore();
        payloadStore.setInput(input);
        payloadStore.setMyQuotation(myQuotation);
        payloadStore.setRimacResponse(rimacResponse);
        payloadStore.setFrequencyType("M");
    }

    @Test
    public void testMappingOutputFieldsEasyes() {
        customerBO.setFirstName("Adrian");
        customerBO.setLastName("Lopes");
        customerBO.setSecondLastName("Herrera");
        customerBO.setLastName("Lopes");
        customerBO.setSecondLastName("Herrera");
        List<CustomerBO> customerBOS= new ArrayList<>();
        customerBOS.add(customerBO);
        customerBO1.setData(customerBOS);
        when(this.rbvdr303.executeGetCustomerHost(anyObject())).thenReturn(customerBO1);

        InsrEasyYesBusinessImpl easyYes = new InsrEasyYesBusinessImpl(rbvdr303);
        QuotationLifeDTO validation = easyYes.mappingOutputFieldsEasyes(payloadStore);

        assertNotNull(validation);
    }
}
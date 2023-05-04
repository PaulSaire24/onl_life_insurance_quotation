package com.bbva.rbvd.lib.r304.dao;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.impl.dao.impl.DAOServiceImpl;
import com.bbva.rbvd.lib.r304.impl.util.MapperHelper;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DAOServiceImplTest {

    private final DAOServiceImpl daoServiceImpl = new DAOServiceImpl();

    private PISDR350 pisdr350;
    private MapperHelper mapperHelper;

    private Map<String, Object> mockResponse;

    @Before
    public void setUp() {
        pisdr350 = mock(PISDR350.class);
        daoServiceImpl.setPisdR350(pisdr350);

        mapperHelper = mock(MapperHelper.class);
        daoServiceImpl.setMapperHelper(mapperHelper);

        mockResponse = singletonMap("key", "value");
    }

    @Test(expected = BusinessException.class)
    public void executeGetSimulationInformationWithBusinessException() {
        this.daoServiceImpl.executeGetSimulationInformation("externalSimulationId");
    }

    @Test
    public void executeGetSimulationInformation_OK() {
        final String mockExternalSimulationId = "externalSimulationId";

        Map<String, Object> argumentsGetSimulationInformation = singletonMap(RBVDProperties.FIELD_INSRNC_COMPANY_SIMULATION_ID.getValue(), mockExternalSimulationId);

        when(this.pisdr350.executeGetASingleRow(RBVDProperties.QUERY_GET_SIMULATION_INFORMATION_FOR_EASYES_QUOTATION.getValue(),
                argumentsGetSimulationInformation)).thenReturn(mockResponse);
        Map<String, Object> validation = this.daoServiceImpl.executeGetSimulationInformation(mockExternalSimulationId);
        assertNotNull(validation);
    }

    @Test(expected = BusinessException.class)
    public void executeGetRequiredInformationWithBusinessException() {
        this.daoServiceImpl.executeGetRequiredInformation("productType", "planId");
    }

    @Test
    public void executeGetRequiredInformation_OK() {
        final String mockProductType = "productType";
        final String mockPlanId = "planId";

        Map<String, Object> argumentsGetRequiredInformation = new HashMap();
        argumentsGetRequiredInformation.put(RBVDProperties.FILTER_INSURANCE_PRODUCT_TYPE.getValue(), mockProductType);
        argumentsGetRequiredInformation.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue(), mockPlanId);

        when(this.pisdr350.executeGetASingleRow(RBVDProperties.QUERY_GET_REQUIRED_INFORMATION_FOR_EASYES_QUOTATION.getValue(),
                argumentsGetRequiredInformation)).thenReturn(mockResponse);
        Map<String, Object> validation = this.daoServiceImpl.executeGetRequiredInformation(mockProductType, mockPlanId);
        assertNotNull(validation);
    }

    @Test
    public void executeGetPaymentFrequencyName_OK() {
        final String mockFrequencyTypeId = "frequencyTypeId";

        Map<String, Object> argumentsGetPaymentFrequencyName = singletonMap(RBVDProperties.FIELD_POLICY_PAYMENT_FREQUENCY_TYPE.getValue(),
                mockFrequencyTypeId);

        when(this.pisdr350.executeGetASingleRow(RBVDProperties.QUERY_GET_PAYMENT_FREQUENCY_NAME.getValue(), argumentsGetPaymentFrequencyName)).
                thenReturn(mockResponse);
        Map<String, Object> validation = this.daoServiceImpl.executeGetPaymentFrequencyName(mockFrequencyTypeId);
        assertNotNull(validation);
    }

    @Test(expected = BusinessException.class)
    public void executeQuotationQueryWithBusinessException() {
        this.daoServiceImpl.executeQuotationQuery(new EasyesQuotationDAO(), new EasyesQuotationDTO());
    }

    @Test
    public void executeQuotationQuery_OK() {
        when(this.pisdr350.executeInsertSingleRow(RBVDProperties.QUERY_INSERT_INSURANCE_QUOTATION.getValue(), new HashMap<>())).thenReturn(1);

        this.daoServiceImpl.executeQuotationQuery(new EasyesQuotationDAO(), new EasyesQuotationDTO());
    }

    @Test(expected = BusinessException.class)
    public void executeQuotationModQueryWithBusinessException() {
        this.daoServiceImpl.executeQuotationModQuery(new EasyesQuotationDAO(), new EasyesQuotationDTO());
    }

    @Test
    public void executeQuotationModQuery_OK() {
        when(this.pisdr350.executeInsertSingleRow(RBVDProperties.QUERY_INSERT_INSURANCE_QUOTATION_MOD.getValue(), new HashMap<>())).thenReturn(1);

        this.daoServiceImpl.executeQuotationModQuery(new EasyesQuotationDAO(), new EasyesQuotationDTO());
    }

}

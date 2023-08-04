package com.bbva.rbvd.lib.r304.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;

import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.mock.MockData;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;

import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;

import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.impl.RBVDR304Impl;

import com.bbva.rbvd.lib.r304.service.dao.impl.InsurancePolicyDAO;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceProductModalityDAO;
import com.bbva.rbvd.lib.r304.service.dao.impl.InsuranceSimulationDAO;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.math.BigDecimal;

import static java.util.Collections.singletonMap;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static com.bbva.rbvd.dto.lifeinsrc.utils.RBVDValidation.build;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
        "classpath:/META-INF/spring/RBVDR304-app.xml",
        "classpath:/META-INF/spring/RBVDR304-app-test.xml",
        "classpath:/META-INF/spring/RBVDR304-arc.xml",
        "classpath:/META-INF/spring/RBVDR304-arc-test.xml" })
public class RBVDR304ImplTest {
    private final RBVDR304Impl rbvdr304 = new RBVDR304Impl();
    private RBVDR303 rbvdr303;
    private PISDR350 pisdR350;
    private EasyesQuotationDTO input;
    private EasyesQuotationDAO easyesQuotationDao;
    private EasyesQuotationBO rimacResponse;
    private InsurancePolicyDAO insurancePolicy;

    @Before
    public void setUp() throws IOException {
        ThreadContext.set(new Context());
        this.input.getProduct().setId("840");

        input = MockData.getInstance().getEasyesInsuranceQuotationRequest();

        easyesQuotationDao = mock(EasyesQuotationDAO.class);
        when(easyesQuotationDao.getInsuranceSimulationId()).thenReturn(BigDecimal.ONE);

        pisdR350 = mock(PISDR350.class);

        ApplicationConfigurationService applicationConfigurationService = mock(ApplicationConfigurationService.class);

        when(applicationConfigurationService.getProperty("MONTHLY")).thenReturn("M");

        //daoService = mock(DAOService.class);
        //rbvdr304.setDaoService(daoService);

        //mapperHelper = mock(MapperHelper.class);
        //rbvdr304.setMapperHelper(mapperHelper);

        //when(mapperHelper.createQuotationDao(anyMap(), anyMap(), anyMap())).thenReturn(easyesQuotationDao);

        rbvdr303 = mock(RBVDR303.class);
        rbvdr304.setRbvdR303(rbvdr303);

        rimacResponse = MockData.getInstance().getInsuranceRimacQuotationResponse();

        when(rbvdr303.executeEasyesQuotationRimac(anyObject(), anyString(), anyString())).thenReturn(rimacResponse);

        when(this.insurancePolicy.executeValidateQuotation(anyString())).
                thenReturn(singletonMap(RBVDProperties.FIELD_RESULT_NUMBER.getValue(), BigDecimal.ZERO));
    }

    @Test
	public void executeBusinessLogicEasyesQutationWithWrongGetSimulationInformationResponse() {

		InsuranceSimulationDAO simulationDAO =new InsuranceSimulationDAO(this.pisdR350);

		when(simulationDAO.executeGetSimulationInformation(input.getExternalSimulationId())).
				thenThrow(build(RBVDErrors.INVALID_RIMAC_QUOTATION_ID));
		EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(input);
		assertEquals(RBVDErrors.INVALID_RIMAC_QUOTATION_ID.getAdviceCode(), this.rbvdr304.getAdvice().getCode());
		assertNull(validation);
	}

}
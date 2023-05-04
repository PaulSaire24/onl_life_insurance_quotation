package com.bbva.rbvd.lib.r304;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.mock.MockData;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;

import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;

import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.impl.RBVDR304Impl;

import com.bbva.rbvd.lib.r304.impl.dao.DAOService;

import com.bbva.rbvd.lib.r304.impl.util.MapperHelper;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyMap;
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
public class RBVDR304Test {

	private final RBVDR304Impl rbvdr304 = new RBVDR304Impl();

	private ApplicationConfigurationService applicationConfigurationService;
	private DAOService daoService;
	private MapperHelper mapperHelper;
	private RBVDR303 rbvdr303;

	private EasyesQuotationDTO easyesQuotationDto;
	private EasyesQuotationDAO easyesQuotationDao;
	private EasyesQuotationBO rimacResponse;

	@Before
	public void setUp() throws IOException {
		ThreadContext.set(new Context());

		easyesQuotationDto = MockData.getInstance().getEasyesInsuranceQuotationRequest();

		easyesQuotationDao = mock(EasyesQuotationDAO.class);
		when(easyesQuotationDao.getInsuranceSimulationId()).thenReturn(BigDecimal.ONE);

		applicationConfigurationService = mock(ApplicationConfigurationService.class);
		rbvdr304.setApplicationConfigurationService(applicationConfigurationService);

		when(applicationConfigurationService.getProperty("MONTHLY")).thenReturn("M");

		daoService = mock(DAOService.class);
		rbvdr304.setDaoService(daoService);

		mapperHelper = mock(MapperHelper.class);
		rbvdr304.setMapperHelper(mapperHelper);

		when(mapperHelper.createQuotationDao(anyMap(), anyMap(), anyMap())).thenReturn(easyesQuotationDao);

		rbvdr303 = mock(RBVDR303.class);
		rbvdr304.setRbvdR303(rbvdr303);

		rimacResponse = MockData.getInstance().getInsuranceRimacQuotationResponse();

		when(rbvdr303.executeEasyesQuotationRimac(anyObject(), anyString(), anyString())).thenReturn(rimacResponse);
	}

	@Test
	public void executeBusinessLogicEasyesQutationWithWrongGetSimulationInformationResponse() {
		when(this.daoService.executeGetSimulationInformation(easyesQuotationDto.getExternalSimulationId())).
				thenThrow(build(RBVDErrors.INVALID_RIMAC_QUOTATION_ID));
		EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(easyesQuotationDto);
		assertNull(validation);
	}

	@Test
	public void executeBusinessLogicEasyesQutationWithWrongProductAndModalityType() {
		final String productType = this.easyesQuotationDto.getProduct().getId();
		final String planId = this.easyesQuotationDto.getProduct().getPlans().get(0).getId();

		when(this.daoService.executeGetRequiredInformation(productType, planId)).
				thenThrow(build(RBVDErrors.INVALID_PRODUCT_TYPE_AND_MODALITY_TYPE));
		EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(easyesQuotationDto);
		assertNull(validation);
	}

	@Test
	public void executeBusinessLogicEasyesQutationWithRimacErrorResponse() {
		when(this.rbvdr303.executeEasyesQuotationRimac(anyObject(), anyString(), anyString())).thenReturn(null);
		EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(easyesQuotationDto);
		assertNull(validation);
	}

	@Test
	public void executeBusinessLogicEasyesQutationWithQuotationInsertionWrong() {
		doThrow(build(RBVDErrors.QUOTATION_INSERTION_WAS_WRONG)).
				when(this.daoService).executeQuotationQuery(anyObject(), anyObject());
		EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(easyesQuotationDto);
		assertNull(validation);
	}

	@Test
	public void executeBusinessLogicEasyesQutationWithQuotationModInsertionWrong() {
		doThrow(build(RBVDErrors.QUOTATION_MOD_INSERTION_WAS_WRONG)).
				when(this.daoService).executeQuotationModQuery(anyObject(), anyObject());
		EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(easyesQuotationDto);
		assertNull(validation);
	}

	@Test
	public void executeBusinessLogicEasyesQutationOK() {
		EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(easyesQuotationDto);
		assertNotNull(validation);
	}

}

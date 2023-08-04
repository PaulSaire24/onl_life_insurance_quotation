package com.bbva.rbvd.lib.r304;

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

/*@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/RBVDR304-app.xml",
		"classpath:/META-INF/spring/RBVDR304-app-test.xml",
		"classpath:/META-INF/spring/RBVDR304-arc.xml",
		"classpath:/META-INF/spring/RBVDR304-arc-test.xml" })

 */
public class AntiguoRBVDR304Test {
/*
	private final RBVDR304Impl rbvdr304 = new RBVDR304Impl();

	//private DAOService daoService;
	//private MapperHelper mapperHelper;
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
*/
/*	@Test
	public void executeBusinessLogicEasyesQutationWithWrongGetSimulationInformationResponse() {

		IInsuranceSimulationDAO simulationDAO =new InsuranceSimulationDAO(this.pisdR350);

		when(simulationDAO.executeGetSimulationInformation(easyesQuotationDto.getExternalSimulationId())).
				thenThrow(build(RBVDErrors.INVALID_RIMAC_QUOTATION_ID));
		EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(easyesQuotationDto);
		assertEquals(RBVDErrors.INVALID_RIMAC_QUOTATION_ID.getAdviceCode(), this.rbvdr304.getAdvice().getCode());
		assertNull(validation);
	}
*/
/*	@Test
	public void executeBusinessLogicEasyesQutationWithWrongProductAndModalityType() {
		final String productType = this.input.getProduct().getId();
		final String planId = this.input.getProduct().getPlans().get(0).getId();

		InsuranceProductModalityDAO productDAO = new InsuranceProductModalityDAO(this.pisdR350);

		when(productDAO.executeGetRequiredInformation(productType, planId)).
				thenThrow(build(RBVDErrors.INVALID_PRODUCT_TYPE_AND_MODALITY_TYPE));
		EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(input);
		assertEquals(RBVDErrors.INVALID_PRODUCT_TYPE_AND_MODALITY_TYPE.getAdviceCode(), this.rbvdr304.getAdvice().getCode());
		assertNull(validation);
	}
*/
/*	@Test
	public void executeBusinessLogicEasyesQutationWithRimacErrorResponse() {
		when(this.rbvdr303.executeEasyesQuotationRimac(anyObject(), anyString(), anyString())).thenReturn(null);
		EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(easyesQuotationDto);
		assertEquals(RBVDErrors.COULDNT_SELECT_MODALITY_RIMAC_ERROR.getAdviceCode(), this.rbvdr304.getAdvice().getCode());
		assertNull(validation);
	}

	@Test
	public void executeBusinessLogicEasyesQutationWithWrongUpdateQuotationMod() {
		Map<String, Object> resultCount = singletonMap(RBVDProperties.FIELD_RESULT_NUMBER.getValue(), BigDecimal.ONE);
		when(this.daoService.executeValidateQuotation(anyString())).thenReturn(resultCount);
		doThrow(build(RBVDErrors.QUOTATION_MOD_INSERTION_WAS_WRONG))
				.when(this.daoService).executeUpdateQuotationModQuery(anyObject(), anyObject());
		EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(easyesQuotationDto);
		assertEquals(RBVDErrors.QUOTATION_MOD_INSERTION_WAS_WRONG.getAdviceCode(), this.rbvdr304.getAdvice().getCode());
		assertNull(validation);
	}

	@Test
	public void executeBusinessLogicEasyesQutationWithQuotationInsertionWrong() {
		doThrow(build(RBVDErrors.QUOTATION_INSERTION_WAS_WRONG)).
				when(this.daoService).executeQuotationQuery(anyObject(), anyObject());
		EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(easyesQuotationDto);
		assertEquals(RBVDErrors.QUOTATION_INSERTION_WAS_WRONG.getAdviceCode(), this.rbvdr304.getAdvice().getCode());
		assertNull(validation);
	}

	@Test
	public void executeBusinessLogicEasyesQutationWithQuotationModInsertionWrong() {
		doThrow(build(RBVDErrors.QUOTATION_MOD_INSERTION_WAS_WRONG)).
				when(this.daoService).executeQuotationModQuery(anyObject(), anyObject(), anyObject());
		EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(easyesQuotationDto);
		assertEquals(RBVDErrors.QUOTATION_MOD_INSERTION_WAS_WRONG.getAdviceCode(), this.rbvdr304.getAdvice().getCode());
		assertNull(validation);
	}

	@Test
	public void executeBusinessLogicEasyesQutationWithCreationFlowOK() {
		EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(easyesQuotationDto);
		assertNotNull(validation);
	}

	@Test
	public void executeBusinessLogicEasyesQutationWithUpdateFlowOK() {
		Map<String, Object> resultCount = singletonMap(RBVDProperties.FIELD_RESULT_NUMBER.getValue(), BigDecimal.ONE);
		when(this.daoService.executeValidateQuotation(anyString())).thenReturn(resultCount);
		EasyesQuotationDTO validation = this.rbvdr304.executeBusinessLogicEasyesQutation(easyesQuotationDto);
		assertNotNull(validation);
	}*/
}

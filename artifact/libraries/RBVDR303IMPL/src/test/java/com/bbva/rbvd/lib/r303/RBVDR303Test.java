package com.bbva.rbvd.lib.r303;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import com.bbva.elara.utility.api.connector.APIConnector;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALW5;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEMSALWU;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pbtq.lib.r002.PBTQR002;
import com.bbva.pisd.dto.insurance.amazon.SignatureAWS;
import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.pisd.dto.insurance.mock.MockDTO;
import com.bbva.pisd.lib.r014.PISDR014;
import com.bbva.rbvd.dto.lifeinsrc.mock.MockData;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.QuotationLifeBO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r303.impl.RBVDR303Impl;
import com.bbva.rbvd.lib.r303.impl.business.ExceptionBusiness;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/RBVDR303-app.xml",
		"classpath:/META-INF/spring/RBVDR303-app-test.xml",
		"classpath:/META-INF/spring/RBVDR303-arc.xml",
		"classpath:/META-INF/spring/RBVDR303-arc-test.xml" })
public class RBVDR303Test {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR303Test.class);
	private final ExceptionBusiness rimacExceptionHandler = new ExceptionBusiness();
	private final RBVDR303Impl rbvdR303 = new RBVDR303Impl();

	private ApplicationConfigurationService applicationConfigurationService;
	private APIConnector externalApiConnector;
	private PISDR014 pisdr014;
	private PBTQR002 pbtqr002;
	private CustomerListASO customerList;
	private static final String MESSAGE_EXCEPTION = "CONNECTION ERROR";
	private String errorMessage;
	private MockDTO mockDTO;
	@Before
	public void setUp() {
		ThreadContext.set(new Context());

		applicationConfigurationService = mock(ApplicationConfigurationService.class);
		rbvdR303.setApplicationConfigurationService(applicationConfigurationService);
		when(this.applicationConfigurationService.getProperty(RBVDProperties.QUOTATION_EASYES_RIMAC_URI.getValue()))
				.thenReturn("/api-vida/V1/cotizaciones/ideCotizacion/seleccionar");


		externalApiConnector = mock(APIConnector.class);
		rbvdR303.setExternalApiConnector(externalApiConnector);

		PISDR014 pisdr014 = mock(PISDR014.class);
		rbvdR303.setPisdR014(pisdr014);

		pbtqr002 = mock(PBTQR002.class);
		rbvdR303.setPbtqR002(pbtqr002);

		mockDTO = MockDTO.getInstance();


		when(pisdr014.executeSignatureConstruction(anyString(), anyString(), anyString(), anyString(), anyString()))
				.thenReturn(new SignatureAWS("", "", "", ""));

		errorMessage = "Something went wrong!!";
	}
	
	@Test
	public void executeEasyesQuotationRimacOK() throws IOException {
		QuotationLifeBO rimacResponse = MockData.getInstance().getInsuranceRimacQuotationResponse();

		when(this.externalApiConnector.exchange(anyString(), anyObject(), anyObject(), (Class<QuotationLifeBO>) any(), anyMap())).
				thenReturn(new ResponseEntity(rimacResponse, HttpStatus.OK));

		QuotationLifeBO validation = this.rbvdR303.executeQuotationRimac(new QuotationLifeBO(),
				"rimacQuotation", "traceId");

		assertNotNull(validation);
		assertNotNull(validation.getPayload());
		assertNotNull(validation.getPayload().getStatus());
		assertNotNull(validation.getPayload().getMensaje());
		assertNotNull(validation.getPayload().getDetalleCotizacion());
		assertNotNull(validation.getPayload().getDetalleCotizacion().get(0));
		assertNotNull(validation.getPayload().getDetalleCotizacion().get(0).getFechaCreacion());
		assertNotNull(validation.getPayload().getDetalleCotizacion().get(0).getFechaExpiracion());
	}

	@Test
	public void executeEasyesQuotationRimacWithRestClientException() {
		when(this.externalApiConnector.exchange(anyString(), Mockito.any(HttpMethod.class), any(), (Class<QuotationLifeBO>) any(), anyMap())).
				thenReturn(new ResponseEntity<>(null, HttpStatus.BAD_REQUEST));

		QuotationLifeBO validation = this.rbvdR303.executeQuotationRimac(new QuotationLifeBO(), "rimacQuotation", "traceId");

		assertNull(validation);
	}


	@Test(expected = BusinessException.class)
	public void handler_HttpClientErrorExceptionWithTwoDetails() {
		LOGGER.info("RBVDR303Test - Executing handler_HttpClientErrorException");
		String responseBody = "{\"error\":{\"code\":\"VIDA001\",\"message\":\"Error al Validar Datos.\",\"details\":[\"El plan 533721, no existe en la cotización.\",\"El codigo de plan no existe\"],\"httpStatus\":400}}";
		HttpClientErrorException clientErrorException = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "", responseBody.getBytes(), StandardCharsets.UTF_8);
		this.rimacExceptionHandler.handler(clientErrorException);
	}

	@Test(expected = BusinessException.class)
	public void handler_HttpClientErrorExceptionWithoutDetails() {
		LOGGER.info("RBVDR303Test - Executing handler_HttpClientErrorException");
		String responseBody = "{\"error\":{\"code\":\"VIDA001\",\"message\":\"El plan 533721, no existe en la cotización.\",\"httpStatus\":400}}";
		HttpClientErrorException clientErrorException = new HttpClientErrorException(HttpStatus.BAD_REQUEST, "", responseBody.getBytes(), StandardCharsets.UTF_8);
		this.rimacExceptionHandler.handler(clientErrorException);
	}

	@Test(expected = BusinessException.class)
	public void handler_HttpServerErrorException() {
		LOGGER.info("RBVDR303Test - Executing handler_HttpServerErrorException");
		HttpServerErrorException serverErrorException = new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "");
		this.rimacExceptionHandler.handler(serverErrorException);
	}

	@Test
	public void executeGetCustomerHostOk() {

		PEWUResponse responseHost = new PEWUResponse();

		PEMSALWU data = new PEMSALWU();
		data.setTdoi("L");
		data.setSexo("M");
		data.setContact("123123123");
		data.setContac2("123123123");
		data.setContac3("123123123");
		responseHost.setPemsalwu(data);
		responseHost.setPemsalw5(new PEMSALW5());
		responseHost.setHostAdviceCode(null);
		when(pbtqr002.executeSearchInHostByCustomerId("00000000"))
				.thenReturn(responseHost);

		CustomerListASO validation = rbvdR303.executeGetCustomerHost("00000000");

		assertNotNull(validation);
	}

	@Test
	public void executeGetCustomerHost_Null() {

		PEWUResponse responseHost = new PEWUResponse();

		PEMSALWU data = new PEMSALWU();
		data.setTdoi("L");
		data.setSexo("M");
		data.setContact("123123123");
		data.setContac2("123123123");
		data.setContac3("123123123");
		responseHost.setPemsalwu(data);
		responseHost.setPemsalw5(new PEMSALW5());
		responseHost.setHostAdviceCode("code");
		when(pbtqr002.executeSearchInHostByCustomerId("00000000"))
				.thenReturn(responseHost);

		CustomerListASO validation = rbvdR303.executeGetCustomerHost("00000000");

		assertNull(validation);
	}

}

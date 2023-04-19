package com.bbva.rbvd.lib.r303;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;

import com.bbva.elara.utility.api.connector.APIConnector;

import com.bbva.pisd.dto.insurance.amazon.SignatureAWS;
import com.bbva.pisd.lib.r014.PISDR014;

import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;

import com.bbva.rbvd.lib.r303.impl.RBVDR303Impl;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.RestClientException;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.anyString;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {
		"classpath:/META-INF/spring/RBVDR303-app.xml",
		"classpath:/META-INF/spring/RBVDR303-app-test.xml",
		"classpath:/META-INF/spring/RBVDR303-arc.xml",
		"classpath:/META-INF/spring/RBVDR303-arc-test.xml" })
public class RBVDR303Test {

	private final RBVDR303Impl rbvdR303 = new RBVDR303Impl();

	private ApplicationConfigurationService applicationConfigurationService;
	private APIConnector externalApiConnector;
	private PISDR014 pisdr014;

	@Before
	public void setUp() {
		ThreadContext.set(new Context());

		applicationConfigurationService = mock(ApplicationConfigurationService.class);
		rbvdR303.setApplicationConfigurationService(applicationConfigurationService);

		externalApiConnector = mock(APIConnector.class);
		rbvdR303.setExternalApiConnector(externalApiConnector);

		pisdr014 = mock(PISDR014.class);
		rbvdR303.setPisdR014(pisdr014);

		when(this.pisdr014.executeSignatureConstruction(anyString(), anyString(), anyString(), anyString(), anyString())).
				thenReturn(new SignatureAWS());
	}
	
	@Test
	public void executeEasyesQuotationRimacOK() {
		when(this.externalApiConnector.postForObject(anyString(), anyObject(), any(), (Object) anyVararg())).
				thenReturn(new EasyesQuotationBO());

		EasyesQuotationBO validation = this.rbvdR303.executeEasyesQuotationRimac(new EasyesQuotationBO(),
				"rimacQuotation", "traceId");

		assertNotNull(validation);
	}

	@Test
	public void executeEasyesQuotationRimacWithRestClientException() {
		when(this.externalApiConnector.postForObject(anyString(), anyObject(), any(), (Object) anyVararg())).
				thenThrow(new RestClientException("Something went wrong!!"));

		EasyesQuotationBO validation = this.rbvdR303.executeEasyesQuotationRimac(new EasyesQuotationBO(),
				"rimacQuotation", "traceId");

		assertNull(validation);
	}

}

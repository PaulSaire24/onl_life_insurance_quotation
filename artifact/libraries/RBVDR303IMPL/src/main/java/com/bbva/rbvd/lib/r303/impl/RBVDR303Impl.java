package com.bbva.rbvd.lib.r303.impl;

import com.bbva.pisd.dto.insurance.amazon.SignatureAWS;

import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;

import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r303.impl.util.JsonHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;

import org.springframework.web.client.RestClientException;

import java.nio.charset.StandardCharsets;

public class RBVDR303Impl extends RBVDR303Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR303Impl.class);

	@Override
	public EasyesQuotationBO executeEasyesQuotationRimac(final EasyesQuotationBO easyesQuotationRimacRequest, final String rimacQuotation, final String traceId) {
		LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac START *****");

		String requestBody = getRequestBodyAsJsonFormat(easyesQuotationRimacRequest);

		LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac ***** Request body: {}", requestBody);

		String uri = this.applicationConfigurationService.getProperty(RBVDProperties.QUOTATION_EASYES_RIMAC_URI.getValue());

		SignatureAWS signature = this.pisdR014.executeSignatureConstruction(requestBody,
				HttpMethod.POST.toString(), uri, null, traceId);

		HttpHeaders headers = this.createHttpHeaders(signature);

		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

		try {
			EasyesQuotationBO easyQuotationRimacResponse = this.externalApiConnector.postForObject(RBVDProperties.QUOTATION_EASYES_RIMAC.getValue(), entity,
					EasyesQuotationBO.class, rimacQuotation);
			LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac ***** Response body: {}", getRequestBodyAsJsonFormat(easyQuotationRimacResponse));
			LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac END *****");
			return easyQuotationRimacResponse;
		} catch (RestClientException exception) {
			LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac ***** Exception: {}", exception.getMessage());
			return null;
		}

	}

	private String getRequestBodyAsJsonFormat(Object source) {
		return JsonHelper.getInstance().convertObjectToJsonString(source);
	}

	private HttpHeaders createHttpHeaders(final SignatureAWS signature) {
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
		headers.setContentType(mediaType);
		headers.set("Authorization", signature.getAuthorization());
		headers.set("X-Amz-Date", signature.getxAmzDate());
		headers.set("x-api-key", signature.getxApiKey());
		headers.set("traceId", signature.getTraceId());
		return headers;
	}

}

package com.bbva.rbvd.lib.r303.impl;

import com.bbva.pisd.dto.insurance.amazon.SignatureAWS;

import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;

import com.bbva.pisd.dto.insurance.utils.PISDProperties;

import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;

import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;

import com.bbva.rbvd.lib.r303.impl.util.JsonHelper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestClientException;

import java.nio.charset.StandardCharsets;

import static java.util.Collections.singletonMap;

public class RBVDR303Impl extends RBVDR303Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR303Impl.class);

	@Override
	public EasyesQuotationBO executeEasyesQuotationRimac(final EasyesQuotationBO easyesQuotationRimacRequest, final String rimacQuotation, final String traceId) {
		LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac START *****");

		String requestBody = getRequestBodyAsJsonFormat(easyesQuotationRimacRequest);

		LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac ***** Request body: {}", requestBody);

		final String key = "ideCotizacion";

		String uriFromConsole = this.applicationConfigurationService.
				getProperty(RBVDProperties.QUOTATION_EASYES_RIMAC_URI.getValue());

		String uri = uriFromConsole.replace(key, rimacQuotation);

		LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac ***** URI: {}", uri);

		SignatureAWS signature = this.pisdR014.executeSignatureConstruction(requestBody,
				HttpMethod.PATCH.toString(), uri, null, traceId);

		HttpHeaders headers = this.createHttpHeaders(signature);

		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

		try {
			ResponseEntity<EasyesQuotationBO> responseEntity = this.externalApiConnector.exchange(
					RBVDProperties.QUOTATION_EASYES_RIMAC.getValue(), HttpMethod.PATCH, entity, EasyesQuotationBO.class, singletonMap(key, rimacQuotation));
			EasyesQuotationBO easyQuotationRimacResponse = responseEntity.getBody();
			LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac ***** Response body: {}", getRequestBodyAsJsonFormat(easyQuotationRimacResponse));
			LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac END *****");
			return easyQuotationRimacResponse;
		} catch (RestClientException exception) {
			LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac ***** Exception: {}", exception.getMessage());
			return null;
		}

	}

	@Override
	public CustomerBO executeListCustomerService(final String customerId) {
		LOGGER.info("***** RBVDR303Impl - executeListCustomerService START *****");

		final String key = "customerId";

		try {
			CustomerListASO customerInformationASO = this.internalApiConnector.getForObject(PISDProperties.ID_API_CUSTOMER_INFORMATION.getValue(),
					CustomerListASO.class, singletonMap(key, customerId));
			LOGGER.info("***** RBVDR303Impl - executeListCustomerService END *****");
			return customerInformationASO.getData().get(0);
		} catch (RestClientException exception) {
			LOGGER.info("***** RBVDR303Impl - executeListCustomerService ***** Exception: {}", exception.getMessage());
			return null;
		}
	}

	private String getRequestBodyAsJsonFormat(final Object source) {
		return JsonHelper.getInstance().convertObjectToJsonString(source);
	}

	private HttpHeaders createHttpHeaders(final SignatureAWS signature) {
		HttpHeaders headers = new HttpHeaders();
		MediaType mediaType = new MediaType("application", "json", StandardCharsets.UTF_8);
		headers.setContentType(mediaType);
		headers.set(RBVDProperties.AUTHORIZATION_HEADER.getValue(), signature.getAuthorization());
		headers.set(RBVDProperties.XAMZDATE_HEADER.getValue(), signature.getxAmzDate());
		headers.set(RBVDProperties.XAPIKEY_HEADER.getValue(), signature.getxApiKey());
		headers.set(RBVDProperties.TRACEID_HEADER.getValue(), signature.getTraceId());
		return headers;
	}

}

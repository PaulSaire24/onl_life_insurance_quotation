package com.bbva.rbvd.lib.r303.impl;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.apx.exception.io.network.TimeoutException;
import com.bbva.pbtq.dto.validatedocument.response.host.pewu.PEWUResponse;
import com.bbva.pisd.dto.insurance.amazon.SignatureAWS;

import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.pisd.dto.insurance.bo.IdentityDocumentsBO;
import com.bbva.pisd.dto.insurance.bo.GenderBO;
import com.bbva.pisd.dto.insurance.bo.BirthDataBO;
import com.bbva.pisd.dto.insurance.bo.ContactDetailsBO;
import com.bbva.pisd.dto.insurance.bo.AddressesBO;
import com.bbva.pisd.dto.insurance.bo.LocationBO;
import com.bbva.pisd.dto.insurance.bo.GeographicGroupsBO;
import com.bbva.pisd.dto.insurance.bo.CountryBO;
import com.bbva.pisd.dto.insurance.bo.DocumentTypeBO;
import com.bbva.pisd.dto.insurance.bo.ContactTypeBO;
import com.bbva.pisd.dto.insurance.bo.AddressTypeBO;

import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;

import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.QuotationLifeBO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;

import com.bbva.rbvd.lib.r303.impl.business.ExceptionBusiness;
import com.bbva.rbvd.lib.r303.impl.util.Constans;
import com.bbva.rbvd.lib.r303.impl.util.JsonHelper;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import org.springframework.web.client.RestClientException;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static java.util.Collections.singletonMap;

public class RBVDR303Impl extends RBVDR303Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR303Impl.class);

	@Override
	public QuotationLifeBO executeQuotationRimac(final QuotationLifeBO easyesQuotationRimacRequest, final String rimacQuotation, final String traceId) {
		LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac START *****");

		//1° llamada
		String requestBody = getRequestBodyAsJsonFormat(easyesQuotationRimacRequest);

		LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac ***** Request body: {}", requestBody);

		//2° llamada
		final String key = "ideCotizacion";

		String uriFromConsole = this.applicationConfigurationService.
				getProperty(RBVDProperties.QUOTATION_EASYES_RIMAC_URI.getValue());

		String uri = uriFromConsole.replace(key, rimacQuotation);

		LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac ***** URI: {}", uri);

		SignatureAWS signature = this.pisdR014.executeSignatureConstruction(requestBody,
				HttpMethod.PATCH.toString(), uri, null, traceId);

		//3° llamada
		HttpHeaders headers = this.createHttpHeaders(signature);

		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

		try {
			ResponseEntity<QuotationLifeBO> responseEntity = this.externalApiConnector.exchange(
					RBVDProperties.QUOTATION_EASYES_RIMAC.getValue(), HttpMethod.PATCH, entity, QuotationLifeBO.class, singletonMap(key, rimacQuotation));
			QuotationLifeBO easyQuotationRimacResponse = responseEntity.getBody();
			LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac ***** Response body: {}", getRequestBodyAsJsonFormat(easyQuotationRimacResponse));
			LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac END *****");
			return easyQuotationRimacResponse;
		} catch (RestClientException exception) {
			ExceptionBusiness exceptionHandler = new ExceptionBusiness();
			exceptionHandler.handler(exception);
			LOGGER.info("***** RBVDR303Impl - executeEasyesQuotationRimac ***** Exception: {}", exception.getMessage());
			return null;
		}catch(TimeoutException ex){
			LOGGER.debug("***** RBVDR303Impl - executeEasyesQuotationRimac ***** TimeoutException: {}", ex.getMessage());
			throw new BusinessException(Constans.Error.BBVAE2 + Constans.Error.COD_008411, false, "Lo sentimos, el servicio de Rimac está tardando más de lo esperado. Por favor,  intentelo nuevamente en unos minutos.");
		}

	}



	@Override
	public CustomerListASO executeGetCustomerHost(String customerId) {
		LOGGER.info("***** PISDR008Impl - executeGetCustomerHost Start *****");
		PEWUResponse result = this.pbtqR002.executeSearchInHostByCustomerId(customerId);
		LOGGER.info("***** PISDR008Impl - executeGetCustomerHost  ***** Response Host: {}", result);
		if (Objects.isNull(result.getHostAdviceCode()) || result.getHostAdviceCode().isEmpty()) {
			CustomerListASO customerList = new CustomerListASO();
			/* section customer data */
			CustomerBO customer = new CustomerBO();
			customer.setCustomerId(result.getPemsalwu().getNroclie());
			customer.setFirstName(result.getPemsalwu().getNombres());
			customer.setLastName(result.getPemsalwu().getApellip());
			customer.setSecondLastName(result.getPemsalwu().getApellim());
			customer.setBirthData(new BirthDataBO());
			customer.getBirthData().setBirthDate(result.getPemsalwu().getFechan());
			customer.getBirthData().setCountry(new CountryBO());
			customer.getBirthData().getCountry().setId(result.getPemsalwu().getPaisn());
			customer.setGender(new GenderBO());
			customer.getGender().setId(result.getPemsalwu().getSexo().equals("M") ? "MALE" : "FEMALE");

			/* section identity document*/
			IdentityDocumentsBO identityDocumentsBO = new IdentityDocumentsBO();
			identityDocumentsBO.setDocumentNumber(result.getPemsalwu().getNdoi());
			identityDocumentsBO.setDocumentType(new DocumentTypeBO());

			/* map document type host ? yes*/
			switch (result.getPemsalwu().getTdoi()) {
				case "L":
					identityDocumentsBO.getDocumentType().setId("DNI");
					break;
				case "R":
					identityDocumentsBO.getDocumentType().setId("RUC");
					break;
				default:
					identityDocumentsBO.getDocumentType().setId(result.getPemsalwu().getTdoi());
					break;
			}

			identityDocumentsBO.setExpirationDate(result.getPemsalwu().getFechav());
			customer.setIdentityDocuments(Collections.singletonList(identityDocumentsBO));
			/* section contact Details */
			List<ContactDetailsBO> contactDetailsBOList = new ArrayList<>();

			/* section contact PHONE_NUMBER */
			LOGGER.info("***** PISDR008Impl - executeGetCustomerHost  ***** Map getTipocon: {}", result.getPemsalwu().getTipocon());
			if (StringUtils.isNotEmpty(result.getPemsalwu().getContact())) {
				ContactDetailsBO contactDetailPhone = new ContactDetailsBO();
				contactDetailPhone.setContactDetailId(result.getPemsalwu().getIdencon());
				contactDetailPhone.setContact(result.getPemsalwu().getContact());
				contactDetailPhone.setContactType(new ContactTypeBO());
				contactDetailPhone.getContactType().setId("PHONE_NUMBER");
				contactDetailPhone.getContactType().setName(result.getPemsalw5().getDescmco());
				contactDetailsBOList.add(contactDetailPhone);
			}

			/* section contact2 type, validate MOBILE_NUMBER */
			LOGGER.info("***** PISDR008Impl - executeGetCustomerHost  ***** Map getTipoco2: {}", result.getPemsalwu().getTipoco2());
			if (StringUtils.isNotEmpty(result.getPemsalwu().getContac2())) {
				ContactDetailsBO contactDetailMobileNumber = new ContactDetailsBO();
				contactDetailMobileNumber.setContactDetailId(result.getPemsalwu().getIdenco2());
				contactDetailMobileNumber.setContact(result.getPemsalwu().getContac2());
				contactDetailMobileNumber.setContactType(new ContactTypeBO());
				contactDetailMobileNumber.getContactType().setId("MOBILE_NUMBER");
				contactDetailMobileNumber.getContactType().setName(result.getPemsalw5().getDescmc1());
				contactDetailsBOList.add(contactDetailMobileNumber);
			}

			/* section contact2 type, validate EMAIL */
			LOGGER.info("***** PISDR008Impl - executeGetCustomerHost  ***** Map getTipoco3: {}", result.getPemsalwu().getTipoco3());
			if (StringUtils.isNotEmpty(result.getPemsalwu().getContac3())) {
				ContactDetailsBO contactDetailEmail = new ContactDetailsBO();
				contactDetailEmail.setContactDetailId(result.getPemsalwu().getIdenco3());
				contactDetailEmail.setContact(result.getPemsalwu().getContac3());
				contactDetailEmail.setContactType(new ContactTypeBO());
				contactDetailEmail.getContactType().setId("EMAIL");
				contactDetailEmail.getContactType().setName(result.getPemsalw5().getDescmc2());
				contactDetailsBOList.add(contactDetailEmail);
			}

			customer.setContactDetails(contactDetailsBOList);
			/* section contact Details */

			/* section addresses */
			List<AddressesBO> addresses = new ArrayList<>();
			AddressesBO address = new AddressesBO();
			address.setAddressType(new AddressTypeBO());
			address.getAddressType().setId(result.getPemsalwu().getTipodir()); // map address type
			address.setResidenceStartDate(result.getPemsalwu().getFedocac());
			address.setAddressId(result.getPemsalwu().getCoddire());

			LocationBO location = new LocationBO();
			location.setCountry(new CountryBO());
			location.getCountry().setId(result.getPemsalwu().getPaisdom());
			location.setAdditionalInformation(result.getPemsalwu().getDetalle());

			List<GeographicGroupsBO> geographicGroups = new ArrayList<>();

			/* map geographicGroup ? */
			location.setGeographicGroups(geographicGroups);

			address.setLocation(location);
			addresses.add(address);
			customer.setAddresses(addresses);
			/* section addresses */

			customerList.setData(Collections.singletonList(customer));
			LOGGER.info("***** PISDR008Impl - executeGetCustomerHost End ***** ListCustomer: {}", customerList);
			return customerList;
		}
		this.addAdviceWithDescription(result.getHostAdviceCode(), result.getHostMessage());
		LOGGER.info("***** PISDR008Impl - executeGetCustomerHost ***** with error: {}", result.getHostMessage());
		return null;
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

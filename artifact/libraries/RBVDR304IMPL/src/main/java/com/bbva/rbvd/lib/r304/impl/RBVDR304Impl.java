package com.bbva.rbvd.lib.r304.impl;

import com.bbva.apx.exception.business.BusinessException;

import com.bbva.rbvd.dto.lifeinsrc.commons.InsurancePlanDTO;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;

import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;

import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;

import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;

import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.impl.dao.DAOService;

import com.bbva.rbvd.lib.r304.impl.util.MapperHelper;

import com.bbva.rbvd.lib.r304.impl.util.MockResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

import java.util.Map;

import static com.bbva.rbvd.lib.r304.impl.util.ValidationUtil.validateServicesResponse;

public class RBVDR304Impl extends RBVDR304Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR304Impl.class);

	private DAOService daoService;
	private MapperHelper mapperHelper;

	@Override
	public EasyesQuotationDTO executeBusinessLogicEasyesQutation(final EasyesQuotationDTO easyesQuotation) {
		LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQutation START *****");
		LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQutation ***** Param: {}", easyesQuotation);

		//el primer objeto de planes
		InsurancePlanDTO selectedPlan = easyesQuotation.getProduct().getPlans().get(0);

		//841
		final String productType = easyesQuotation.getProduct().getId();

		//plan: 01 o 02
		final String selectedPlanId = selectedPlan.getId();

		// periodicidad: MONTHLY, QUARTERLY, SEMIANNUAL, ANNUAL
		final String periodId = selectedPlan.getInstallmentPlans().get(0).getPeriod().getId();

		//frecuencia de pago convertida: M, T, S, A
		final String frequencyTypeId = this.applicationConfigurationService.getProperty(periodId);

		try {
			//query a la tabla de simulacion: simulation_id y expiracion simulacion
			final Map<String, Object> responseGetSimulationInformation = this.daoService.
					executeGetSimulationInformation(easyesQuotation.getExternalSimulationId());

			//query a la tabla de producto inner tabla modalidad:
			//codigo producto, nombre producto que envia a rimac, product desc,nombre plan,codigo plan rimac (5678456)
			final Map<String, Object> responseGetRequiredInformation = this.daoService.executeGetRequiredInformation(productType, selectedPlanId);

			//nombre frecuencia pago en español (MENSUAL, TRIMESTRAL, SEMESTRAL, ANUAL)
			final Map<String, Object> responsePaymentFrequencyName = this.daoService.executeGetPaymentFrequencyName(frequencyTypeId);

			//bean
			final EasyesQuotationDAO easyesQuotationDao = this.mapperHelper.
					createQuotationDao(responseGetSimulationInformation, responseGetRequiredInformation, responsePaymentFrequencyName);

			//crea el codigo de cotizacion
			final String policyQuotaInternalId = this.generatePolicyQuotaInternalId(easyesQuotationDao.getInsuranceSimulationId());

			//inserta en la trx
			easyesQuotation.setId(policyQuotaInternalId);

			EasyesQuotationBO rimacQuotationRequest = this.mapperHelper.createRimacQuotationRequest(easyesQuotationDao, policyQuotaInternalId);
			EasyesQuotationBO rimacQuotationResponse = null;
			if(easyesQuotation.getProduct().getId().equals("841")){
				if(this.applicationConfigurationService.getProperty("MOCK_SELECT_PLAN_DYNAMIC_LIFE").equals("S")){
					rimacQuotationResponse = new MockResponse().getMockResponseRimacService();
				}else{
					rimacQuotationResponse = this.rbvdR303.executeEasyesQuotationRimac(rimacQuotationRequest,
							easyesQuotation.getExternalSimulationId(), easyesQuotation.getTraceId());
				}
			}else{
				rimacQuotationResponse = this.rbvdR303.executeEasyesQuotationRimac(rimacQuotationRequest,
						easyesQuotation.getExternalSimulationId(), easyesQuotation.getTraceId());
			}

			validateServicesResponse(rimacQuotationResponse, RBVDErrors.COULDNT_SELECT_MODALITY_RIMAC_ERROR);

			//valida si existe el codigo cotizacion en la tabla de cotizacion
			final Map<String, Object> responseValidateQuotation = this.daoService.executeValidateQuotation(easyesQuotation.getId());
			final BigDecimal resultCount = (BigDecimal) responseValidateQuotation.get(RBVDProperties.FIELD_RESULT_NUMBER.getValue());

			//si es que ya existe la cotizacion, actualiza en la tabla de cotizacion_mod, si no existe la cotizacion la inserta en cotizacion y cotizacion_mod
			if(BigDecimal.ONE.compareTo(resultCount) == 0) {
				//actualiza modalidad, prima, oficina
				this.daoService.executeUpdateQuotationModQuery(easyesQuotationDao, easyesQuotation);
			} else {
				this.daoService.executeQuotationQuery(easyesQuotationDao, easyesQuotation);
				this.daoService.executeQuotationModQuery(easyesQuotationDao, easyesQuotation, rimacQuotationResponse);
			}

			this.mapperHelper.mappingOutputFields(easyesQuotation, easyesQuotationDao);

			LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQutation ***** Response: {}", easyesQuotation);
			LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQutation END *****");
			return easyesQuotation;
		} catch (BusinessException exception) {
			this.printErrorMessage(exception.getMessage());
			this.addAdvice(exception.getAdviceCode());
			return null;
		}
	}

	private String generatePolicyQuotaInternalId(final BigDecimal insuranceSimulationId) {
		final int requiredSize = 9;

		final StringBuilder policyQuotaInternalId = new StringBuilder("0814");

		final int sizeToComplete = requiredSize - insuranceSimulationId.toString().length();

		for(int i = 0; i < sizeToComplete; i++) {
			policyQuotaInternalId.append("0");
		}

		policyQuotaInternalId.append(insuranceSimulationId);
		return policyQuotaInternalId.toString();
	}

	private void printErrorMessage(final String message) {
		LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQutation ***** Something went wrong -> {}", message);
	}

	public void setDaoService(DAOService daoService) {this.daoService = daoService;}

	public void setMapperHelper(MapperHelper mapperHelper) {this.mapperHelper = mapperHelper;}

}

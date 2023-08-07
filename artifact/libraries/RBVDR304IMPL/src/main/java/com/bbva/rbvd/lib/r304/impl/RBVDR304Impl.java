package com.bbva.rbvd.lib.r304.impl;

import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.lib.r304.pattern.Quotation;
import com.bbva.rbvd.lib.r304.pattern.impl.QuotationEasyYes;
import com.bbva.rbvd.lib.r304.pattern.impl.QuotationParameter;
import com.bbva.rbvd.lib.r304.pattern.impl.QuotationStore;
import com.bbva.rbvd.lib.r304.pattern.impl.QuotationVidaDinamico;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RBVDR304Impl extends RBVDR304Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR304Impl.class);

	@Override
	public EasyesQuotationDTO executeBusinessLogicEasyesQutation(EasyesQuotationDTO input) {

			LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQutation  START *****");
			LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQutation  ***** {}", input);

			EasyesQuotationDTO response = new EasyesQuotationDTO();
			Quotation quotation;

			if(input.getProduct().getId().equals("840")){
				quotation = new QuotationEasyYes(
						new QuotationParameter(this.pisdR350, this.applicationConfigurationService)
						, new QuotationStore(this.pisdR350)
				);
				response = quotation.start(input, this.rbvdR303);
			}else if (input.getProduct().getId().equals("841")){
				quotation = new QuotationVidaDinamico(
						new QuotationParameter(this.pisdR350,this.applicationConfigurationService),
						new QuotationStore(this.pisdR350)
				);
				response = quotation.start(input, this.rbvdR303);
			}

			return response;


/*
		LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQutation START *****");
		LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQutation ***** Param: {}", input);

		InsurancePlanDTO selectedPlan = input.getProduct().getPlans().get(0);

		final String productType = input.getProduct().getId();
		final String selectedPlanId = selectedPlan.getId();
		final String periodId = selectedPlan.getInstallmentPlans().get(0).getPeriod().getId();
		final String frequencyTypeId = this.applicationConfigurationService.getProperty(periodId);

		try {
			final Map<String, Object> responseGetSimulationInformation = this.daoService.
					executeGetSimulationInformation(input.getExternalSimulationId());
			final Map<String, Object> responseGetRequiredInformation = this.daoService.executeGetRequiredInformation(productType, selectedPlanId);
			final Map<String, Object> responsePaymentFrequencyName = this.daoService.executeGetPaymentFrequencyName(frequencyTypeId);

			final EasyesQuotationDAO easyesQuotationDao = this.mapperHelper.
					createQuotationDao(responseGetSimulationInformation, responseGetRequiredInformation, responsePaymentFrequencyName);

			final String policyQuotaInternalId = this.generatePolicyQuotaInternalId(easyesQuotationDao.getInsuranceSimulationId());

			input.setId(policyQuotaInternalId);
			//config
//------------------------------------------------------------------------------------------------------------------------------------
			final EasyesQuotationBO rimacQuotationRequest = this.mapperHelper.createRimacQuotationRequest(easyesQuotationDao, policyQuotaInternalId);
			//-----------------------------un solo metodo-------------------------
			final EasyesQuotationBO rimacQuotationResponse = this.rbvdR303.executeEasyesQuotationRimac(rimacQuotationRequest,
					input.getExternalSimulationId(), input.getTraceId());

			validateServicesResponse(rimacQuotationResponse, RBVDErrors.COULDNT_SELECT_MODALITY_RIMAC_ERROR);
			//-----------------------------un solo metodo-------------------------
//----------------------------------------------post------------------------------------------------------------------------
			final Map<String, Object> responseValidateQuotation = this.daoService.executeValidateQuotation(input.getId());
			final BigDecimal resultCount = (BigDecimal) responseValidateQuotation.get(RBVDProperties.FIELD_RESULT_NUMBER.getValue());
			.
			 if(BigDecimal.ONE.compareTo(resultCount) == 0) {
				this.daoService.executeUpdateQuotationModQuery(easyesQuotationDao, input);
			} else {
				this.daoService.executeQuotationQuery(easyesQuotationDao, input);
				this.daoService.executeQuotationModQuery(easyesQuotationDao, input, rimacQuotationResponse);
			}

			this.mapperHelper.mappingOutputFields(input, easyesQuotationDao);

			LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQutation ***** Response: {}", input);
			LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQutation END *****");
			return input;
		} catch (BusinessException exception) {
			this.printErrorMessage(exception.getMessage());
			this.addAdvice(exception.getAdviceCode());
			return null;
		}
	}
*/
/*	private String generatePolicyQuotaInternalId(final BigDecimal insuranceSimulationId) {
		final int requiredSize = 9;

		final StringBuilder policyQuotaInternalId = new StringBuilder("0814");

		final int sizeToComplete = requiredSize - insuranceSimulationId.toString().length();

		for(int i = 0; i < sizeToComplete; i++) {
			policyQuotaInternalId.append("0");
		}

		policyQuotaInternalId.append(insuranceSimulationId);
		return policyQuotaInternalId.toString();
*/
	}

	/*private void printErrorMessage(final String message) {
		LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQutation ***** Something went wrong -> {}", message);
	}
*/
//	public void setDaoService(DAOService daoService) {this.daoService = daoService;}
//	public void setMapperHelper(MapperHelper mapperHelper) {this.mapperHelper = mapperHelper;}

}

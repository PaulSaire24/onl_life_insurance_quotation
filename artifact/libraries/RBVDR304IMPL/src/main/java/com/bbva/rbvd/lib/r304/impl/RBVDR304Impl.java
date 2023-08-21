package com.bbva.rbvd.lib.r304.impl;

import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.lib.r304.pattern.Quotation;
import com.bbva.rbvd.lib.r304.pattern.product.QuotationEasyYes;
import com.bbva.rbvd.lib.r304.pattern.impl.QuotationParameter;
import com.bbva.rbvd.lib.r304.pattern.impl.QuotationStore;
import com.bbva.rbvd.lib.r304.pattern.product.QuotationVidaDinamico;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RBVDR304Impl extends RBVDR304Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR304Impl.class);

	@Override
	public EasyesQuotationDTO executeBusinessLogicQuotation(EasyesQuotationDTO input) {

		LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQuotation  START *****");
		LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQuotation  ***** {}", input);

		EasyesQuotationDTO response = new EasyesQuotationDTO();
		Quotation quotation;

		if (input.getProduct().getId().equals("840")) {
			quotation = new QuotationEasyYes(
					new QuotationParameter(this.pisdR350, this.applicationConfigurationService)
					, new QuotationStore(this.pisdR350)
			);
			response = quotation.start(input, this.rbvdR303);
		} else if (input.getProduct().getId().equals("841")) {
			quotation = new QuotationVidaDinamico(
					new QuotationParameter(this.pisdR350, this.applicationConfigurationService),
					new QuotationStore(this.pisdR350)
			);
			response = quotation.start(input, this.rbvdR303);
		}

		return response;
	}
}

package com.bbva.rbvd.lib.r304.impl;

import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
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
	public QuotationLifeDTO executeBusinessLogicQuotation(QuotationLifeDTO input) {

		LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQuotation  START *****");
		LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQuotation  ***** {}", input);
		LOGGER.info("***** RBVDR304Impl - userAudit  ***** {}", input.getUserAudit());

		LOGGER.info("***** RBVDR304Impl - user  ***** {}", input.getCreationUser());
		QuotationLifeDTO response = new QuotationLifeDTO();
		Quotation quotation = null;

		if (input.getProduct().getId().equals("840")) {
			quotation = new QuotationEasyYes(
					new QuotationParameter(this.pisdR350, this.applicationConfigurationService,this.rbvdR303)
					, new QuotationStore(this.pisdR350)
			);
			response = quotation.start(input, this.rbvdR303,this.applicationConfigurationService);


		} else if (input.getProduct().getId().equals("841")) {
			quotation = new QuotationVidaDinamico(
					new QuotationParameter(this.pisdR350, this.applicationConfigurationService,this.rbvdR303),
					new QuotationStore(this.pisdR350)
			);
			response = quotation.start(input, this.rbvdR303,this.applicationConfigurationService);
		}

		return response;
	}
}

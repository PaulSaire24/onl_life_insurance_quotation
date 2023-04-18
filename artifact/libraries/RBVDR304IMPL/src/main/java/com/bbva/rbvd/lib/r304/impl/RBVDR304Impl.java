package com.bbva.rbvd.lib.r304.impl;

import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RBVDR304Impl extends RBVDR304Abstract {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDR304Impl.class);

	@Override
	public EasyesQuotationDTO executeBusinessLogicEasyesQutation(EasyesQuotationDTO easyesQuotation) {
		LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQutation START *****");
		LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQutation ***** Param: {}", easyesQuotation);
		LOGGER.info("***** RBVDR304Impl - executeBusinessLogicEasyesQutation END *****");
		return new EasyesQuotationDTO();
	}

}

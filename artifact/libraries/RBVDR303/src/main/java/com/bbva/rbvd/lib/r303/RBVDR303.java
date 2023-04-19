package com.bbva.rbvd.lib.r303;

import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;

public interface RBVDR303 {

	EasyesQuotationBO executeEasyesQuotationRimac(EasyesQuotationBO easyesQuotation, String rimacQuotation, String traceId);

}

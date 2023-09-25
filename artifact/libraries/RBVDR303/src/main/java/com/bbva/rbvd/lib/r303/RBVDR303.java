package com.bbva.rbvd.lib.r303;

import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.QuotationLifeBO;

public interface RBVDR303 {

	QuotationLifeBO executeQuotationRimac(QuotationLifeBO easyesQuotation, String rimacQuotation, String traceId);
	CustomerBO executeListCustomerService(String customerId);

}

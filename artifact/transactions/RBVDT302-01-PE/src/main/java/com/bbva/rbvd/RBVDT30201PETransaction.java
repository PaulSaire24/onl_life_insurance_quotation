package com.bbva.rbvd;

import com.bbva.elara.domain.transaction.Severity;
import com.bbva.elara.domain.transaction.response.HttpResponseCode;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;

import com.bbva.rbvd.lib.r304.RBVDR304;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Objects.nonNull;

public class RBVDT30201PETransaction extends AbstractRBVDT30201PETransaction {

	private static final Logger LOGGER = LoggerFactory.getLogger(RBVDT30201PETransaction.class);

	@Override
	public void execute() {
		LOGGER.info("RBVDT30201PETransaction - START");

		RBVDR304 rbvdR304 = this.getServiceLibrary(RBVDR304.class);

		EasyesQuotationDTO easyesQuotation = new EasyesQuotationDTO();
		easyesQuotation.setProduct(this.getProduct());
		easyesQuotation.setInsuredAmount(this.getInsuredamount());
		easyesQuotation.setHolder(this.getHolder());
		easyesQuotation.setIsDataTreatment(this.getIsdatatreatment());
		easyesQuotation.setExternalSimulationId(this.getExternalsimulationid());

		EasyesQuotationDTO response = rbvdR304.executeBusinessLogicEasyesQutation(easyesQuotation);

		if(nonNull(response)) {
			this.setId(response.getId());
			this.setProduct(response.getProduct());
			this.setInsuredamount(response.getInsuredAmount());
			this.setHolder(response.getHolder());
			this.setIsdatatreatment(response.getIsDataTreatment());
			this.setExternalsimulationid(response.getExternalSimulationId());

			this.setHttpResponseCode(HttpResponseCode.HTTP_CODE_200, Severity.OK);
		} else {
			this.setSeverity(Severity.ENR);
		}

	}

}

package com.bbva.rbvd.lib.r304.business;

import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;

public interface IInsrEasyYesBusiness {

    PayloadStore doEasyYes(PayloadConfig payloadConfig);
    EasyesQuotationDTO mappingOutputFieldsEasyes(PayloadStore payloadStore);
}

package com.bbva.rbvd.lib.r304.pattern;

import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;

public interface PreQuotation {
    PayloadConfig getConfig(EasyesQuotationDTO input);
}

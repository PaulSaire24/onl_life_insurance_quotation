package com.bbva.rbvd.lib.r304.pattern;


import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;

public interface PreQuotation {
    PayloadConfig getConfig(QuotationLifeDTO input);

}

package com.bbva.rbvd.lib.r304.pattern;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.RBVDR304;

public interface Quotation {
    EasyesQuotationDTO start(EasyesQuotationDTO input, RBVDR303 rbvdr303);
}

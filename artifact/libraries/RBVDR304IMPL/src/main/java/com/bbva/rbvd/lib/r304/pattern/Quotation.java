package com.bbva.rbvd.lib.r304.pattern;


import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.lib.r303.RBVDR303;

public interface Quotation {
    QuotationLifeDTO start(QuotationLifeDTO input, RBVDR303 rbvdr303, ApplicationConfigurationService applicationConfigurationService);
}

package com.bbva.rbvd.lib.r304.pattern;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;

public interface PostQuotation {
    void end(PayloadStore payloadStore, ApplicationConfigurationService applicationConfigurationService);
}

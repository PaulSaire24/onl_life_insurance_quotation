package com.bbva.rbvd.lib.r304.pattern;

import com.bbva.rbvd.lib.r304.pattern.impl.QuotationStore;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;

public interface PostQuotation {
    void end(PayloadStore payloadStore);
}

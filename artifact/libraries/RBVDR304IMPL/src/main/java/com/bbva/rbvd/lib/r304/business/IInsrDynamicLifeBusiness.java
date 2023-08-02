package com.bbva.rbvd.lib.r304.business;

import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;

public interface IInsrDynamicLifeBusiness {
    PayloadStore doDynamicLife(PayloadConfig payloadConfig);
}

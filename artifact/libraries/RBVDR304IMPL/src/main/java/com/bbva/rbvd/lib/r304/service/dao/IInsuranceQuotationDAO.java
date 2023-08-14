package com.bbva.rbvd.lib.r304.service.dao;

import com.bbva.rbvd.lib.r304.transfer.PayloadStore;

public interface IInsuranceQuotationDAO {
    void executeInsertQuotationQuery(PayloadStore payloadStore);
}

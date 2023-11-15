package com.bbva.rbvd.lib.r304.service.dao;

import com.bbva.rbvd.lib.r304.transfer.PayloadStore;

import java.util.Map;

public interface IInsuranceQuotationDAO {
    void deleteQuotationInsuredLife(String policyQuotaInternalId);
    void insertQuotationInsuredLife(Map<String, Object> argumentForSaveParticipant);
    void executeInsertQuotationQuery(PayloadStore payloadStore);
}

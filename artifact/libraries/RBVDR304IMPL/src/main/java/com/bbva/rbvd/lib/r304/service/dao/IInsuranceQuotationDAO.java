package com.bbva.rbvd.lib.r304.service.dao;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.lifeinsrc.dao.InsuredLifeDAO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;

import java.util.Map;

public interface IInsuranceQuotationDAO {
    void updateQuotationInsured(Map<String, Object> argumentForSaveParticipant);
    void insertQuotationInsured(Map<String, Object> argumentForSaveParticipant);
    void executeInsertQuotationQuery(PayloadStore payloadStore);
}

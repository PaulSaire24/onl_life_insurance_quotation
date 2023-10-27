package com.bbva.rbvd.lib.r304.service.dao;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.rbvd.dto.lifeinsrc.dao.InsuredLifeDAO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;

import java.util.Map;

public interface IInsuranceQuotationDAO {
    void updateSimulationParticipant(Map<String, Object> argumentForSaveParticipant);
    void insertSimulationParticipant(Map<String, Object> argumentForSaveParticipant);
    InsuredLifeDAO createQuotationParticipant(PayloadStore payloadStore, ApplicationConfigurationService applicationConfigurationService);
    void executeInsertQuotationQuery(PayloadStore payloadStore);
}

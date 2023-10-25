package com.bbva.rbvd.lib.r304.service.dao;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.rbvd.dto.lifeinsrc.dao.CommonsLifeDAO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;

import java.util.Map;

public interface IInsuranceQuotationDAO {
    void updateSimulationParticipant(Map<String, Object> argumentForSaveParticipant);
    void insertSimulationParticipant(Map<String, Object> argumentForSaveParticipant);
    CommonsLifeDAO createQuotationParticipant(PayloadStore payloadStore, ApplicationConfigurationService applicationConfigurationService);
    void executeInsertQuotationQuery(PayloadStore payloadStore);
}

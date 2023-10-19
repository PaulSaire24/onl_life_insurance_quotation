package com.bbva.rbvd.lib.r304.service.dao;

import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.rbvd.dto.lifeinsrc.dao.CommonsLifeDAO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;

import java.util.Map;

public interface IInsuranceQuotationDAO {
    void insertSimulationParticipant(Map<String, Object> argumentForSaveParticipant);
    CommonsLifeDAO createQuotationParticipant(PayloadStore payloadStore, CustomerListASO customerInformation);
    void executeInsertQuotationQuery(PayloadStore payloadStore);
}

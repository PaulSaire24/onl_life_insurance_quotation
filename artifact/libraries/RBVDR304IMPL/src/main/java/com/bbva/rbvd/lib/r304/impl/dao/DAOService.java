package com.bbva.rbvd.lib.r304.impl.dao;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;

import java.util.Map;

public interface DAOService {

    Map<String, Object> executeGetSimulationInformation(String externalSimulationId);
    Map<String, Object> executeGetRequiredInformation(String productType, String planId);
    Map<String, Object> executeGetPaymentFrequencyName(String frequencyTypeId);
    Map<String, Object> executeValidateQuotation(String policyQuotaInternalId);
    void executeUpdateQuotationModQuery(EasyesQuotationDAO easyesQuotationDAO, EasyesQuotationDTO easyesQuotationDTO);
    void executeQuotationQuery(EasyesQuotationDAO easyesQuotationDAO, EasyesQuotationDTO easyesQuotationDTO);
    void executeQuotationModQuery(EasyesQuotationDAO easyesQuotationDAO, EasyesQuotationDTO easyesQuotationDTO, EasyesQuotationBO easyesQuotationBO);

}

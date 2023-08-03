package com.bbva.rbvd.lib.r304.transform.map;

import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;

import java.util.HashMap;
import java.util.Map;

public class InsuranceQuotationMap {
    private InsuranceQuotationMap(){}
    public static Map<String, Object> createArgumentsQuotationDao(final InsuranceQuotationDAO insuranceQuotationDao) {
        final Map<String, Object> arguments = new HashMap<>();
        arguments.put(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(), insuranceQuotationDao.getPolicyQuotaInternalId());
        arguments.put(RBVDProperties.FIELD_INSURANCE_SIMULATION_ID.getValue(), insuranceQuotationDao.getInsuranceSimulationId());
        arguments.put(RBVDProperties.FIELD_INSURANCE_COMPANY_QUOTA_ID.getValue(), insuranceQuotationDao.getInsuranceCompanyQuotaId());
        arguments.put(RBVDProperties.FIELD_QUOTE_DATE.getValue(), insuranceQuotationDao.getQuoteDate());
        arguments.put(RBVDProperties.FIELD_POLICY_QUOTA_END_VALIDITY_DATE.getValue(), insuranceQuotationDao.getPolicyQuotaEndValidityDate());
        arguments.put(RBVDProperties.FIELD_CUSTOMER_ID.getValue(), insuranceQuotationDao.getCustomerId());
        arguments.put(RBVDProperties.FIELD_LAST_CHANGE_BRANCH_ID.getValue(), insuranceQuotationDao.getLastChangeBranchId());
        arguments.put(RBVDProperties.FIELD_SOURCE_BRANCH_ID.getValue(), insuranceQuotationDao.getSourceBranchId());
        arguments.put(RBVDProperties.FIELD_CREATION_USER_ID.getValue(), insuranceQuotationDao.getCreationUser());
        arguments.put(RBVDProperties.FIELD_USER_AUDIT_ID.getValue(), insuranceQuotationDao.getUserAudit());
        arguments.put(RBVDProperties.FIELD_PERSONAL_DOC_TYPE.getValue(), null);
        arguments.put(RBVDProperties.FIELD_PARTICIPANT_PERSONAL_ID.getValue(), insuranceQuotationDao.getParticipantPersonalId());
        return arguments;
    }
}

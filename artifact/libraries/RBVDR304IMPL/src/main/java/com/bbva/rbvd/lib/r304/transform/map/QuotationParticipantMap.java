package com.bbva.rbvd.lib.r304.transform.map;

import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.rbvd.dto.lifeinsrc.dao.CommonsLifeDAO;
import com.bbva.rbvd.dto.lifeinsrc.dao.SimulationParticipantDAO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class QuotationParticipantMap {
    private static final Logger LOGGER = LoggerFactory.getLogger(QuotationParticipantMap.class);
    private QuotationParticipantMap() {super();}
    public static Map<String, Object> createArgumentsForSaveParticipant(CommonsLifeDAO quotationParticipant){


        LOGGER.info("QuotationParticipantMap start - createArgumentsForSaveParticipant");
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(),quotationParticipant.getInsuranceSimulationId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue(),quotationParticipant.getInsuranceProductId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue(),quotationParticipant.getInsuranceModalityType());
        arguments.put(RBVDProperties.FIELD_INSURED_AMOUNT.getValue(),quotationParticipant.getInsuranceProductId());
        arguments.put(RBVDProperties.FIELD_CURRENCY_ID.getValue(),quotationParticipant.getCurrencyId());
        arguments.put(RBVDProperties.FIELD_PERIOD_TYPE.getValue(),quotationParticipant.getPeriodType());
        arguments.put(RBVDProperties.FIELD_PERIOD_NUMBER.getValue(),quotationParticipant.getPeriodNumber());
        arguments.put(RBVDProperties.FIELD_REFUND_PER.getValue(),quotationParticipant.getRefundPer());
        arguments.put(RBVDProperties.FIELD_TOTAL_RETURN_AMOUNT.getValue(),quotationParticipant.getTotalReturnAmount());
        arguments.put(RBVDProperties.FIELD_INSURED_ID.getValue(),quotationParticipant.getInsuranceModalityType());
        arguments.put(RBVDProperties.FIELD_CUSTOMER_DOCUMENT_TYPE.getValue(),quotationParticipant.getCustomerDocumentType());
        arguments.put(RBVDProperties.FIELD_PERSONAL_DOCUMENT_ID.getValue(),quotationParticipant.getPersonalDocumentId());
        arguments.put(RBVDProperties.FIELD_IS_BBVA_CUSTOMER_TYPE.getValue(),quotationParticipant.getIsBbvaCustomerType());
        arguments.put(RBVDProperties.FIELD_PHONE_ID.getValue(),quotationParticipant.getIsBbvaCustomerType());
        arguments.put(RBVDProperties.FIELD_CUSTOMER_ENTRY_DATE.getValue(),quotationParticipant.getCustomerDocumentType());
        arguments.put(RBVDProperties.FIELD_PARTICIPANT_ROLE_ID.getValue(),quotationParticipant.getParticipantRoleId());
        arguments.put(RBVDProperties.FIELD_INSURED_CUSTOMER_NAME.getValue(),quotationParticipant.getInsuredCustomerName());
        arguments.put(RBVDProperties.FIELD_CLIENT_LAST_NAME.getValue(),quotationParticipant.getClientLastName());
        arguments.put(RBVDProperties.FIELD_USER_EMAIL_PERSONAL_DESC.getValue(),quotationParticipant.getUserEmailPersonalDesc());
        arguments.put(RBVDProperties.FIELD_CUSTOMER_BIRTH_DATE.getValue(),quotationParticipant.getCustomerBirthDate());
        arguments.put(RBVDProperties.FIELD_CREATION_USER_ID.getValue(),quotationParticipant.getCreationUser());
        arguments.put(RBVDProperties.FIELD_USER_AUDIT_ID.getValue(),quotationParticipant.getUserAudit());
        arguments.put(RBVDProperties.FIELD_GENDER_ID.getValue(),quotationParticipant.getGenderId());
        LOGGER.info("QuotationParticipantMap end");
        return arguments;
    }

}

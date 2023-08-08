package com.bbva.rbvd.lib.r304.transform.bean;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

public class QuotationBean {
    private static final String RIMAC_PRODUCT_NAME = "PRODUCT_SHORT_DESC";

    private QuotationBean() {
    }

    public static EasyesQuotationDAO createQuotationDao(Map<String, Object> responseGetSimulationIdAndExpirationDate, Map<String, Object> responseGetRequiredInformation,
                                                        Map<String, Object> responseGetPaymentFrequencyName) {
        EasyesQuotationDAO quotationDao = new EasyesQuotationDAO();
        quotationDao.setInsuranceSimulationId((BigDecimal) responseGetSimulationIdAndExpirationDate.get(RBVDProperties.FIELD_INSURANCE_SIMULATION_ID.getValue()));
        quotationDao.setCustSimulationExpiredDate((Timestamp) responseGetSimulationIdAndExpirationDate.get(RBVDProperties.FIELD_CUST_SIMULATION_EXPIRED_DATE.getValue()));
        quotationDao.setInsuranceModalityName((String) responseGetRequiredInformation.get(RBVDProperties.FIELD_INSURANCE_MODALITY_NAME.getValue()));
        quotationDao.setInsuranceCompanyModalityId((String) responseGetRequiredInformation.get(RBVDProperties.FIELD_INSURANCE_COMPANY_MODALITY_ID.getValue()));
        quotationDao.setInsuranceProductId((BigDecimal) responseGetRequiredInformation.get(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue()));
        quotationDao.setInsuranceProductDescription((String) responseGetRequiredInformation.get(RBVDProperties.FIELD_INSURANCE_PRODUCT_DESC.getValue()));
        quotationDao.setInsuranceBusinessName((String) responseGetRequiredInformation.get(RIMAC_PRODUCT_NAME));
        quotationDao.setPaymentFrequencyName((String) responseGetPaymentFrequencyName.get(RBVDProperties.FIELD_PAYMENT_FREQUENCY_NAME.getValue()));
        return quotationDao;
    }
}

package com.bbva.rbvd.lib.r304.service.dao.impl;


import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationModDAO;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;

import com.bbva.rbvd.lib.r304.impl.util.ConstantUtils;
import com.bbva.rbvd.lib.r304.impl.util.JsonHelper;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceQuotationModDAO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import com.bbva.rbvd.lib.r304.transform.bean.InsuranceQuotationModBean;
import com.bbva.rbvd.lib.r304.transform.map.InsuranceQuotationModMap;
import com.bbva.rbvd.lib.r304.transform.map.QuotationLifeInternalMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static com.bbva.rbvd.lib.r304.impl.util.ValidationUtil.validateInsertionQueries;

public class InsuranceQuotationModDAOImpl implements IInsuranceQuotationModDAO {
    private final PISDR350 pisdR350;
    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceQuotationModDAOImpl.class);


    public InsuranceQuotationModDAOImpl(PISDR350 pisdR350) {
        this.pisdR350 = pisdR350;
    }

    @Override
    public void executeUpdateQuotationModQuery(EasyesQuotationDAO myQuotation, QuotationLifeDTO input) {
       InsuranceQuotationModDAO insuranceQuotationMod = InsuranceQuotationModBean.createUpdateQuotationModDao(myQuotation, input);
        Map<String, Object> argumentsUpdateQuotationMod = InsuranceQuotationModMap.createUpdateQuotationModArguments(insuranceQuotationMod);
        Integer updateQuotationModResult = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_UPDATE_QUOTATION_MOD.getValue(), argumentsUpdateQuotationMod);
        validateInsertionQueries(updateQuotationModResult, RBVDErrors.QUOTATION_MOD_UPDATE_WAS_WRONG);
    }
    @Override
    public void deleteQuotationInsuredMod(String policyQuotaInternalId) {
        Map<String, Object> argument = InsuranceQuotationModMap.createArgumentForDeleteQuotationMod(policyQuotaInternalId);
        this.pisdR350.executeInsertSingleRow(ConstantUtils.DELETE_QUOTATION_MOD,argument);
    }

    @Override
    public void executeInsertQuotationModQuery(PayloadStore payloadStore) {
        InsuranceQuotationModDAO insuranceQuotationModDao = InsuranceQuotationModBean.createQuotationModDao(payloadStore);
        Map<String, Object> argumentsQuotationModDao = InsuranceQuotationModMap.createArgumentsQuotationModDao(insuranceQuotationModDao);
        Integer quotationModResult = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_INSERT_INSURANCE_QUOTATION_MOD.getValue(), argumentsQuotationModDao);
        validateInsertionQueries(quotationModResult, RBVDErrors.QUOTATION_MOD_INSERTION_WAS_WRONG);
    }
}

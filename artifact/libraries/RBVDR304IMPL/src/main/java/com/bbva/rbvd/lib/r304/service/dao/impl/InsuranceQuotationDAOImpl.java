package com.bbva.rbvd.lib.r304.service.dao.impl;

import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationDAO;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDValidation;
import com.bbva.rbvd.lib.r304.impl.util.ValidationUtil;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceQuotationDAO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import com.bbva.rbvd.lib.r304.transform.bean.InsuranceQuotationBean;
import com.bbva.rbvd.lib.r304.transform.map.InsuranceQuotationMap;

import java.util.Map;



public class InsuranceQuotationDAOImpl implements IInsuranceQuotationDAO {

    private final PISDR350 pisdR350;

    public InsuranceQuotationDAOImpl(PISDR350 pisdR350) {
        this.pisdR350 = pisdR350;
    }


    @Override
    public void insertQuotationInsured(Map<String, Object> argumentForSaveParticipant) {
        int idNewSimulation = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_INSERT_INSURED_QUOTATION.getValue(),argumentForSaveParticipant);
        if(idNewSimulation != 1){
            throw RBVDValidation.build(RBVDErrors.QUOTATION_INSERTION_WAS_WRONG);
        }
    }
    @Override
    public void updateQuotationInsured(Map<String, Object> argumentForSaveParticipant) {
        int idNewSimulation = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_UPDATE_INSURED_QUOTATION.getValue(),argumentForSaveParticipant);
        if(idNewSimulation != 1){
            throw RBVDValidation.build(RBVDErrors.QUOTATION_INSERTION_WAS_WRONG);
        }
    }


    @Override
    public void executeInsertQuotationQuery(PayloadStore payloadStore) {
        InsuranceQuotationDAO insuranceQuotationDao = InsuranceQuotationBean.createInsuranceQuotationDAO(payloadStore.getMyQuotation(), payloadStore.getInput());
        Map<String, Object> argumentsQuotationDao = InsuranceQuotationMap.createArgumentsQuotationDao(insuranceQuotationDao);
        Integer quotationResult = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_INSERT_INSURANCE_QUOTATION.getValue(), argumentsQuotationDao);
        ValidationUtil.validateInsertionQueries(quotationResult, RBVDErrors.QUOTATION_INSERTION_WAS_WRONG);
    }
}

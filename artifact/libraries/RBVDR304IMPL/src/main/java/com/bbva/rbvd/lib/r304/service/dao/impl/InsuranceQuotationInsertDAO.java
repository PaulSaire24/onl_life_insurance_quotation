package com.bbva.rbvd.lib.r304.service.dao.impl;

import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationDAO;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceQuotationInsertDAO;
import com.bbva.rbvd.lib.r304.transform.bean.InsuranceQuotationBean;
import com.bbva.rbvd.lib.r304.transform.map.InsuranceQuotationMap;

import java.util.Map;

import static com.bbva.rbvd.lib.r304.impl.util.ValidationUtil.validateInsertionQueries;

public class InsuranceQuotationInsertDAO implements IInsuranceQuotationInsertDAO {
    private PISDR350 pisdR350;
    private InsuranceQuotationBean insuranceQuotationbean;
    private InsuranceQuotationMap insuranceQuotationmap;

    @Override
    public void executeQuotationQuery(EasyesQuotationDAO easyesQuotationDAO, EasyesQuotationDTO easyesQuotationDTO) {
        InsuranceQuotationDAO insuranceQuotationDao = this.insuranceQuotationbean.createInsuranceQuotationDAO(easyesQuotationDAO, easyesQuotationDTO);
        Map<String, Object> argumentsQuotationDao = this.insuranceQuotationmap.createArgumentsQuotationDao(insuranceQuotationDao);
        Integer quotationResult = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_INSERT_INSURANCE_QUOTATION.getValue(), argumentsQuotationDao);
        validateInsertionQueries(quotationResult, RBVDErrors.QUOTATION_INSERTION_WAS_WRONG);
    }
}

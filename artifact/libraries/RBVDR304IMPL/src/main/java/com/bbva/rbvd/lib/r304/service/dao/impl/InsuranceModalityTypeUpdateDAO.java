package com.bbva.rbvd.lib.r304.service.dao.impl;

import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationModDAO;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;

import com.bbva.rbvd.lib.r304.service.dao.IInsuranceModalityTypeUpdateDAO;
import com.bbva.rbvd.lib.r304.transform.bean.InsuranceQuotationModBean;
import com.bbva.rbvd.lib.r304.transform.map.InsuranceQuotationModMap;

import java.util.Map;

import static com.bbva.rbvd.lib.r304.impl.util.ValidationUtil.validateInsertionQueries;

public class InsuranceModalityTypeUpdateDAO implements IInsuranceModalityTypeUpdateDAO {
    private PISDR350 pisdR350;
    private InsuranceQuotationModBean insurancePlan;
    private InsuranceQuotationModMap quotationModifiArgument;
    @Override
    public void executeUpdateQuotationModQuery(EasyesQuotationDAO easyesQuotationDAO, EasyesQuotationDTO easyesQuotationDTO, Object rimacQuotationResponse) {
        InsuranceQuotationModDAO updateInsuranceQuotationModDao = this.insurancePlan.createUpdateQuotationModDao(easyesQuotationDAO, easyesQuotationDTO);
        Map<String, Object> argumentsUpdateQuotationMod = this.quotationModifiArgument.createUpdateQuotationModArguments(updateInsuranceQuotationModDao);
        Integer updateQuotationModResult = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_UPDATE_QUOTATION_MOD.getValue(), argumentsUpdateQuotationMod);

        validateInsertionQueries(updateQuotationModResult, RBVDErrors.QUOTATION_MOD_INSERTION_WAS_WRONG);
    }
}

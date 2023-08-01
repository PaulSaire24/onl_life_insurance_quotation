package com.bbva.rbvd.lib.r304.transform.bean;

import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationModDAO;
import com.bbva.rbvd.dto.lifeinsrc.commons.InsurancePlanDTO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;

public class InsuranceQuotationModBean {
    private InsuranceQuotationModBean() {}

    public InsuranceQuotationModDAO createUpdateQuotationModDao(final EasyesQuotationDAO easyesQuotationDAO, final EasyesQuotationDTO easyesQuotationDTO) {
        final InsurancePlanDTO plan = easyesQuotationDTO.getProduct().getPlans().get(0);

        final InsuranceQuotationModDAO insuranceQuotationModDao = new InsuranceQuotationModDAO();
        insuranceQuotationModDao.setPolicyQuotaInternalId(easyesQuotationDTO.getId());
        insuranceQuotationModDao.setInsuranceProductId(easyesQuotationDAO.getInsuranceProductId());
        insuranceQuotationModDao.setInsuranceModalityType(plan.getId());
        insuranceQuotationModDao.setPremiumAmount(plan.getInstallmentPlans().get(0).getPaymentAmount().getAmount());
        insuranceQuotationModDao.setLastChangeBranchId(easyesQuotationDTO.getBank().getBranch().getId());
        return insuranceQuotationModDao;
    }
}

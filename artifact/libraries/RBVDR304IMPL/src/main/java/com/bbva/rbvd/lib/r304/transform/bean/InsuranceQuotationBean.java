package com.bbva.rbvd.lib.r304.transform.bean;

import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InsuranceQuotationBean {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private InsuranceQuotationBean() {}

    public InsuranceQuotationDAO createInsuranceQuotationDAO(final EasyesQuotationDAO quotationDao, final EasyesQuotationDTO easyesQuotation) {
        final InsuranceQuotationDAO insuranceQuotationDAO = new InsuranceQuotationDAO();
        insuranceQuotationDAO.setPolicyQuotaInternalId(easyesQuotation.getId());
        insuranceQuotationDAO.setInsuranceSimulationId(quotationDao.getInsuranceSimulationId());
        insuranceQuotationDAO.setInsuranceCompanyQuotaId(easyesQuotation.getExternalSimulationId());

        insuranceQuotationDAO.setQuoteDate(this.dateFormat.format(new Date()));

        insuranceQuotationDAO.setPolicyQuotaEndValidityDate(quotationDao.getCustSimulationExpiredDate());
        insuranceQuotationDAO.setCustomerId(easyesQuotation.getHolder().getId());
        insuranceQuotationDAO.setLastChangeBranchId(easyesQuotation.getBank().getBranch().getId());
        insuranceQuotationDAO.setSourceBranchId(easyesQuotation.getBank().getBranch().getId());
        insuranceQuotationDAO.setCreationUser(easyesQuotation.getCreationUser());
        insuranceQuotationDAO.setUserAudit(easyesQuotation.getUserAudit());
        return insuranceQuotationDAO;
    }
}

package com.bbva.rbvd.lib.r304.service.dao;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;

public interface IInsuranceQuotationModDAO {
    void executeUpdateQuotationModQuery(EasyesQuotationDAO easyesQuotationDao, EasyesQuotationDTO quotation);
    void executeQuotationModQuery(EasyesQuotationDAO easyesQuotationDAO, EasyesQuotationDTO easyesQuotationDTO, EasyesQuotationBO easyesQuotationBO);
}

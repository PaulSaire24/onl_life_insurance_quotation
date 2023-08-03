package com.bbva.rbvd.lib.r304.service.dao;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;

public interface IInsuranceQuotationDAO {
    void executeQuotationQuery(EasyesQuotationDAO easyesQuotationDAO, EasyesQuotationDTO input);
}

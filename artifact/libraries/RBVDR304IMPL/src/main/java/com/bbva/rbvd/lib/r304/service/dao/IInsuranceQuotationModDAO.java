package com.bbva.rbvd.lib.r304.service.dao;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;

public interface IInsuranceQuotationModDAO {
    void executeUpdateQuotationModQuery(EasyesQuotationDAO easyesQuotationDao, QuotationLifeDTO quotation);
    void executeInsertQuotationModQuery(PayloadStore payloadStore);
}

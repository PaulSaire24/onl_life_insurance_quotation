package com.bbva.rbvd.lib.r304.service.dao;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;

public interface IInsuranceQuotationDAO {
    void executeQuotationQuery(PayloadStore payloadStore);
}

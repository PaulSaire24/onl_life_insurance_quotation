package com.bbva.rbvd.lib.r304.transform.bean;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.commons.PlanBO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationPayloadBO;

import static java.util.Collections.singletonList;

public class QuotationRimacBean {
    private QuotationRimacBean() {
    }

    public static EasyesQuotationBO createRimacQuotationRequest(final EasyesQuotationDAO input, final String policyQuotaInternalId) {
        EasyesQuotationBO easyesQuotationBO = new EasyesQuotationBO();

        EasyesQuotationPayloadBO payload = new EasyesQuotationPayloadBO();
        payload.setProducto(input.getInsuranceBusinessName());
        PlanBO selectedPlan = new PlanBO();
        selectedPlan.setPlan(Long.valueOf(input.getInsuranceCompanyModalityId()));
        payload.setPlanes(singletonList(selectedPlan));
        payload.setCodigoExterno(policyQuotaInternalId);

        easyesQuotationBO.setPayload(payload);
        return easyesQuotationBO;
    }
}

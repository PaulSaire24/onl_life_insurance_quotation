package com.bbva.rbvd.lib.r304.transform.bean;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationModDAO;
import com.bbva.rbvd.dto.lifeinsrc.commons.InstallmentsDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.InsurancePlanDTO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;
import java.util.Objects;

import static java.math.BigDecimal.valueOf;

public class InsuranceQuotationModBean {

    private InsuranceQuotationModBean() {}

    public static InsuranceQuotationModDAO createUpdateQuotationModDao(EasyesQuotationDAO easyesQuotationDAO, EasyesQuotationDTO easyesQuotationDTO) {
        final InsurancePlanDTO plan = easyesQuotationDTO.getProduct().getPlans().get(0);

        final InsuranceQuotationModDAO insuranceQuotationModDao = new InsuranceQuotationModDAO();
        insuranceQuotationModDao.setPolicyQuotaInternalId(easyesQuotationDTO.getId());
        insuranceQuotationModDao.setInsuranceProductId(easyesQuotationDAO.getInsuranceProductId());
        insuranceQuotationModDao.setInsuranceModalityType(plan.getId());
        insuranceQuotationModDao.setPremiumAmount(plan.getInstallmentPlans().get(0).getPaymentAmount().getAmount());
        insuranceQuotationModDao.setLastChangeBranchId(easyesQuotationDTO.getBank().getBranch().getId());
        return insuranceQuotationModDao;
    }

    public static InsuranceQuotationModDAO createQuotationModDao(PayloadStore payloadStore) {

        EasyesQuotationDAO quotationDao = payloadStore.getMyQuotation();
        EasyesQuotationDTO input = payloadStore.getInput();
        EasyesQuotationBO rimacResponse = payloadStore.getRimacResponse();
        String frequencyType = payloadStore.getFrequencyType();

        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        final InsurancePlanDTO plan = input.getProduct().getPlans().get(0);

        final com.bbva.pisd.dto.insurance.dao.InsuranceQuotationModDAO insuranceQuotationModDAO = new InsuranceQuotationModDAO();
        insuranceQuotationModDAO.setPolicyQuotaInternalId(input.getId());
        insuranceQuotationModDAO.setInsuranceProductId(quotationDao.getInsuranceProductId());
        insuranceQuotationModDAO.setInsuranceModalityType(plan.getId());
        insuranceQuotationModDAO.setSaleChannelId(input.getSaleChannelId());

        InstallmentsDTO installment = plan.getInstallmentPlans().get(0);

        insuranceQuotationModDAO.setPaymentTermNumber(valueOf(installment.getPaymentsTotalNumber()));

        insuranceQuotationModDAO.setPolicyPaymentFrequencyType(frequencyType);

        final String rimacFechaInicio = rimacResponse.getPayload().getDetalleCotizacion().get(0).getPlanes().get(0).
                getFinanciamientos().get(0).getFechaInicio();
        final String rimacFechaFin = rimacResponse.getPayload().getDetalleCotizacion().get(0).getPlanes().get(0).
                getFinanciamientos().get(0).getFechaFin();

        LocalDate localDateFechaInicio = new LocalDate(rimacFechaInicio);
        LocalDate localDateFechaFin = new LocalDate(rimacFechaFin);

        insuranceQuotationModDAO.setFinancingStartDate(dateFormat.format(localDateFechaInicio.toDateTimeAtStartOfDay().toDate()));
        insuranceQuotationModDAO.setFinancingEndDate(dateFormat.format(localDateFechaFin.toDateTimeAtStartOfDay().toDate()));

        insuranceQuotationModDAO.setPremiumAmount(installment.getPaymentAmount().getAmount());
        insuranceQuotationModDAO.setPremiumCurrencyId(installment.getPaymentAmount().getCurrency());

        insuranceQuotationModDAO.setSaveQuotationIndType(null);
        insuranceQuotationModDAO.setLastChangeBranchId(input.getBank().getBranch().getId());
        insuranceQuotationModDAO.setSourceBranchId(input.getBank().getBranch().getId());
        insuranceQuotationModDAO.setCreationUser(input.getCreationUser());
        insuranceQuotationModDAO.setUserAudit(input.getUserAudit());
        insuranceQuotationModDAO.setContactEmailDesc(null);
        insuranceQuotationModDAO.setCustomerPhoneDesc(null);
        insuranceQuotationModDAO.setDataTreatmentIndType(validateIsDataTreatment(input.getIsDataTreatment()));
        return insuranceQuotationModDAO;
    }

    private static String validateIsDataTreatment(Boolean isDataTreatment){
        if(Objects.nonNull(isDataTreatment)){
            return Boolean.TRUE.equals(isDataTreatment) ? "S" : "N";
        }else{
            return "N";
        }
    }

}

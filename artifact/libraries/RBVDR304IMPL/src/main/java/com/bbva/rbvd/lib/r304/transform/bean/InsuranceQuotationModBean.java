package com.bbva.rbvd.lib.r304.transform.bean;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationModDAO;
import com.bbva.rbvd.dto.lifeinsrc.commons.InstallmentsDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.InsurancePlanDTO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;
import org.joda.time.LocalDate;

import java.text.SimpleDateFormat;

import static java.math.BigDecimal.valueOf;

public class InsuranceQuotationModBean {
    private ApplicationConfigurationService applicationConfigurationService;
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private InsuranceQuotationModBean() {}

    public InsuranceQuotationModDAO createUpdateQuotationModDao(final EasyesQuotationDAO easyesQuotationDAO,
                                                                final EasyesQuotationDTO easyesQuotationDTO)
    {
        final InsurancePlanDTO plan = easyesQuotationDTO.getProduct().getPlans().get(0);

        final InsuranceQuotationModDAO insuranceQuotationModDao = new InsuranceQuotationModDAO();
        insuranceQuotationModDao.setPolicyQuotaInternalId(easyesQuotationDTO.getId());
        insuranceQuotationModDao.setInsuranceProductId(easyesQuotationDAO.getInsuranceProductId());
        insuranceQuotationModDao.setInsuranceModalityType(plan.getId());
        insuranceQuotationModDao.setPremiumAmount(plan.getInstallmentPlans().get(0).getPaymentAmount().getAmount());
        insuranceQuotationModDao.setLastChangeBranchId(easyesQuotationDTO.getBank().getBranch().getId());
        return insuranceQuotationModDao;
    }
    public InsuranceQuotationModDAO createQuotationModDao(final EasyesQuotationDAO quotationDao,
                                                          final EasyesQuotationDTO easyesQuotation,
                                                          final EasyesQuotationBO easyesQuotationBO)
    {

        final InsurancePlanDTO plan = easyesQuotation.getProduct().getPlans().get(0);

        final com.bbva.pisd.dto.insurance.dao.InsuranceQuotationModDAO insuranceQuotationModDAO = new InsuranceQuotationModDAO();
        insuranceQuotationModDAO.setPolicyQuotaInternalId(easyesQuotation.getId());
        insuranceQuotationModDAO.setInsuranceProductId(quotationDao.getInsuranceProductId());
        insuranceQuotationModDAO.setInsuranceModalityType(plan.getId());
        insuranceQuotationModDAO.setSaleChannelId(easyesQuotation.getSaleChannelId());

        InstallmentsDTO installment = plan.getInstallmentPlans().get(0);

        insuranceQuotationModDAO.setPaymentTermNumber(valueOf(installment.getPaymentsTotalNumber()));

        String paymentFrequency = this.applicationConfigurationService.getProperty(installment.getPeriod().getId());
        insuranceQuotationModDAO.setPolicyPaymentFrequencyType(paymentFrequency);

        final String rimacFechaInicio = easyesQuotationBO.getPayload().getDetalleCotizacion().get(0).getPlanes().get(0).
                getFinanciamientos().get(0).getFechaInicio();
        final String rimacFechaFin = easyesQuotationBO.getPayload().getDetalleCotizacion().get(0).getPlanes().get(0).
                getFinanciamientos().get(0).getFechaFin();

        LocalDate localDateFechaInicio = new LocalDate(rimacFechaInicio);
        LocalDate localDateFechaFin = new LocalDate(rimacFechaFin);

        insuranceQuotationModDAO.setFinancingStartDate(this.dateFormat.format(localDateFechaInicio.toDateTimeAtStartOfDay().toDate()));
        insuranceQuotationModDAO.setFinancingEndDate(this.dateFormat.format(localDateFechaFin.toDateTimeAtStartOfDay().toDate()));

        insuranceQuotationModDAO.setPremiumAmount(installment.getPaymentAmount().getAmount());
        insuranceQuotationModDAO.setPremiumCurrencyId(installment.getPaymentAmount().getCurrency());

        insuranceQuotationModDAO.setSaveQuotationIndType(null);
        insuranceQuotationModDAO.setLastChangeBranchId(easyesQuotation.getBank().getBranch().getId());
        insuranceQuotationModDAO.setSourceBranchId(easyesQuotation.getBank().getBranch().getId());
        insuranceQuotationModDAO.setCreationUser(easyesQuotation.getCreationUser());
        insuranceQuotationModDAO.setUserAudit(easyesQuotation.getUserAudit());
        insuranceQuotationModDAO.setContactEmailDesc(null);
        insuranceQuotationModDAO.setCustomerPhoneDesc(null);
        insuranceQuotationModDAO.setDataTreatmentIndType((Boolean.TRUE.equals(easyesQuotation.getIsDataTreatment()) ? "S" : "N"));
        return insuranceQuotationModDAO;
    }
}

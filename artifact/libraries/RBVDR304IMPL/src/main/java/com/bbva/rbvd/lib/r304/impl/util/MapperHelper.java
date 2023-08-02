package com.bbva.rbvd.lib.r304.impl.util;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;

import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;

import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationDAO;
import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationModDAO;

import com.bbva.rbvd.dto.lifeinsrc.commons.InstallmentsDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.InsurancePlanDTO;

import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;

import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;

import com.bbva.rbvd.dto.lifeinsrc.rimac.commons.PlanBO;

import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationPayloadBO;

import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;

import com.bbva.rbvd.lib.r303.RBVDR303;
import org.joda.time.LocalDate;

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.math.BigDecimal.valueOf;
import static java.util.Collections.singletonList;
import static java.util.Collections.singletonMap;
import static java.util.Objects.nonNull;

public class MapperHelper {

    private static final String RIMAC_PRODUCT_NAME = "PRODUCT_SHORT_DESC";

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    private ApplicationConfigurationService applicationConfigurationService;
    private RBVDR303 rbvdR303;

/*    public EasyesQuotationBO createRimacQuotationRequest(final EasyesQuotationDAO easyesQuotationDao, final String policyQuotaInternalId) {
        EasyesQuotationBO easyesQuotationBO = new EasyesQuotationBO();

        EasyesQuotationPayloadBO payload = new EasyesQuotationPayloadBO();
        payload.setProducto(easyesQuotationDao.getInsuranceBusinessName());
        PlanBO selectedPlan = new PlanBO();
        selectedPlan.setPlan(Long.valueOf(easyesQuotationDao.getInsuranceCompanyModalityId()));
        payload.setPlanes(singletonList(selectedPlan));
        payload.setCodigoExterno(policyQuotaInternalId);

            easyesQuotationBO.setPayload(payload);
        return easyesQuotationBO;
    }
*/
    /*public Map<String, Object> createArgumentForValidateQuotation(final String policyQuotaInternalId) {
        return singletonMap(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(), policyQuotaInternalId);
    }
*/
 /*   public InsuranceQuotationModDAO createUpdateQuotationModDao(final EasyesQuotationDAO easyesQuotationDAO, final EasyesQuotationDTO easyesQuotationDTO) {
        final InsurancePlanDTO plan = easyesQuotationDTO.getProduct().getPlans().get(0);

        final InsuranceQuotationModDAO insuranceQuotationModDao = new InsuranceQuotationModDAO();
        insuranceQuotationModDao.setPolicyQuotaInternalId(easyesQuotationDTO.getId());
        insuranceQuotationModDao.setInsuranceProductId(easyesQuotationDAO.getInsuranceProductId());
        insuranceQuotationModDao.setInsuranceModalityType(plan.getId());
        insuranceQuotationModDao.setPremiumAmount(plan.getInstallmentPlans().get(0).getPaymentAmount().getAmount());
        insuranceQuotationModDao.setLastChangeBranchId(easyesQuotationDTO.getBank().getBranch().getId());
        return insuranceQuotationModDao;
    }
*/
/*    public Map<String, Object> createUpdateQuotationModArguments(final InsuranceQuotationModDAO updateInsuranceQuotationModDao) {
        final Map<String, Object> arguments = new HashMap<>();
        arguments.put(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(), updateInsuranceQuotationModDao.getPolicyQuotaInternalId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue(), updateInsuranceQuotationModDao.getInsuranceProductId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue(), updateInsuranceQuotationModDao.getInsuranceModalityType());
        arguments.put(RBVDProperties.FIELD_PREMIUM_AMOUNT.getValue(), updateInsuranceQuotationModDao.getPremiumAmount());
        arguments.put(RBVDProperties.FIELD_LAST_CHANGE_BRANCH_ID.getValue(), updateInsuranceQuotationModDao.getLastChangeBranchId());
        return arguments;
    }
*/
/*    public EasyesQuotationDAO createQuotationDao(final Map<String, Object> responseGetSimulationIdAndExpirationDate, final Map<String, Object> responseGetRequiredInformation,
                                                  final Map<String, Object> responseGetPaymentFrequencyName) {
        EasyesQuotationDAO quotationDao = new EasyesQuotationDAO();
        quotationDao.setInsuranceSimulationId((BigDecimal) responseGetSimulationIdAndExpirationDate.get(RBVDProperties.FIELD_INSURANCE_SIMULATION_ID.getValue()));
        quotationDao.setCustSimulationExpiredDate((Timestamp) responseGetSimulationIdAndExpirationDate.get(RBVDProperties.FIELD_CUST_SIMULATION_EXPIRED_DATE.getValue()));
        quotationDao.setInsuranceModalityName((String) responseGetRequiredInformation.get(RBVDProperties.FIELD_INSURANCE_MODALITY_NAME.getValue()));
        quotationDao.setInsuranceCompanyModalityId((String) responseGetRequiredInformation.get(RBVDProperties.FIELD_INSURANCE_COMPANY_MODALITY_ID.getValue()));
        quotationDao.setInsuranceProductId((BigDecimal) responseGetRequiredInformation.get(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue()));
        quotationDao.setInsuranceProductDescription((String) responseGetRequiredInformation.get(RBVDProperties.FIELD_INSURANCE_PRODUCT_DESC.getValue()));
        quotationDao.setInsuranceBusinessName((String) responseGetRequiredInformation.get(RIMAC_PRODUCT_NAME));
        quotationDao.setPaymentFrequencyName((String) responseGetPaymentFrequencyName.get(RBVDProperties.FIELD_PAYMENT_FREQUENCY_NAME.getValue()));
        return quotationDao;
    }
*/
/*    public InsuranceQuotationDAO createInsuranceQuotationDAO(final EasyesQuotationDAO quotationDao, final EasyesQuotationDTO easyesQuotation) {
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
*/
/*    public Map<String, Object> createArgumentsQuotationDao(final InsuranceQuotationDAO insuranceQuotationDao) {
        final Map<String, Object> arguments = new HashMap<>();
        arguments.put(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(), insuranceQuotationDao.getPolicyQuotaInternalId());
        arguments.put(RBVDProperties.FIELD_INSURANCE_SIMULATION_ID.getValue(), insuranceQuotationDao.getInsuranceSimulationId());
        arguments.put(RBVDProperties.FIELD_INSURANCE_COMPANY_QUOTA_ID.getValue(), insuranceQuotationDao.getInsuranceCompanyQuotaId());
        arguments.put(RBVDProperties.FIELD_QUOTE_DATE.getValue(), insuranceQuotationDao.getQuoteDate());
        arguments.put(RBVDProperties.FIELD_POLICY_QUOTA_END_VALIDITY_DATE.getValue(), insuranceQuotationDao.getPolicyQuotaEndValidityDate());
        arguments.put(RBVDProperties.FIELD_CUSTOMER_ID.getValue(), insuranceQuotationDao.getCustomerId());
        arguments.put(RBVDProperties.FIELD_LAST_CHANGE_BRANCH_ID.getValue(), insuranceQuotationDao.getLastChangeBranchId());
        arguments.put(RBVDProperties.FIELD_SOURCE_BRANCH_ID.getValue(), insuranceQuotationDao.getSourceBranchId());
        arguments.put(RBVDProperties.FIELD_CREATION_USER_ID.getValue(), insuranceQuotationDao.getCreationUser());
        arguments.put(RBVDProperties.FIELD_USER_AUDIT_ID.getValue(), insuranceQuotationDao.getUserAudit());
        arguments.put(RBVDProperties.FIELD_PERSONAL_DOC_TYPE.getValue(), null);
        arguments.put(RBVDProperties.FIELD_PARTICIPANT_PERSONAL_ID.getValue(), insuranceQuotationDao.getParticipantPersonalId());
        return arguments;
    }
*/
/*    public InsuranceQuotationModDAO createQuotationModDao(final EasyesQuotationDAO quotationDao,
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
        insuranceQuotationModDAO.setDataTreatmentIndType((easyesQuotation.getIsDataTreatment() ? "S" : "N"));
        return insuranceQuotationModDAO;
    }
*/
/*    public Map<String, Object> createArgumentsQuotationModDao(final InsuranceQuotationModDAO insuranceQuotationModDao) {
        Map<String, Object> arguments = new HashMap<>();
        arguments.put(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue(), insuranceQuotationModDao.getPolicyQuotaInternalId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue(), insuranceQuotationModDao.getInsuranceProductId());
        arguments.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue(), insuranceQuotationModDao.getInsuranceModalityType());
        arguments.put(RBVDProperties.FIELD_SALE_CHANNEL_ID.getValue(), insuranceQuotationModDao.getSaleChannelId());
        arguments.put(RBVDProperties.FIELD_PAYMENT_TERM_NUMBER.getValue(), insuranceQuotationModDao.getPaymentTermNumber());
        arguments.put(RBVDProperties.FIELD_POLICY_PAYMENT_FREQUENCY_TYPE.getValue(), insuranceQuotationModDao.getPolicyPaymentFrequencyType());
        arguments.put(RBVDProperties.FIELD_FINANCING_START_DATE.getValue(), insuranceQuotationModDao.getFinancingStartDate());
        arguments.put(RBVDProperties.FIELD_FINANCING_END_DATE.getValue(), insuranceQuotationModDao.getFinancingEndDate());
        arguments.put(RBVDProperties.FIELD_PREMIUM_AMOUNT.getValue(), insuranceQuotationModDao.getPremiumAmount());
        arguments.put(RBVDProperties.FIELD_PREMIUM_CURRENCY_ID.getValue(), insuranceQuotationModDao.getPremiumCurrencyId());
        arguments.put(RBVDProperties.FIELD_SAVED_QUOTATION_IND_TYPE.getValue(), insuranceQuotationModDao.getSaveQuotationIndType());
        arguments.put(RBVDProperties.FIELD_LAST_CHANGE_BRANCH_ID.getValue(), insuranceQuotationModDao.getLastChangeBranchId());
        arguments.put(RBVDProperties.FIELD_SOURCE_BRANCH_ID.getValue(), insuranceQuotationModDao.getSourceBranchId());
        arguments.put(RBVDProperties.FIELD_CREATION_USER_ID.getValue(), insuranceQuotationModDao.getCreationUser());
        arguments.put(RBVDProperties.FIELD_USER_AUDIT_ID.getValue(), insuranceQuotationModDao.getUserAudit());
        arguments.put(RBVDProperties.FIELD_CONTACT_EMAIL_DESC.getValue(), insuranceQuotationModDao.getContactEmailDesc());
        arguments.put(RBVDProperties.FIELD_CUSTOMER_PHONE_DESC.getValue(), insuranceQuotationModDao.getCustomerPhoneDesc());
        arguments.put(RBVDProperties.FIELD_DATA_TREATMENT_IND_TYPE.getValue(), insuranceQuotationModDao.getDataTreatmentIndType());
        return arguments;
    }
*/
    public void mappingOutputFields(final EasyesQuotationDTO easyesQuotation, final EasyesQuotationDAO easyesQuotationDao) {

        final String defaultValue = "";

        CustomerBO customerInformation = this.rbvdR303.executeListCustomerService(easyesQuotation.getHolder().getId());

        if(nonNull(customerInformation)) {
            easyesQuotation.getHolder().setFirstName(customerInformation.getFirstName());
            easyesQuotation.getHolder().setLastName(customerInformation.getLastName());
            final String fullName = customerInformation.getFirstName().concat(" ").
                    concat(customerInformation.getLastName()).concat(" ").concat(customerInformation.getSecondLastName());
            easyesQuotation.getHolder().setFullName(fullName);
        } else {
            easyesQuotation.getHolder().setFirstName(defaultValue);
            easyesQuotation.getHolder().setLastName(defaultValue);
            easyesQuotation.getHolder().setFullName(defaultValue);
        }

        easyesQuotation.getProduct().setName(easyesQuotationDao.getInsuranceProductDescription());
        easyesQuotation.getProduct().getPlans().get(0).setName(easyesQuotationDao.getInsuranceModalityName());
        easyesQuotation.getProduct().getPlans().get(0).getInstallmentPlans().get(0).getPeriod()
                .setName(easyesQuotationDao.getPaymentFrequencyName());
    }

    public void setApplicationConfigurationService(ApplicationConfigurationService applicationConfigurationService) {this.applicationConfigurationService = applicationConfigurationService;}

    public void setRbvdR303(RBVDR303 rbvdR303) {this.rbvdR303 = rbvdR303;}

}

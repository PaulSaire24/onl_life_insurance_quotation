package com.bbva.rbvd.lib.r304.impl.util;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;

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

import java.math.BigDecimal;

import java.sql.Timestamp;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static java.math.BigDecimal.valueOf;
import static java.util.Collections.singletonList;

public class MapperHelper {

    private ApplicationConfigurationService applicationConfigurationService;

    public EasyesQuotationBO createRimacQuotationRequest(final EasyesQuotationDAO easyesQuotationDao, final String policyQuotaInternalId) {
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

    public EasyesQuotationDAO createQuotationDao(final Map<String, Object> responseGetSimulationIdAndExpirationDate, final Map<String, Object> responseGetRequiredInformation,
                                                  final Map<String, Object> responseGetPaymentFrequencyName) {
        EasyesQuotationDAO quotationDao = new EasyesQuotationDAO();
        quotationDao.setInsuranceSimulationId((BigDecimal) responseGetSimulationIdAndExpirationDate.get(RBVDProperties.FIELD_INSURANCE_SIMULATION_ID.getValue()));
        quotationDao.setCustSimulationExpiredDate((Timestamp) responseGetSimulationIdAndExpirationDate.get(RBVDProperties.FIELD_CUST_SIMULATION_EXPIRED_DATE.getValue()));
        quotationDao.setInsuranceModalityName((String) responseGetRequiredInformation.get(RBVDProperties.FIELD_INSURANCE_MODALITY_NAME.getValue()));
        quotationDao.setInsuranceCompanyModalityId((String) responseGetRequiredInformation.get(RBVDProperties.FIELD_INSURANCE_COMPANY_MODALITY_ID.getValue()));
        quotationDao.setInsuranceProductId((BigDecimal) responseGetRequiredInformation.get(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue()));
        quotationDao.setInsuranceProductDescription((String) responseGetRequiredInformation.get(RBVDProperties.FIELD_INSURANCE_PRODUCT_DESC.getValue()));
        quotationDao.setInsuranceBusinessName((String) responseGetRequiredInformation.get(RBVDProperties.FIELD_INSURANCE_BUSINESS_NAME.getValue()));
        quotationDao.setPaymentFrequencyName((String) responseGetPaymentFrequencyName.get(RBVDProperties.FIELD_PAYMENT_FREQUENCY_NAME.getValue()));
        return quotationDao;
    }

    public InsuranceQuotationDAO createInsuranceQuotationDAO(final EasyesQuotationDAO quotationDao, final EasyesQuotationDTO easyesQuotation) {
        InsuranceQuotationDAO insuranceQuotationDAO = new InsuranceQuotationDAO();
        insuranceQuotationDAO.setPolicyQuotaInternalId(easyesQuotation.getId());
        insuranceQuotationDAO.setInsuranceSimulationId(quotationDao.getInsuranceSimulationId());
        insuranceQuotationDAO.setInsuranceCompanyQuotaId(easyesQuotation.getExternalSimulationId());

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        insuranceQuotationDAO.setQuoteDate(dateFormat.format(new Date()));

        insuranceQuotationDAO.setPolicyQuotaEndValidityDate(quotationDao.getCustSimulationExpiredDate());
        insuranceQuotationDAO.setCustomerId(easyesQuotation.getHolder().getId());
        insuranceQuotationDAO.setLastChangeBranchId(easyesQuotation.getBank().getBranch().getId());
        insuranceQuotationDAO.setSourceBranchId(easyesQuotation.getBank().getBranch().getId());
        insuranceQuotationDAO.setCreationUser(easyesQuotation.getCreationUser());
        insuranceQuotationDAO.setUserAudit(easyesQuotation.getUserAudit());
        return insuranceQuotationDAO;
    }

    public Map<String, Object> createArgumentsQuotationDao(final InsuranceQuotationDAO insuranceQuotationDao) {
        Map<String, Object> arguments = new HashMap<>();
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
        arguments.put(RBVDProperties.FIELD_PERSONAL_DOC_TYPE.getValue(), null); //FALTA CAMBIAR
        arguments.put(RBVDProperties.FIELD_PARTICIPANT_PERSONAL_ID.getValue(), insuranceQuotationDao.getParticipantPersonalId());
        return arguments;
    }

    public InsuranceQuotationModDAO createQuotationModDao(final EasyesQuotationDAO quotationDao, final EasyesQuotationDTO easyesQuotation) {

        InsurancePlanDTO plan = easyesQuotation.getProduct().getPlans().get(0);

        com.bbva.pisd.dto.insurance.dao.InsuranceQuotationModDAO insuranceQuotationModDAO = new InsuranceQuotationModDAO();
        insuranceQuotationModDAO.setPolicyQuotaInternalId(easyesQuotation.getId());
        insuranceQuotationModDAO.setInsuranceProductId(quotationDao.getInsuranceProductId());
        insuranceQuotationModDAO.setInsuranceModalityType(plan.getId());
        insuranceQuotationModDAO.setSaleChannelId(easyesQuotation.getSaleChannelId());

        InstallmentsDTO installment = plan.getInstallmentPlans().get(0);

        insuranceQuotationModDAO.setPaymentTermNumber(valueOf(installment.getPaymentsTotalNumber()));

        String paymentFrequency = this.applicationConfigurationService.getProperty(installment.getPeriod().getId());
        insuranceQuotationModDAO.setPolicyPaymentFrequencyType(paymentFrequency);

        insuranceQuotationModDAO.setFinancingStartDate("01/01/2023"); //FALTA CAMBIAR
        insuranceQuotationModDAO.setFinancingEndDate("01/02/2023"); //FALTA CAMBIARQ

        insuranceQuotationModDAO.setPremiumAmount(installment.getPaymentAmount().getAmount());
        insuranceQuotationModDAO.setPremiumCurrencyId(installment.getPaymentAmount().getCurrency());

        insuranceQuotationModDAO.setSaveQuotationIndType(null); //FALTA CAMBIARQ
        insuranceQuotationModDAO.setLastChangeBranchId(easyesQuotation.getBank().getBranch().getId());
        insuranceQuotationModDAO.setSourceBranchId(easyesQuotation.getBank().getBranch().getId());
        insuranceQuotationModDAO.setCreationUser(easyesQuotation.getCreationUser());
        insuranceQuotationModDAO.setUserAudit(easyesQuotation.getUserAudit());
        insuranceQuotationModDAO.setContactEmailDesc(null);  //FALTA CAMBIARQ
        insuranceQuotationModDAO.setCustomerPhoneDesc(null);  //FALTA CAMBIARQ
        insuranceQuotationModDAO.setDataTreatmentIndType((easyesQuotation.getIsDataTreatment() ? "S" : "N"));
        return insuranceQuotationModDAO;
    }

    public Map<String, Object> createArgumentsQuotationModDao(final InsuranceQuotationModDAO insuranceQuotationModDao) {
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

    public void mappingOutputFields(final EasyesQuotationDTO easyesQuotation, final EasyesQuotationDAO easyesQuotationDao) {
        easyesQuotation.getHolder().setFirstName("Fulano"); //FALTA CAMBIAR!!!!!!!!
        easyesQuotation.getHolder().setLastName("Sultano"); //FALTA CAMBIAR!!!!!!!!
        easyesQuotation.getHolder().setFullName("Fulano Sultano"); //FALTA CAMBIAR!!!!!!!!
        easyesQuotation.getProduct().setName(easyesQuotationDao.getInsuranceProductDescription());
        easyesQuotation.getProduct().getPlans().get(0).setName(easyesQuotationDao.getInsuranceModalityName());
        //easyesQuotation.getProduct().getPlans().get(0).getTotalInstallment().getPeriod().setName();
        easyesQuotation.getProduct().getPlans().get(0).getInstallmentPlans().get(0).getPeriod()
                .setName(easyesQuotationDao.getPaymentFrequencyName());
    }

    public void setApplicationConfigurationService(ApplicationConfigurationService applicationConfigurationService) {this.applicationConfigurationService = applicationConfigurationService;}

}

package com.bbva.rbvd.lib.r304.util;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;

import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationDAO;

import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationModDAO;

import com.bbva.pisd.dto.insurance.mock.MockDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.InstallmentsDTO;

import com.bbva.rbvd.dto.lifeinsrc.commons.InsurancePlanDTO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;

import com.bbva.rbvd.dto.lifeinsrc.mock.MockData;
import com.bbva.rbvd.dto.lifeinsrc.quotation.EasyesQuotationDTO;

import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.EasyesQuotationBO;

import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.impl.util.MapperHelper;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static java.math.BigDecimal.valueOf;
import static java.util.Collections.singletonMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Matchers.anyString;

public class MapperHelperTest {

    private final MapperHelper mapperHelper = new MapperHelper();

    private ApplicationConfigurationService applicationConfigurationService;
    private RBVDR303 rbvdR303;

    private EasyesQuotationDAO easyesQuotationDao;
    private EasyesQuotationDTO easyesQuotationDto;

    private InsuranceQuotationDAO insuranceQuotationDao;

    private InsuranceQuotationModDAO insuranceQuotationModDao;

    @Before
    public void setUp() throws IOException {
        applicationConfigurationService = mock(ApplicationConfigurationService.class);
        this.mapperHelper.setApplicationConfigurationService(applicationConfigurationService);

        rbvdR303 = mock(RBVDR303.class);
        this.mapperHelper.setRbvdR303(rbvdR303);

        easyesQuotationDao = mock(EasyesQuotationDAO.class);

        when(easyesQuotationDao.getInsuranceSimulationId()).thenReturn(valueOf(432));
        when(easyesQuotationDao.getCustSimulationExpiredDate()).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(easyesQuotationDao.getInsuranceBusinessName()).thenReturn("EASYYES01");
        when(easyesQuotationDao.getInsuranceProductId()).thenReturn(valueOf(4));
        when(easyesQuotationDao.getInsuranceCompanyModalityId()).thenReturn("5441");
        when(easyesQuotationDao.getInsuranceProductDescription()).thenReturn("SEGURO DE VIDA YA!");
        when(easyesQuotationDao.getInsuranceModalityName()).thenReturn("PLAN BASICO");
        when(easyesQuotationDao.getPaymentFrequencyName()).thenReturn("Mensual");

        easyesQuotationDto = MockData.getInstance().getEasyesInsuranceQuotationRequest();
        easyesQuotationDto.setId("0814000004380");
        easyesQuotationDto.setCreationUser("creationUser");
        easyesQuotationDto.setUserAudit("userAudit");
        easyesQuotationDto.setSaleChannelId("BI");
        easyesQuotationDto.setIsDataTreatment(true);

        insuranceQuotationDao = mock(InsuranceQuotationDAO.class);

        when(insuranceQuotationDao.getPolicyQuotaInternalId()).thenReturn("0814000004321");
        when(insuranceQuotationDao.getInsuranceSimulationId()).thenReturn(valueOf(432));
        when(insuranceQuotationDao.getInsuranceCompanyQuotaId()).thenReturn("ed2507ae-2ed4-471f-b023-9ced9626574a");
        when(insuranceQuotationDao.getQuoteDate()).thenReturn("02/05/2023");
        when(insuranceQuotationDao.getPolicyQuotaEndValidityDate()).thenReturn(new Timestamp(System.currentTimeMillis()));
        when(insuranceQuotationDao.getCustomerId()).thenReturn("11122288");
        when(insuranceQuotationDao.getLastChangeBranchId()).thenReturn("lastBranchId");
        when(insuranceQuotationDao.getSourceBranchId()).thenReturn("sourceBranchId");
        when(insuranceQuotationDao.getCreationUser()).thenReturn("creationUser");
        when(insuranceQuotationDao.getUserAudit()).thenReturn("userAudit");
        when(insuranceQuotationDao.getParticipantPersonalId()).thenReturn("participantPersonalId");

        insuranceQuotationModDao = mock(InsuranceQuotationModDAO.class);

        when(insuranceQuotationModDao.getPolicyQuotaInternalId()).thenReturn("0814000004321");
        when(insuranceQuotationModDao.getInsuranceProductId()).thenReturn(valueOf(4));
        when(insuranceQuotationModDao.getInsuranceModalityType()).thenReturn("02");
        when(insuranceQuotationModDao.getSaleChannelId()).thenReturn("BI");
        when(insuranceQuotationModDao.getPaymentTermNumber()).thenReturn(valueOf(12));
        when(insuranceQuotationModDao.getPolicyPaymentFrequencyType()).thenReturn("Mensual");
        when(insuranceQuotationModDao.getFinancingStartDate()).thenReturn("02/05/2023");
        when(insuranceQuotationModDao.getFinancingEndDate()).thenReturn("02/06/2023");
        when(insuranceQuotationModDao.getPremiumAmount()).thenReturn(valueOf(288.0));
        when(insuranceQuotationModDao.getPremiumCurrencyId()).thenReturn("PEN");
        when(insuranceQuotationModDao.getSaveQuotationIndType()).thenReturn("S");
        when(insuranceQuotationModDao.getLastChangeBranchId()).thenReturn("lastChangeBranchId");
        when(insuranceQuotationModDao.getSourceBranchId()).thenReturn("sourceBranchId");
        when(insuranceQuotationModDao.getCreationUser()).thenReturn("creationUser");
        when(insuranceQuotationModDao.getUserAudit()).thenReturn("userAudit");
        when(insuranceQuotationModDao.getContactEmailDesc()).thenReturn("contactEmailDesc");
        when(insuranceQuotationModDao.getCustomerPhoneDesc()).thenReturn("customerPhoneDesc");
        when(insuranceQuotationModDao.getDataTreatmentIndType()).thenReturn("S");
    }

    @Test
    public void createRimacQuotationRequest_OK() {
        final String policyQuotaInternalId = "0814000004321";

        EasyesQuotationBO validation = this.mapperHelper.createRimacQuotationRequest(easyesQuotationDao, policyQuotaInternalId);

        assertNotNull(validation.getPayload().getProducto());
        assertNotNull(validation.getPayload().getPlanes());
        assertNotNull(validation.getPayload().getPlanes().get(0));
        assertNotNull(validation.getPayload().getPlanes().get(0).getPlan());
        assertNotNull(validation.getPayload().getCodigoExterno());

        assertEquals(easyesQuotationDao.getInsuranceBusinessName(), validation.getPayload().getProducto());
        assertEquals(easyesQuotationDao.getInsuranceCompanyModalityId(), validation.getPayload().getPlanes().get(0).getPlan().toString());
        assertEquals(policyQuotaInternalId, validation.getPayload().getCodigoExterno());
    }

    @Test
    public void createQuotationDao_OK() {

        Map<String, Object> responseGetSimulationIdAndExpirationDate = new HashMap<>();
        responseGetSimulationIdAndExpirationDate.put(RBVDProperties.FIELD_INSURANCE_SIMULATION_ID.getValue(), valueOf(432));
        responseGetSimulationIdAndExpirationDate.put(RBVDProperties.FIELD_CUST_SIMULATION_EXPIRED_DATE.getValue(),
                new Timestamp(System.currentTimeMillis()));

        Map<String, Object> responseGetRequiredInformation = new HashMap<>();
        responseGetRequiredInformation.put(RBVDProperties.FIELD_INSURANCE_MODALITY_NAME.getValue(), "PLAN BASICO");
        responseGetRequiredInformation.put(RBVDProperties.FIELD_INSURANCE_COMPANY_MODALITY_ID.getValue(), "54421");
        responseGetRequiredInformation.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue(), valueOf(4));
        responseGetRequiredInformation.put(RBVDProperties.FIELD_INSURANCE_PRODUCT_DESC.getValue(), "SEGURO DE VIDA EASY-YES");
        responseGetRequiredInformation.put(RBVDProperties.FIELD_INSURANCE_BUSINESS_NAME.getValue(), "EASYYES01");

        Map<String, Object> responseGetPaymentFrequencyName = singletonMap(RBVDProperties.FIELD_PAYMENT_FREQUENCY_NAME.getValue(), "Mensual");

        EasyesQuotationDAO validation = this.mapperHelper.createQuotationDao(responseGetSimulationIdAndExpirationDate,
                responseGetRequiredInformation, responseGetPaymentFrequencyName);

        assertNotNull(validation);
        assertNotNull(validation.getInsuranceSimulationId());
        assertNotNull(validation.getCustSimulationExpiredDate());
        assertNotNull(validation.getInsuranceModalityName());
        assertNotNull(validation.getInsuranceCompanyModalityId());
        assertNotNull(validation.getInsuranceProductId());
        assertNotNull(validation.getInsuranceProductDescription());
        assertNotNull(validation.getInsuranceBusinessName());
        assertNotNull(validation.getPaymentFrequencyName());

        assertEquals(responseGetSimulationIdAndExpirationDate.get(RBVDProperties.FIELD_INSURANCE_SIMULATION_ID.getValue()),
                validation.getInsuranceSimulationId());
        assertEquals(responseGetRequiredInformation.get(RBVDProperties.FIELD_INSURANCE_MODALITY_NAME.getValue()),
                validation.getInsuranceModalityName());
        assertEquals(responseGetRequiredInformation.get(RBVDProperties.FIELD_INSURANCE_COMPANY_MODALITY_ID.getValue()),
                validation.getInsuranceCompanyModalityId());
        assertEquals(responseGetRequiredInformation.get(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue()),
                validation.getInsuranceProductId());
        assertEquals(responseGetRequiredInformation.get(RBVDProperties.FIELD_INSURANCE_PRODUCT_DESC.getValue()),
                validation.getInsuranceProductDescription());
        assertEquals(responseGetRequiredInformation.get(RBVDProperties.FIELD_INSURANCE_BUSINESS_NAME.getValue()),
                validation.getInsuranceBusinessName());
        assertEquals(responseGetPaymentFrequencyName.get(RBVDProperties.FIELD_PAYMENT_FREQUENCY_NAME.getValue()),
                validation.getPaymentFrequencyName());
    }

    @Test
    public void createInsuranceQuotationDAO_OK() {
        InsuranceQuotationDAO validation = this.mapperHelper.createInsuranceQuotationDAO(easyesQuotationDao, easyesQuotationDto);

        assertNotNull(validation.getPolicyQuotaInternalId());
        assertNotNull(validation.getInsuranceSimulationId());
        assertNotNull(validation.getInsuranceCompanyQuotaId());
        assertNotNull(validation.getQuoteDate());
        assertNotNull(validation.getPolicyQuotaEndValidityDate());
        assertNotNull(validation.getCustomerId());
        assertNotNull(validation.getLastChangeBranchId());
        assertNotNull(validation.getSourceBranchId());
        assertNotNull(validation.getCreationUser());
        assertNotNull(validation.getUserAudit());

        assertEquals(easyesQuotationDto.getId(), validation.getPolicyQuotaInternalId());
        assertEquals(easyesQuotationDao.getInsuranceSimulationId(), validation.getInsuranceSimulationId());
        assertEquals(easyesQuotationDto.getExternalSimulationId(), validation.getInsuranceCompanyQuotaId());
        assertEquals(easyesQuotationDto.getHolder().getId(), validation.getCustomerId());
        assertEquals(easyesQuotationDto.getBank().getBranch().getId(), validation.getLastChangeBranchId());
        assertEquals(easyesQuotationDto.getBank().getBranch().getId(), validation.getSourceBranchId());
        assertEquals(easyesQuotationDto.getCreationUser(), validation.getCreationUser());
        assertEquals(easyesQuotationDto.getUserAudit(), validation.getUserAudit());
    }

    @Test
    public void createArgumentsQuotationDao_OK() {
        Map<String, Object> validation = this.mapperHelper.createArgumentsQuotationDao(insuranceQuotationDao);

        assertNotNull(validation.get(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_INSURANCE_SIMULATION_ID.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_INSURANCE_COMPANY_QUOTA_ID.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_QUOTE_DATE.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_POLICY_QUOTA_END_VALIDITY_DATE.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_CUSTOMER_ID.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_LAST_CHANGE_BRANCH_ID.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_SOURCE_BRANCH_ID.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_CREATION_USER_ID.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_USER_AUDIT_ID.getValue()));
        assertNull(validation.get(RBVDProperties.FIELD_PERSONAL_DOC_TYPE.getValue())); //VALIDAAAAAAAR!!!
        assertNotNull(validation.get(RBVDProperties.FIELD_PARTICIPANT_PERSONAL_ID.getValue()));

        assertEquals(insuranceQuotationDao.getPolicyQuotaInternalId(), validation.get(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue()));
        assertEquals(insuranceQuotationDao.getInsuranceSimulationId(), validation.get(RBVDProperties.FIELD_INSURANCE_SIMULATION_ID.getValue()));
        assertEquals(insuranceQuotationDao.getInsuranceCompanyQuotaId(), validation.get(RBVDProperties.FIELD_INSURANCE_COMPANY_QUOTA_ID.getValue()));
        assertEquals(insuranceQuotationDao.getQuoteDate(), validation.get(RBVDProperties.FIELD_QUOTE_DATE.getValue()));
        assertEquals(insuranceQuotationDao.getPolicyQuotaEndValidityDate(), validation.get(RBVDProperties.FIELD_POLICY_QUOTA_END_VALIDITY_DATE.getValue()));
        assertEquals(insuranceQuotationDao.getCustomerId(), validation.get(RBVDProperties.FIELD_CUSTOMER_ID.getValue()));
        assertEquals(insuranceQuotationDao.getLastChangeBranchId(), validation.get(RBVDProperties.FIELD_LAST_CHANGE_BRANCH_ID.getValue()));
        assertEquals(insuranceQuotationDao.getSourceBranchId(), validation.get(RBVDProperties.FIELD_SOURCE_BRANCH_ID.getValue()));
        assertEquals(insuranceQuotationDao.getCreationUser(), validation.get(RBVDProperties.FIELD_CREATION_USER_ID.getValue()));
        assertEquals(insuranceQuotationDao.getUserAudit(), validation.get(RBVDProperties.FIELD_USER_AUDIT_ID.getValue()));
        assertEquals(insuranceQuotationDao.getParticipantPersonalId(), validation.get(RBVDProperties.FIELD_PARTICIPANT_PERSONAL_ID.getValue()));
    }

    @Test
    public void createQuotationModDao_OK() throws IOException {
        when(this.applicationConfigurationService.getProperty("MONTHLY")).thenReturn("M");

        EasyesQuotationBO rimacResponse = MockData.getInstance().getInsuranceRimacQuotationResponse();

        InsuranceQuotationModDAO validation = this.mapperHelper.createQuotationModDao(easyesQuotationDao, easyesQuotationDto, rimacResponse);

        assertNotNull(validation.getPolicyQuotaInternalId());
        assertNotNull(validation.getInsuranceProductId());
        assertNotNull(validation.getInsuranceModalityType());
        assertNotNull(validation.getSaleChannelId());
        assertNotNull(validation.getPaymentTermNumber());
        assertNotNull(validation.getPolicyPaymentFrequencyType());
        assertNotNull(validation.getFinancingStartDate());
        assertNotNull(validation.getFinancingEndDate());
        assertNotNull(validation.getPremiumAmount());
        assertNotNull(validation.getPremiumCurrencyId());
        //assertNotNull(validation.getSaveQuotationIndType());
        assertNotNull(validation.getLastChangeBranchId());
        assertNotNull(validation.getSourceBranchId());
        assertNotNull(validation.getCreationUser());
        assertNotNull(validation.getUserAudit());
        //assertNotNull(validation.getContactEmailDesc());
        //assertNotNull(validation.getCustomerPhoneDesc());
        assertNotNull(validation.getDataTreatmentIndType());

        InsurancePlanDTO plan = easyesQuotationDto.getProduct().getPlans().get(0);

        assertEquals(easyesQuotationDto.getId(), validation.getPolicyQuotaInternalId());
        assertEquals(easyesQuotationDao.getInsuranceProductId(), validation.getInsuranceProductId());
        assertEquals(plan.getId(), validation.getInsuranceModalityType());
        assertEquals(easyesQuotationDto.getSaleChannelId(), validation.getSaleChannelId());

        InstallmentsDTO installment = plan.getInstallmentPlans().get(0);

        assertEquals(valueOf(installment.getPaymentsTotalNumber()), validation.getPaymentTermNumber());
        assertEquals("M", validation.getPolicyPaymentFrequencyType());
        assertEquals("17/04/2023", validation.getFinancingStartDate());
        assertEquals("17/04/2024", validation.getFinancingEndDate());
        assertEquals(installment.getPaymentAmount().getAmount(), validation.getPremiumAmount());
        assertEquals(installment.getPaymentAmount().getCurrency(), validation.getPremiumCurrencyId());
        assertEquals(easyesQuotationDto.getBank().getBranch().getId(), validation.getLastChangeBranchId());
        assertEquals(easyesQuotationDto.getBank().getBranch().getId(), validation.getSourceBranchId());
        assertEquals(easyesQuotationDto.getCreationUser(), validation.getCreationUser());
        assertEquals(easyesQuotationDto.getUserAudit(), validation.getUserAudit());
        assertEquals("S", validation.getDataTreatmentIndType());

        easyesQuotationDto.setIsDataTreatment(false);

        validation = this.mapperHelper.createQuotationModDao(easyesQuotationDao, easyesQuotationDto, rimacResponse);

        assertEquals("N", validation.getDataTreatmentIndType());

    }

    @Test
    public void createArgumentsQuotationModDao_OK() {
        Map<String, Object> validation = this.mapperHelper.createArgumentsQuotationModDao(insuranceQuotationModDao);

        assertNotNull(validation.get(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_SALE_CHANNEL_ID.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_PAYMENT_TERM_NUMBER.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_POLICY_PAYMENT_FREQUENCY_TYPE.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_FINANCING_START_DATE.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_FINANCING_END_DATE.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_PREMIUM_AMOUNT.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_PREMIUM_CURRENCY_ID.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_SAVED_QUOTATION_IND_TYPE.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_LAST_CHANGE_BRANCH_ID.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_SOURCE_BRANCH_ID.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_CREATION_USER_ID.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_USER_AUDIT_ID.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_CONTACT_EMAIL_DESC.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_CUSTOMER_PHONE_DESC.getValue()));
        assertNotNull(validation.get(RBVDProperties.FIELD_DATA_TREATMENT_IND_TYPE.getValue()));

        assertEquals(insuranceQuotationModDao.getPolicyQuotaInternalId(), validation.get(RBVDProperties.FIELD_POLICY_QUOTA_INTERNAL_ID.getValue()));
        assertEquals(insuranceQuotationModDao.getInsuranceProductId(), validation.get(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue()));
        assertEquals(insuranceQuotationModDao.getInsuranceModalityType(), validation.get(RBVDProperties.FIELD_OR_FILTER_INSURANCE_MODALITY_TYPE.getValue()));
        assertEquals(insuranceQuotationModDao.getSaleChannelId(), validation.get(RBVDProperties.FIELD_SALE_CHANNEL_ID.getValue()));
        assertEquals(insuranceQuotationModDao.getPaymentTermNumber(), validation.get(RBVDProperties.FIELD_PAYMENT_TERM_NUMBER.getValue()));
        assertEquals(insuranceQuotationModDao.getPolicyPaymentFrequencyType(), validation.get(RBVDProperties.FIELD_POLICY_PAYMENT_FREQUENCY_TYPE.getValue()));
        assertEquals(insuranceQuotationModDao.getFinancingStartDate(), validation.get(RBVDProperties.FIELD_FINANCING_START_DATE.getValue()));
        assertEquals(insuranceQuotationModDao.getFinancingEndDate(), validation.get(RBVDProperties.FIELD_FINANCING_END_DATE.getValue()));
        assertEquals(insuranceQuotationModDao.getPremiumAmount(), validation.get(RBVDProperties.FIELD_PREMIUM_AMOUNT.getValue()));
        assertEquals(insuranceQuotationModDao.getPremiumCurrencyId(), validation.get(RBVDProperties.FIELD_PREMIUM_CURRENCY_ID.getValue()));
        assertEquals(insuranceQuotationModDao.getSaveQuotationIndType(), validation.get(RBVDProperties.FIELD_SAVED_QUOTATION_IND_TYPE.getValue()));
        assertEquals(insuranceQuotationModDao.getLastChangeBranchId(), validation.get(RBVDProperties.FIELD_LAST_CHANGE_BRANCH_ID.getValue()));
        assertEquals(insuranceQuotationModDao.getSourceBranchId(), validation.get(RBVDProperties.FIELD_SOURCE_BRANCH_ID.getValue()));
        assertEquals(insuranceQuotationModDao.getCreationUser(), validation.get(RBVDProperties.FIELD_CREATION_USER_ID.getValue()));
        assertEquals(insuranceQuotationModDao.getUserAudit(), validation.get(RBVDProperties.FIELD_USER_AUDIT_ID.getValue()));
        assertEquals(insuranceQuotationModDao.getContactEmailDesc(), validation.get(RBVDProperties.FIELD_CONTACT_EMAIL_DESC.getValue()));
        assertEquals(insuranceQuotationModDao.getCustomerPhoneDesc(), validation.get(RBVDProperties.FIELD_CUSTOMER_PHONE_DESC.getValue()));
        assertEquals(insuranceQuotationModDao.getDataTreatmentIndType(), validation.get(RBVDProperties.FIELD_DATA_TREATMENT_IND_TYPE.getValue()));
    }

    @Test
    public void mappingOutputFieldsOK() throws IOException {
        CustomerListASO customerList = MockDTO.getInstance().getCustomerDataResponse();
        CustomerBO customerInformation = customerList.getData().get(0);

        when(this.rbvdR303.executeListCustomerService(anyString())).thenReturn(customerInformation);

        this.mapperHelper.mappingOutputFields(easyesQuotationDto, easyesQuotationDao);

        assertNotNull(easyesQuotationDto.getProduct().getName());
        assertNotNull(easyesQuotationDto.getProduct().getPlans().get(0).getName());
        assertNotNull(easyesQuotationDto.getProduct().getPlans().get(0).getInstallmentPlans().get(0).getPeriod().getName());
        assertNotNull(easyesQuotationDto.getHolder().getFirstName());
        assertNotNull(easyesQuotationDto.getHolder().getLastName());
        assertNotNull(easyesQuotationDto.getHolder().getFullName());

        String fullName = customerInformation.getFirstName().concat(" ").
                concat(customerInformation.getLastName()).concat(" ").concat(customerInformation.getSecondLastName());

        assertEquals(easyesQuotationDao.getInsuranceProductDescription(), easyesQuotationDto.getProduct().getName());
        assertEquals(easyesQuotationDao.getInsuranceModalityName(), easyesQuotationDto.getProduct().getPlans().get(0).getName());
        assertEquals(easyesQuotationDao.getPaymentFrequencyName(),
                easyesQuotationDto.getProduct().getPlans().get(0).getInstallmentPlans().get(0).getPeriod().getName());
        assertEquals(customerInformation.getFirstName(), easyesQuotationDto.getHolder().getFirstName());
        assertEquals(customerInformation.getLastName(), easyesQuotationDto.getHolder().getLastName());
        assertEquals(fullName, easyesQuotationDto.getHolder().getFullName());

        when(this.rbvdR303.executeListCustomerService(anyString())).thenReturn(null);

        this.mapperHelper.mappingOutputFields(easyesQuotationDto, easyesQuotationDao);

        assertEquals("", easyesQuotationDto.getHolder().getFirstName());
        assertEquals("", easyesQuotationDto.getHolder().getLastName());
        assertEquals("", easyesQuotationDto.getHolder().getFullName());
    }
}

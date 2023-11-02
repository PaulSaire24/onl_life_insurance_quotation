package com.bbva.rbvd.lib.r304.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.elara.domain.transaction.Context;
import com.bbva.elara.domain.transaction.ThreadContext;
import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.pisd.dto.insurance.bo.ContactDetailsBO;
import com.bbva.pisd.dto.insurance.bo.ContactTypeBO;
import com.bbva.pisd.dto.insurance.bo.DocumentTypeBO;
import com.bbva.pisd.dto.insurance.bo.IdentityDocumentsBO;
import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.pisd.dto.insurance.commons.ContactDetailDTO;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.commons.*;
import com.bbva.rbvd.dto.lifeinsrc.dao.ParticipantDAO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.mock.MockData;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.QuotationLifeBO;
import com.bbva.rbvd.dto.lifeinsrc.simulation.ContactDTO;
import com.bbva.rbvd.dto.lifeinsrc.simulation.ContractDetailsDTO;
import com.bbva.rbvd.dto.lifeinsrc.simulation.ParticipantDTO;
import com.bbva.rbvd.dto.lifeinsrc.simulation.ParticipantTypeDTO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.lib.r303.RBVDR303;
import com.bbva.rbvd.lib.r304.transfer.PayloadConfig;
import com.bbva.rbvd.lib.r304.transfer.PayloadProperties;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RBVDR304ImplTest {

    private final RBVDR304Impl rbvdr304 = new RBVDR304Impl();
    private PISDR350 pisdR350;
    private QuotationLifeDTO input;
    private Map<String,Object> mapInformation;
    @Mock
    private ApplicationConfigurationService applicationConfigurationService;

    @Before
    public void setUp() throws Exception {
        ThreadContext.set(new Context());

        input = MockData.getInstance().getEasyesInsuranceQuotationRequest();
        List<RefundsDTO> refunds = new ArrayList<>();
        RefundsDTO refund = new RefundsDTO();
        UnitDTO unit =new UnitDTO();
        unit.setUnitType("PERCENTAGE");

        unit.setPercentage(BigDecimal.ZERO);
        refund.setUnit(unit);
        refunds.add(refund);
        input.setRefunds(refunds);
        EasyesQuotationDAO myQuotation = mock(EasyesQuotationDAO.class);


        pisdR350 = mock(PISDR350.class);
        RBVDR303 rbvdr303 = mock(RBVDR303.class);
        applicationConfigurationService = mock(ApplicationConfigurationService.class);

        when(applicationConfigurationService.getProperty("MONTHLY")).thenReturn("M");

        PayloadProperties properties = new PayloadProperties();
        properties.setFrequencyTypeId("M");
        properties.setSelectedPlanId("02");
        properties.setProductType("840");
        properties.setPeriodId("MONTHLY");
        PayloadConfig payloadConfig = new PayloadConfig();
        payloadConfig.setInput(input);
        payloadConfig.setPayloadProperties(properties);
        payloadConfig.setMyQuotation(myQuotation);
        payloadConfig.setPolicyQuotaId("8523654");
        rbvdr304.setRbvdR303(rbvdr303);
        rbvdr304.setApplicationConfigurationService(applicationConfigurationService);
        rbvdr304.setPisdR350(pisdR350);

        QuotationLifeBO rimacResponse = MockData.getInstance().getInsuranceRimacQuotationResponse();

        when(rbvdr303.executeQuotationRimac(anyObject(), anyString(), anyString())).thenReturn(rimacResponse);

        mapInformation = new HashMap<>();

        mapInformation.put(RBVDProperties.FIELD_INSURANCE_SIMULATION_ID.getValue(),new BigDecimal("1"));
        mapInformation.put(RBVDProperties.FIELD_CUST_SIMULATION_EXPIRED_DATE.getValue(),Timestamp.valueOf("2018-12-12 01:02:03.123456789"));
        mapInformation.put(RBVDProperties.FIELD_INSURANCE_MODALITY_NAME.getValue(),"PLAN 1");
        mapInformation.put(RBVDProperties.FIELD_INSURANCE_COMPANY_MODALITY_ID.getValue(),"568904");
        mapInformation.put(RBVDProperties.FIELD_OR_FILTER_INSURANCE_PRODUCT_ID.getValue(),new BigDecimal("10"));
        mapInformation.put(RBVDProperties.FIELD_INSURANCE_PRODUCT_DESC.getValue(),"Seguro vida");
        mapInformation.put("PRODUCT_SHORT_DESC","EASYYES");
        mapInformation.put(RBVDProperties.FIELD_PAYMENT_FREQUENCY_NAME.getValue(),"MENSUAL");


        when(pisdR350.executeInsertSingleRow(anyString(),anyMap())).thenReturn(1);
        when(applicationConfigurationService.getProperty("DNI")).thenReturn("L");

    }

    @Test
    public void testExecuteBusinessLogicEasyesQuotationInsertQuotation_OK() {
        this.input.getProduct().setId("840");
        DocumentTypeDTO documentTypeDTO = new DocumentTypeDTO();
        documentTypeDTO.setId("DNI");
        IdentityDocumentDTO identityDocumentDTO = new IdentityDocumentDTO();
        identityDocumentDTO.setDocumentNumber("14457841");
        identityDocumentDTO.setDocumentType(documentTypeDTO);
        HolderDTO holderDTO = new HolderDTO();
        holderDTO.setFirstName("Alec");
        holderDTO.setLastName("Alec taboada");
        ParticipantDTO participantDTO = new ParticipantDTO();
        participantDTO.setId("10225879");
        participantDTO.setBirthDate( new Date());
        participantDTO.setIdentityDocument(identityDocumentDTO);

        participantDTO.setParticipantType(new ParticipantTypeDTO());
        participantDTO.getParticipantType().setId("455");
        ContractDetailsDTO contractDetail = new ContractDetailsDTO();
        contractDetail.setContact(new ContactDTO());
        contractDetail.getContact().setContactDetailType("MOBILE_NUMBER");
        contractDetail.getContact().setNumber("999999999");
        ContractDetailsDTO contractDetail2 = new ContractDetailsDTO();
        contractDetail2.setContact(new ContactDTO());
        contractDetail2.getContact().setContactDetailType("EMAIL");
        contractDetail2.getContact().setAddress("@gmail.com");
        List<ContractDetailsDTO> contractDetailsList = new ArrayList<>();
        contractDetailsList.add(contractDetail);
        contractDetailsList.add(contractDetail2);
        participantDTO.setContactDetails(contractDetailsList);
        this.input.setHolder(holderDTO);
        this.input.setTerm(new TermDTO());
        this.input.getTerm().setNumber(45);
        this.input.setParticipants(Collections.singletonList(participantDTO));
        CustomerListASO customerListASO= new CustomerListASO();
        CustomerBO customerBO =new CustomerBO();
        customerBO.setFirstName("Alec");
        customerBO.setSecondLastName("Taboada");
        customerBO.setLastName("Taboada");
        List<IdentityDocumentsBO> documents= new ArrayList<>();
        IdentityDocumentsBO identityDocumentsBO = new IdentityDocumentsBO();
        identityDocumentsBO.setDocumentNumber("75874332");
        DocumentTypeBO type = new DocumentTypeBO();
        type.setId("DNI");
        identityDocumentsBO.setDocumentType(type);
        documents.add(identityDocumentsBO);
        customerBO.setIdentityDocuments(documents);
        ContactDetailsBO contractDetailc = new ContactDetailsBO();
        contractDetailc.setContactType(new ContactTypeBO());
        contractDetailc.getContactType().setId("MOBILE_NUMBER");
        contractDetailc.setContact("999999999");
        ContactDetailsBO contractDetail2c = new ContactDetailsBO();
        contractDetail2c.setContactType(new ContactTypeBO());
        contractDetail2c.getContactType().setId("EMAIL");
        contractDetail2c.setContact("@gmail.com");
        List<ContactDetailsBO> contractDetailsListc = new ArrayList<>();
        contractDetailsListc.add(contractDetailc);
        contractDetailsListc.add(contractDetail2c);
        customerBO.setContactDetails(contractDetailsListc);
        List<CustomerBO> customerBOS = new ArrayList<>();
        customerBOS.add(customerBO);
        customerListASO.setData(customerBOS);
        mapInformation.put(RBVDProperties.FIELD_RESULT_NUMBER.getValue(),new BigDecimal(0));
        when(rbvdr304.rbvdR303.executeGetCustomerHost(anyString())).thenReturn(customerListASO);
        when(pisdR350.executeGetASingleRow(anyString(), anyMap())).thenReturn(mapInformation);
        QuotationLifeDTO validation = this.rbvdr304.executeBusinessLogicQuotation(input);
        assertNotNull(validation);
    }
    @Test
    public void testExecuteBusinessLogicEasyesQuotationInsertQuotation_Passport() {
        EasyesQuotationDAO myQuotation = mock(EasyesQuotationDAO.class);
        CustomerListASO customerListASO= new CustomerListASO();
        CustomerBO customerBO =new CustomerBO();

        customerBO.setFirstName("Alec");
        customerBO.setSecondLastName("Taboada");
        customerBO.setLastName("Taboada");
        List<IdentityDocumentsBO> documents= new ArrayList<>();
        IdentityDocumentsBO identityDocumentsBO = new IdentityDocumentsBO();
        identityDocumentsBO.setDocumentNumber("75874332");
        DocumentTypeBO type = new DocumentTypeBO();
        type.setId("DNI");
        identityDocumentsBO.setDocumentType(type);
        documents.add(identityDocumentsBO);
        customerBO.setIdentityDocuments(documents);
        ContactDetailsBO contractDetailc = new ContactDetailsBO();
        contractDetailc.setContactType(new ContactTypeBO());
        contractDetailc.getContactType().setId("MOBILE_NUMBER");
        contractDetailc.setContact("999999999");
        ContactDetailsBO contractDetail2c = new ContactDetailsBO();
        contractDetail2c.setContactType(new ContactTypeBO());
        contractDetail2c.getContactType().setId("EMAIL");
        contractDetail2c.setContact("@gmail.com");
        List<ContactDetailsBO> contractDetailsListc = new ArrayList<>();
        contractDetailsListc.add(contractDetailc);
        contractDetailsListc.add(contractDetail2c);
        customerBO.setContactDetails(contractDetailsListc);
        List<CustomerBO> customerBOS = new ArrayList<>();
        customerBOS.add(customerBO);
        customerListASO.setData(customerBOS);
        PayloadProperties properties = new PayloadProperties();
        properties.setFrequencyTypeId("M");
        properties.setSelectedPlanId("02");
        properties.setProductType("840");
        properties.setPeriodId("MONTHLY");
        PayloadConfig payloadConfig = new PayloadConfig();
        payloadConfig.setInput(input);
        payloadConfig.setCustomerInformation(customerListASO);
        payloadConfig.setPayloadProperties(properties);
        payloadConfig.setMyQuotation(myQuotation);
        payloadConfig.setPolicyQuotaId("8523654");
        this.input.getProduct().setId("840");
        DocumentTypeDTO documentTypeDTO = new DocumentTypeDTO();
        documentTypeDTO.setId("P");
        IdentityDocumentDTO identityDocumentDTO = new IdentityDocumentDTO();
        identityDocumentDTO.setDocumentNumber("14457841");
        identityDocumentDTO.setDocumentType(documentTypeDTO);
        HolderDTO holderDTO = new HolderDTO();
        holderDTO.setFirstName("Alec");
        holderDTO.setLastName("Alec taboada");
        ContractDetailsDTO contractDetail = new ContractDetailsDTO();
        contractDetail.setContact(new ContactDTO());
        contractDetail.getContact().setContactDetailType("MOBILE_NUMBER");
        contractDetail.getContact().setNumber("999999999");
        ContractDetailsDTO contractDetail2 = new ContractDetailsDTO();
        contractDetail2.setContact(new ContactDTO());
        contractDetail2.getContact().setContactDetailType("EMAIL");
        contractDetail2.getContact().setAddress("@gmail.com");
        List<ContractDetailsDTO> contractDetailsList = new ArrayList<>();
        contractDetailsList.add(contractDetail);
        contractDetailsList.add(contractDetail2);

        this.input.setHolder(holderDTO);
        this.input.setTerm(new TermDTO());
        this.input.getTerm().setNumber(45);

        mapInformation.put(RBVDProperties.FIELD_RESULT_NUMBER.getValue(),new BigDecimal(0));
        when(rbvdr304.rbvdR303.executeGetCustomerHost(anyString())).thenReturn(customerListASO);
        when(pisdR350.executeGetASingleRow(anyString(), anyMap())).thenReturn(mapInformation);
        QuotationLifeDTO validation = this.rbvdr304.executeBusinessLogicQuotation(input);
        assertNotNull(validation);
    }
    @Test
    public void testExecuteBusinessLogicEasyesQuotationInsertQuotation_Holder() {
        EasyesQuotationDAO myQuotation = mock(EasyesQuotationDAO.class);
        CustomerListASO customerListASO= new CustomerListASO();
        CustomerBO customerBO =new CustomerBO();

        customerBO.setFirstName("Alec");
        customerBO.setSecondLastName("Taboada");
        customerBO.setLastName("Taboada");
        List<IdentityDocumentsBO> documents= new ArrayList<>();
        IdentityDocumentsBO identityDocumentsBO = new IdentityDocumentsBO();
        identityDocumentsBO.setDocumentNumber("75874332");
        DocumentTypeBO type = new DocumentTypeBO();
        type.setId("DNI");
        identityDocumentsBO.setDocumentType(type);
        documents.add(identityDocumentsBO);
        customerBO.setIdentityDocuments(documents);
        ContactDetailsBO contractDetailc = new ContactDetailsBO();
        contractDetailc.setContactType(new ContactTypeBO());
        contractDetailc.getContactType().setId("MOBILE_NUMBER");
        contractDetailc.setContact("999999999");
        ContactDetailsBO contractDetail2c = new ContactDetailsBO();
        contractDetail2c.setContactType(new ContactTypeBO());
        contractDetail2c.getContactType().setId("EMAIL");
        contractDetail2c.setContact("@gmail.com");
        List<ContactDetailsBO> contractDetailsListc = new ArrayList<>();
        contractDetailsListc.add(contractDetailc);
        contractDetailsListc.add(contractDetail2c);
        customerBO.setContactDetails(contractDetailsListc);
        List<CustomerBO> customerBOS = new ArrayList<>();
        customerBOS.add(customerBO);
        customerListASO.setData(customerBOS);
        PayloadProperties properties = new PayloadProperties();
        properties.setFrequencyTypeId("M");
        properties.setSelectedPlanId("02");
        properties.setProductType("840");
        properties.setPeriodId("MONTHLY");
        PayloadConfig payloadConfig = new PayloadConfig();
        payloadConfig.setInput(input);
        payloadConfig.setCustomerInformation(customerListASO);
        payloadConfig.setPayloadProperties(properties);
        payloadConfig.setMyQuotation(myQuotation);
        payloadConfig.setPolicyQuotaId("8523654");
        this.input.getProduct().setId("840");
        DocumentTypeDTO documentTypeDTO = new DocumentTypeDTO();
        documentTypeDTO.setId("DNI");
        IdentityDocumentDTO identityDocumentDTO = new IdentityDocumentDTO();
        identityDocumentDTO.setDocumentNumber("14457841");
        identityDocumentDTO.setDocumentType(documentTypeDTO);
        HolderDTO holderDTO = new HolderDTO();
        holderDTO.setFirstName("Alec");
        holderDTO.setLastName("Alec taboada");
        ContractDetailsDTO contractDetail = new ContractDetailsDTO();
        contractDetail.setContact(new ContactDTO());
        contractDetail.getContact().setContactDetailType("MOBILE_NUMBER");
        contractDetail.getContact().setNumber("999999999");
        ContractDetailsDTO contractDetail2 = new ContractDetailsDTO();
        contractDetail2.setContact(new ContactDTO());
        contractDetail2.getContact().setContactDetailType("EMAIL");
        contractDetail2.getContact().setAddress("@gmail.com");
        List<ContractDetailsDTO> contractDetailsList = new ArrayList<>();
        contractDetailsList.add(contractDetail);
        contractDetailsList.add(contractDetail2);

        this.input.setHolder(holderDTO);
        this.input.setTerm(new TermDTO());
        this.input.getTerm().setNumber(45);

        mapInformation.put(RBVDProperties.FIELD_RESULT_NUMBER.getValue(),new BigDecimal(0));
        when(rbvdr304.rbvdR303.executeGetCustomerHost(anyString())).thenReturn(customerListASO);
        when(pisdR350.executeGetASingleRow(anyString(), anyMap())).thenReturn(mapInformation);
        QuotationLifeDTO validation = this.rbvdr304.executeBusinessLogicQuotation(input);
        assertNotNull(validation);
    }
    @Test
    public void testExecuteBusinessLogicEasyesQuotationInsertQuotation_LastNameNull() {
        this.input.getProduct().setId("840");
        DocumentTypeDTO documentTypeDTO = new DocumentTypeDTO();
        documentTypeDTO.setId("DNI");
        IdentityDocumentDTO identityDocumentDTO = new IdentityDocumentDTO();
        identityDocumentDTO.setDocumentNumber("14457841");
        identityDocumentDTO.setDocumentType(documentTypeDTO);
        HolderDTO holderDTO = new HolderDTO();
        holderDTO.setFirstName("Alec");
        holderDTO.setLastName("Alec taboada");
        holderDTO.setIdentityDocument(identityDocumentDTO);
        ContractDetailsDTO contractDetail = new ContractDetailsDTO();
        contractDetail.setContact(new ContactDTO());
        contractDetail.getContact().setContactDetailType("MOBILE_NUMBER");
        contractDetail.getContact().setNumber("999999999");
        ContractDetailsDTO contractDetail2 = new ContractDetailsDTO();
        contractDetail2.setContact(new ContactDTO());
        contractDetail2.getContact().setContactDetailType("EMAIL");
        contractDetail2.getContact().setAddress("xd@gmail.com");
        List<ContractDetailsDTO> contractDetailsList = new ArrayList<>();
        contractDetailsList.add(contractDetail);
        contractDetailsList.add(contractDetail2);


        this.input.setHolder(holderDTO);
        this.input.setTerm(new TermDTO());
        this.input.getTerm().setNumber(45);

        mapInformation.put(RBVDProperties.FIELD_RESULT_NUMBER.getValue(),new BigDecimal(0));
        when(pisdR350.executeGetASingleRow(anyString(), anyMap())).thenReturn(mapInformation);
        QuotationLifeDTO validation = this.rbvdr304.executeBusinessLogicQuotation(input);
        assertNotNull(validation);
    }

    private List<ParticipantDTO> participantsInput(){
        List<ParticipantDTO> participantDTOS = new ArrayList<>();
        ParticipantDTO insured = new ParticipantDTO();

        insured.setId("73944043");
        insured.setFirstName("Daniel");
        insured.setLastName("Leroy");
        insured.setSecondLastName("Silverson");
        ParticipantTypeDTO participantType = new ParticipantTypeDTO();
        participantType.setId("INSURED");
        insured.setParticipantType(participantType);
        List<ContractDetailsDTO> contractDetails = new ArrayList<>();
        ContractDetailsDTO phone = new ContractDetailsDTO();
        phone.setContact(new ContactDTO());
        phone.getContact().setContactDetailType("MOBILE_NUMBER");
        phone.getContact().setNumber("999999999");
        ContractDetailsDTO email = new ContractDetailsDTO();
        email.setContact(new ContactDTO());
        email.getContact().setContactDetailType("EMAIL");
        email.getContact().setAddress("xd@gmail.com");
        contractDetails.add(phone);
        contractDetails.add(email);
        insured.setContactDetails(contractDetails);
        IdentityDocumentDTO identityDocument = new IdentityDocumentDTO();
        identityDocument.setDocumentNumber("36384943");
        DocumentTypeDTO documentType = new DocumentTypeDTO();
        documentType.setId("DNI");
        identityDocument.setDocumentType(documentType);
        insured.setIdentityDocument(identityDocument);
        GenderDTO gender = new GenderDTO();
        gender.setId("MALE");
        insured.setGender(gender);

        participantDTOS.add(insured);
        return participantDTOS;
    }

    @Test
    public void testExecuteBusinessLogicDynamicLifeQuotationInsert_InputParticipantsNotNull() {
        this.input.getProduct().setId("841");
        DocumentTypeDTO documentTypeDTO = new DocumentTypeDTO();
        documentTypeDTO.setId("DNI");
        IdentityDocumentDTO identityDocumentDTO = new IdentityDocumentDTO();
        identityDocumentDTO.setDocumentNumber("14457841");
        identityDocumentDTO.setDocumentType(documentTypeDTO);
        HolderDTO holderDTO = new HolderDTO();
        holderDTO.setFirstName("Alec");
        holderDTO.setLastName("Alec taboada");
        holderDTO.setIdentityDocument(identityDocumentDTO);
        this.input.setHolder(holderDTO);
        this.input.setTerm(new TermDTO());
        this.input.getTerm().setNumber(45);
        this.input.setRefunds(null);
        this.input.setParticipants(participantsInput());

        mapInformation.put(RBVDProperties.FIELD_RESULT_NUMBER.getValue(),new BigDecimal(0));
        when(pisdR350.executeGetASingleRow(anyString(), anyMap())).thenReturn(mapInformation);
        QuotationLifeDTO validation = this.rbvdr304.executeBusinessLogicQuotation(input);

        assertNotNull(validation);
    }

    @Test
    public void testExecuteBusinessLogicEasyesQuotationInsertQuotation_NullParticipant() {
        input.setInsuredAmount(null);
        PayloadProperties properties = new PayloadProperties();
        properties.setFrequencyTypeId("M");
        properties.setSelectedPlanId("02");
        properties.setProductType("840");
        properties.setPeriodId("MONTHLY");
        PayloadConfig payloadConfig = new PayloadConfig();
        payloadConfig.setInput(input);
        payloadConfig.setPayloadProperties(properties);
        payloadConfig.setPolicyQuotaId("8523654");
        mapInformation.put(RBVDProperties.FIELD_RESULT_NUMBER.getValue(),new BigDecimal(0));
        when(pisdR350.executeGetASingleRow(anyString(), anyMap())).thenReturn(mapInformation);
        QuotationLifeDTO validation = this.rbvdr304.executeBusinessLogicQuotation(input);
        assertNotNull(validation);
    }

    @Test
    public void testExecuteBusinessLogicEasyesQuotationUpdateQuotation_OK() {

        mapInformation.put(RBVDProperties.FIELD_RESULT_NUMBER.getValue(),new BigDecimal(1));
        when(pisdR350.executeGetASingleRow(anyString(), anyMap())).thenReturn(mapInformation);

        QuotationLifeDTO validation = this.rbvdr304.executeBusinessLogicQuotation(input);
        assertNotNull(validation);
    }

    @Test
    public void testExecuteBusinessLogicDynamicLifeInsertQuotation_OK() {
        input.getProduct().setId("841");

        mapInformation.put(RBVDProperties.FIELD_RESULT_NUMBER.getValue(),new BigDecimal(0));
        when(pisdR350.executeGetASingleRow(anyString(), anyMap())).thenReturn(mapInformation);

        QuotationLifeDTO validation = this.rbvdr304.executeBusinessLogicQuotation(input);
        assertNotNull(validation);

    }

    @Test
    public void testExecuteBusinessLogicDynamicLifeUpdateQuotation_OK() {
        input.getProduct().setId("841");
         mapInformation.put(RBVDProperties.FIELD_RESULT_NUMBER.getValue(),new BigDecimal(1));
        when(pisdR350.executeGetASingleRow(anyString(), anyMap())).thenReturn(mapInformation);

        QuotationLifeDTO validation = this.rbvdr304.executeBusinessLogicQuotation(input);
        assertNotNull(validation);
    }

}
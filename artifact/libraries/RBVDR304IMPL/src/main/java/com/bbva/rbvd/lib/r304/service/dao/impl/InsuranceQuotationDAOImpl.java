package com.bbva.rbvd.lib.r304.service.dao.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.pisd.dto.insurance.bo.ContactDetailsBO;
import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationDAO;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.commons.InsurancePlanDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.RefundsDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.TermDTO;
import com.bbva.rbvd.dto.lifeinsrc.dao.ContactDetailsDAO;
import com.bbva.rbvd.dto.lifeinsrc.dao.InsuredLifeDAO;
import com.bbva.rbvd.dto.lifeinsrc.dao.ParticipantDAO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.dto.lifeinsrc.simulation.ContractDetailsDTO;
import com.bbva.rbvd.dto.lifeinsrc.simulation.ParticipantDTO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDValidation;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceQuotationDAO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import com.bbva.rbvd.lib.r304.transform.bean.InsuranceQuotationBean;
import com.bbva.rbvd.lib.r304.transform.map.InsuranceQuotationMap;
import com.bbva.rbvd.lib.r304.impl.util.ConstantUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Date;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.bbva.rbvd.lib.r304.impl.util.ConstantUtils.*;
import static com.bbva.rbvd.lib.r304.impl.util.ValidationUtil.*;


public class InsuranceQuotationDAOImpl implements IInsuranceQuotationDAO {
    private static final Logger LOGGER = LoggerFactory.getLogger(InsuranceQuotationDAOImpl.class);
    private final PISDR350 pisdR350;

    public InsuranceQuotationDAOImpl(PISDR350 pisdR350) {
        this.pisdR350 = pisdR350;
    }
    @Override
    public InsuredLifeDAO createQuotationParticipant(PayloadStore payloadStore, ApplicationConfigurationService applicationConfigurationService) {
        InsuredLifeDAO quotationParticipant = new InsuredLifeDAO();

        EasyesQuotationDAO quotationDao = payloadStore.getMyQuotation();
        QuotationLifeDTO input = payloadStore.getInput();
        BigDecimal periodNumber = safeBigDecimal(input, QuotationLifeDTO::getTerm, TermDTO::getNumber, BigDecimal.ZERO);
        RefundsDTO refundPercentaje = calculateTotalReturnAmountOrRefundPercentaje(input,PERCENTAGE); ;
        RefundsDTO totalAmount = calculateTotalReturnAmountOrRefundPercentaje(input,AMOUNT); ;
        InsurancePlanDTO plan = input.getProduct().getPlans().get(0);

        quotationParticipant.setPolicyQuotaInternalId(input.getId());
        quotationParticipant.setInsuranceProductId(quotationDao.getInsuranceProductId());
        quotationParticipant.setInsuranceModalityType(plan.getId());
        quotationParticipant.setInsuredAmount(safeGetInsuredAmount(input));
        quotationParticipant.setParticipant(getParticipantDao(input,applicationConfigurationService,payloadStore.getCustomerInformation()));
        quotationParticipant.setTerm(getTermDAO(periodNumber));
        quotationParticipant.setRefunds(getRefundsDAO(refundPercentaje,input,totalAmount));
        quotationParticipant.setCreationUser(input.getCreationUser());
        quotationParticipant.setUserAudit(input.getUserAudit());

        /** en caso que el term sea nulo period type nulo**/
        if(Objects.isNull(input.getTerm())){
            quotationParticipant.getTerm().setPeriodType(null);
        }


        return quotationParticipant;
    }

    private InsuredLifeDAO.RefundsDAO getRefundsDAO(RefundsDTO refundPercentaje,QuotationLifeDTO input,RefundsDTO totalAmount){
        InsuredLifeDAO insuredLifeDAO = new InsuredLifeDAO();
        InsuredLifeDAO.RefundsDAO refundsDAO = insuredLifeDAO.new RefundsDAO();

        refundsDAO.setRefundPer((Objects.nonNull(refundPercentaje) && refundPercentaje.getUnit().getPercentage() != null) ?
                refundPercentaje.getUnit().getPercentage(): null);
        refundsDAO.setCurrencyId(safeGetInsuredCurrency(input));
        refundsDAO.setTotalReturnAmount((Objects.nonNull(totalAmount) && totalAmount.getUnit().getAmount() != null) ?
                totalAmount.getUnit().getAmount(): null);

        return refundsDAO;
    }

    private InsuredLifeDAO.TermDAO getTermDAO(BigDecimal periodNumber){
        InsuredLifeDAO insuredLife = new InsuredLifeDAO();
        InsuredLifeDAO.TermDAO termDAO = insuredLife.new TermDAO();
        termDAO.setPeriodNumber(periodNumber);
        termDAO.setPeriodType(ANNUAL);

        return termDAO;
    }

    private ParticipantDAO getParticipantDao(QuotationLifeDTO input,
                                             ApplicationConfigurationService applicationConfigurationService,
                                             CustomerListASO customerInformation){
        ParticipantDAO participantDAO = new ParticipantDAO();

        participantDAO.setCustomerEntryDate(getCustomerEntryDate(input));
        participantDAO.setParticipantRoleId(BigDecimal.valueOf(ConstantUtils.IS_INSURED));

        /** verificar si participante existe**/
        ParticipantDTO participant = safeGetParticipant(input);
        if (Objects.nonNull(participant)) {
            setParticipantPropertiesFromInput(participantDAO, participant,applicationConfigurationService,input);
        } else {
            CustomerBO customerData = safeGetCustomerData(customerInformation, input);
            setParticipantPreportiesFromCustomerData(participantDAO, input, customerData,applicationConfigurationService);
        }

        return participantDAO;
    }

    private BigDecimal safeBigDecimal(QuotationLifeDTO input, Function<QuotationLifeDTO, TermDTO> getTerm,
                                      Function<TermDTO, Integer> getNumber, BigDecimal defaultValue) {

        TermDTO term = getTerm.apply(input);
        if (Objects.isNull(term)) {
            return defaultValue;
        }

        Integer number = getNumber.apply(term);
        if (Objects.isNull(number)) {
            return defaultValue;
        }

        return new BigDecimal(number);
    }

    private RefundsDTO calculateTotalReturnAmountOrRefundPercentaje(QuotationLifeDTO input,String tipo) {
        if (CollectionUtils.isEmpty(input.getRefunds())) {
            return null;
        } else {
            return input.getRefunds().stream()
                    .filter(refundsDTO -> refundsDTO.getUnit() != null)
                    .filter(refundsDTO -> tipo.equals(refundsDTO.getUnit().getUnitType()))
                    .findFirst()
                    .orElse(null);
        }
    }

    public static List<ContractDetailsDTO> getGroupedByTypeContactDetail(List<ContractDetailsDTO> customer, String tipoContacto) {
        return customer.stream()
                .filter(contactInfo ->
                        contactInfo != null &&
                                contactInfo.getContact() != null &&
                                tipoContacto.equals(contactInfo.getContact().getContactDetailType())
                )
                .collect(Collectors.toList());
    }
    public static List<ContactDetailsBO> getGroupedByTypeContactDetailBO(List<ContactDetailsBO> customer, String tipoContacto) {
        return customer.stream()
                .filter(contactInfo ->
                        contactInfo != null &&
                                contactInfo.getContact() != null &&
                                tipoContacto.equals(contactInfo.getContactType().getId())
                )
                .collect(Collectors.toList());
    }

    private BigDecimal safeGetInsuredAmount(QuotationLifeDTO input) {
        return (Objects.nonNull(input.getInsuredAmount())) ? input.getInsuredAmount().getAmount() : null;
    }
    private LocalDate getCustomerEntryDate(QuotationLifeDTO input) {
        if (CollectionUtils.isEmpty(input.getParticipants()) ||
                (Objects.isNull(input.getParticipants().get(0)) ||
                        Objects.isNull(input.getParticipants().get(0).getId()) ||
                        BLANK.equals(input.getParticipants().get(0).getId()))) {
            return null;
        } else {
            return LocalDate.now();
        }
    }
    private String safeGetInsuredCurrency(QuotationLifeDTO input) {
        return (Objects.nonNull(input.getInsuredAmount())) ? input.getInsuredAmount().getCurrency() : null;
    }

    private String safeGetInsuredId(QuotationLifeDTO input) {
        if (!CollectionUtils.isEmpty(input.getParticipants())) {
            ParticipantDTO firstParticipant = input.getParticipants().get(0);
            if (Objects.nonNull(firstParticipant) && Objects.nonNull(firstParticipant.getId())) {
                return firstParticipant.getId();
            }
        }
        return input.getHolder().getId();
    }
    private ParticipantDTO safeGetParticipant(QuotationLifeDTO input) {
        return (!CollectionUtils.isEmpty(input.getParticipants())) ? input.getParticipants().get(0) : null;
    }

    private CustomerBO safeGetCustomerData(CustomerListASO customerInformation, QuotationLifeDTO input) {
        if (Objects.nonNull(customerInformation) && !CollectionUtils.isEmpty(customerInformation.getData())) {
            return customerInformation.getData().get(0);
        }
        return null;
    }

    private void setParticipantPropertiesFromInput(ParticipantDAO participantDAO, ParticipantDTO participant,
                                          ApplicationConfigurationService applicationConfigurationService,QuotationLifeDTO input) {
        List<ContractDetailsDTO> tipoContratoEmail = getGroupedByTypeContactDetail(participant.getContactDetails(), EMAIL);
        List<ContractDetailsDTO> tipoContratoMov = getGroupedByTypeContactDetail(participant.getContactDetails(), MOBILE_NUMBER);
        String lastName = (participant.getLastName() != null ? participant.getLastName() : BLANK) +
                SLASH + (participant.getSecondLastName() != null ? participant.getSecondLastName() : BLANK);

        Instant instant = participant.getBirthDate().toInstant();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        String documentType =(participant.getIdentityDocument() != null && participant.getIdentityDocument().getDocumentType() != null) ?
                participant.getIdentityDocument().getDocumentType().getId(): null;

        /** Rellenar datos que trae host de holder **/
        participantDAO.setInsuredId(safeGetInsuredId(input));
        participantDAO.setCustomerDocumentType((documentType != null) ? applicationConfigurationService.getProperty(documentType):null);
        participantDAO.setPersonalId(participant.getIdentityDocument().getDocumentNumber());
        participantDAO.setIsBbvaCustomerType(isBBVAClient(participant.getId()) ? ConstantUtils.YES_S : ConstantUtils.NO_N);
        participantDAO.setInsuredCustomerName(participant.getFirstName());
        participantDAO.setClientLastName(lastName);
        participantDAO.setCustomerBirthDate(localDate);
        participantDAO.setGenderId((participant.getGender() != null) ? participant.getGender().getId().substring(0,1) : null);
        participantDAO.setContactDetails(getContactDetailsDAOFromInputParticipant(tipoContratoEmail, tipoContratoMov));
    }

    private void setParticipantPreportiesFromCustomerData(ParticipantDAO participantDAO, QuotationLifeDTO input,
                                  CustomerBO customerData,ApplicationConfigurationService applicationConfigurationService) {
        List<ContactDetailsBO> tipoContratoEmail;
        List<ContactDetailsBO> tipoContratoMov;

        if(Objects.nonNull(customerData)&&customerData.getContactDetails()!=null) {
             tipoContratoEmail = getGroupedByTypeContactDetailBO(customerData.getContactDetails(), EMAIL);
             tipoContratoMov = getGroupedByTypeContactDetailBO(customerData.getContactDetails(), NUMBER);
        }
        else{
            tipoContratoEmail = null;
            tipoContratoMov = null;
        }

        String documentType =(Objects.nonNull(customerData)&&!CollectionUtils.isEmpty(customerData.getIdentityDocuments()) &&
                !customerData.getIdentityDocuments().isEmpty() && customerData.getIdentityDocuments().get(0).getDocumentType() != null) ?
                customerData.getIdentityDocuments().get(0).getDocumentType().getId() : null;

        /** Rellenar datos de participant **/
        participantDAO.setInsuredId(input.getHolder().getId());
        participantDAO.setCustomerDocumentType((documentType != null) ? applicationConfigurationService.getProperty(documentType):null);
        participantDAO.setPersonalId(
                Optional.ofNullable(customerData)
                        .map(CustomerBO::getIdentityDocuments)
                        .filter(documents -> !documents.isEmpty())
                        .map(documents -> documents.get(0).getDocumentNumber())
                        .orElse(null));
        participantDAO.setIsBbvaCustomerType(isBBVAClient(input.getHolder().getId()) ? ConstantUtils.YES_S : ConstantUtils.NO_N);
        if(Objects.nonNull(customerData) ) {
            participantDAO.setInsuredCustomerName(getSafeFirstName(customerData));
            participantDAO.setClientLastName(getLastName(customerData));
            participantDAO.setCustomerBirthDate(parseDate(customerData));
        }
        participantDAO.setGenderId((Objects.nonNull(customerData)&&(customerData.getGender() != null) ? customerData.getGender().getId().substring(0, 1) : null));
        participantDAO.setContactDetails(getContactDetailsASOFromCustomerBO(tipoContratoEmail, tipoContratoMov));
    }

    private static ContactDetailsDAO getContactDetailsDAOFromInputParticipant(List<ContractDetailsDTO> tipoContratoEmail, List<ContractDetailsDTO> tipoContratoMov) {
        ContactDetailsDAO contactDetailsDAO = new ContactDetailsDAO();
        contactDetailsDAO.setPhoneId((!CollectionUtils.isEmpty(tipoContratoMov) && tipoContratoMov.get(0).getContact() != null) ?
                tipoContratoMov.get(0).getContact().getNumber() : null);
        contactDetailsDAO.setUserEmailPersonalDesc((!CollectionUtils.isEmpty(tipoContratoEmail) && tipoContratoEmail.get(0).getContact() != null) ?
                tipoContratoEmail.get(0).getContact().getAddress() : null);
        return contactDetailsDAO;
    }

    private static ContactDetailsDAO getContactDetailsASOFromCustomerBO(List<ContactDetailsBO> tipoContratoEmail, List<ContactDetailsBO> tipoContratoMov) {
        ContactDetailsDAO contactDetailsDAO = new ContactDetailsDAO();
        contactDetailsDAO.setPhoneId((Objects.nonNull(tipoContratoMov) && tipoContratoMov.get(0).getContact() != null) ? tipoContratoMov.get(0).getContact() : null);
        contactDetailsDAO.setUserEmailPersonalDesc((!CollectionUtils.isEmpty(tipoContratoEmail) && tipoContratoEmail.get(0).getContact() != null) ? tipoContratoEmail.get(0).getContact() : null);
        return contactDetailsDAO;
    }

    public static LocalDate parseDate(CustomerBO customerData) {
        Date birthday=null;
        if(customerData.getBirthData()!=null && customerData.getBirthData().getBirthDate()!=null) {
            LocalDate lc = LocalDate.parse(customerData.getBirthData().getBirthDate(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            ZoneId localZone = ZoneId.of("America/Lima");
            birthday = Date.from(lc.atStartOfDay(localZone).toInstant());
        }
        return (Objects.nonNull(birthday))?birthday.toInstant().atZone(ZONE_ID).toLocalDate():null;
    }
    private String getSafeFirstName(CustomerBO customerData){
        return Objects.nonNull(customerData) && (customerData.getFirstName())!=null?
                customerData.getFirstName() : null;
    }
    private String getLastName(CustomerBO customerData){

        String lastName = BLANK;
        if (customerData != null) {
            if (customerData.getLastName() != null) {
                lastName += customerData.getLastName();
            }
            lastName += SLASH;
            if (customerData.getSecondLastName() != null) {
                lastName += customerData.getSecondLastName();
            }
    }
        return lastName;
    }
    @Override
    public void insertSimulationParticipant(Map<String, Object> argumentForSaveParticipant) {
        int idNewSimulation = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_INSERT_INSURED_QUOTATION.getValue(),argumentForSaveParticipant);
        if(idNewSimulation != 1){
            throw RBVDValidation.build(RBVDErrors.QUOTATION_INSERTION_WAS_WRONG);
        }
    }
    @Override
    public void updateSimulationParticipant(Map<String, Object> argumentForSaveParticipant) {
        int idNewSimulation = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_UPDATE_INSURED_QUOTATION.getValue(),argumentForSaveParticipant);
        if(idNewSimulation != 1){
            throw RBVDValidation.build(RBVDErrors.QUOTATION_INSERTION_WAS_WRONG);
        }
    }


    @Override
    public void executeInsertQuotationQuery(PayloadStore payloadStore) {
        InsuranceQuotationDAO insuranceQuotationDao = InsuranceQuotationBean.createInsuranceQuotationDAO(payloadStore.getMyQuotation(), payloadStore.getInput());
        Map<String, Object> argumentsQuotationDao = InsuranceQuotationMap.createArgumentsQuotationDao(insuranceQuotationDao);
        Integer quotationResult = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_INSERT_INSURANCE_QUOTATION.getValue(), argumentsQuotationDao);
        validateInsertionQueries(quotationResult, RBVDErrors.QUOTATION_INSERTION_WAS_WRONG);
    }
}

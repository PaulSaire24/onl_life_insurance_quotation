package com.bbva.rbvd.lib.r304.transform.bean;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.pisd.dto.insurance.bo.ContactDetailsBO;
import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.rbvd.dto.lifeinsrc.commons.HolderDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.InsurancePlanDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.RefundsDTO;
import com.bbva.rbvd.dto.lifeinsrc.dao.ContactDetailsDAO;
import com.bbva.rbvd.dto.lifeinsrc.dao.InsuredLifeDAO;
import com.bbva.rbvd.dto.lifeinsrc.dao.ParticipantDAO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.dto.lifeinsrc.simulation.ContractDetailsDTO;
import com.bbva.rbvd.dto.lifeinsrc.simulation.ParticipantDTO;
import com.bbva.rbvd.lib.r304.impl.util.ConstantUtils;
import com.bbva.rbvd.lib.r304.impl.util.ConvertUtils;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.bbva.rbvd.lib.r304.impl.util.ValidationUtil.isBBVAClient;

public class InsuranceQuotationInsuredBean {

    private InsuranceQuotationInsuredBean(){}

    public static InsuredLifeDAO createQuotationParticipant(PayloadStore payloadStore, ApplicationConfigurationService applicationConfigurationService) {
        InsuredLifeDAO quotationParticipant = new InsuredLifeDAO();

        EasyesQuotationDAO quotationDao = payloadStore.getMyQuotation();
        QuotationLifeDTO input = payloadStore.getInput();
        InsurancePlanDTO plan = input.getProduct().getPlans().get(0);

        quotationParticipant.setPolicyQuotaInternalId(input.getId());
        quotationParticipant.setInsuranceProductId(quotationDao.getInsuranceProductId());
        quotationParticipant.setInsuranceModalityType(plan.getId());
        quotationParticipant.setInsuredAmount(safeGetInsuredAmount(input));
        quotationParticipant.setParticipant(
                getParticipantDao(input,applicationConfigurationService,payloadStore.getCustomerInformation()));
        quotationParticipant.setTerm(getTermDAO(input));
        quotationParticipant.setRefunds(getRefundsDAO(input));
        quotationParticipant.setCreationUser(input.getCreationUser());
        quotationParticipant.setUserAudit(input.getUserAudit());

        return quotationParticipant;
    }

    private static InsuredLifeDAO.RefundsDAO getRefundsDAO(QuotationLifeDTO input){
        InsuredLifeDAO insuredLifeDAO = new InsuredLifeDAO();
        InsuredLifeDAO.RefundsDAO refundsDAO = insuredLifeDAO.new RefundsDAO();

        RefundsDTO returnPercentage = getRefundPerUnitType(input,ConstantUtils.PERCENTAGE);
        RefundsDTO returnAmount = getRefundPerUnitType(input,ConstantUtils.AMOUNT);

        refundsDAO.setRefundPer((Objects.nonNull(returnPercentage) && returnPercentage.getUnit().getPercentage() != null) ?
                returnPercentage.getUnit().getPercentage() : null);
        refundsDAO.setTotalReturnAmount((Objects.nonNull(returnAmount) && returnAmount.getUnit().getAmount() != null) ?
                returnAmount.getUnit().getAmount(): null);
        refundsDAO.setCurrencyId(getCurrencyFromInput(input));

        return refundsDAO;
    }

    private static InsuredLifeDAO.TermDAO getTermDAO(QuotationLifeDTO input){
        InsuredLifeDAO insuredLife = new InsuredLifeDAO();
        InsuredLifeDAO.TermDAO termDAO = insuredLife.new TermDAO();

        BigDecimal periodNumber = getTermNumberFromInput(input);

        termDAO.setPeriodNumber(periodNumber);
        termDAO.setPeriodType(input.getTerm() != null ? ConstantUtils.ANNUAL : null);

        return termDAO;
    }

    private static ParticipantDAO getParticipantDao(QuotationLifeDTO input,
                                             ApplicationConfigurationService applicationConfigurationService,
                                             CustomerListASO customerInformation){
        ParticipantDAO participantDAO = new ParticipantDAO();

        participantDAO.setCustomerEntryDate(getCustomerEntryDate(input));
        participantDAO.setParticipantRoleId(BigDecimal.valueOf(ConstantUtils.ROLE_INSURED));

        ParticipantDTO participantDTO = getParticipantsArrayFromInput(input);
        if (Objects.nonNull(participantDTO)) {
            setParticipantPropertiesFromInput(participantDAO, participantDTO,applicationConfigurationService);
        } else {
            CustomerBO customerData = safeGetCustomerData(customerInformation);
            setParticipantPreportiesFromCustomerData(participantDAO, input, customerData,applicationConfigurationService);
        }

        return participantDAO;
    }

    private static BigDecimal getTermNumberFromInput(QuotationLifeDTO input) {
        return input.getTerm() != null ? BigDecimal.valueOf(input.getTerm().getNumber()) : BigDecimal.ZERO;
    }

    private static RefundsDTO getRefundPerUnitType(QuotationLifeDTO input,String unitType) {
        if (CollectionUtils.isEmpty(input.getRefunds())) {
            return null;
        } else {
            return input.getRefunds().stream()
                    .filter(refundsDTO -> refundsDTO.getUnit() != null)
                    .filter(refundsDTO -> unitType.equals(refundsDTO.getUnit().getUnitType()))
                    .findFirst()
                    .orElse(null);
        }
    }

    private static BigDecimal safeGetInsuredAmount(QuotationLifeDTO input) {
        return (Objects.nonNull(input.getInsuredAmount())) ? input.getInsuredAmount().getAmount() : null;
    }
    private static LocalDate getCustomerEntryDate(QuotationLifeDTO input) {
        if (CollectionUtils.isEmpty(input.getParticipants()) ||
                (Objects.isNull(input.getParticipants().get(0)) ||
                        Objects.isNull(input.getParticipants().get(0).getId()) ||
                        ConstantUtils.BLANK.equals(input.getParticipants().get(0).getId()))) {
            return null;
        } else {
            return LocalDate.now();
        }
    }
    private static String getCurrencyFromInput(QuotationLifeDTO input) {
        return (Objects.nonNull(input.getInsuredAmount())) ? input.getInsuredAmount().getCurrency() : null;
    }

    private static String safeGetInsuredId(ParticipantDTO participantDTO) {
        return Objects.nonNull(participantDTO) && Objects.nonNull(participantDTO.getId()) ? participantDTO.getId() : null;
    }
    private static ParticipantDTO getParticipantsArrayFromInput(QuotationLifeDTO input) {
        return (!CollectionUtils.isEmpty(input.getParticipants())) ? input.getParticipants().get(0) : null;
    }

    private static CustomerBO safeGetCustomerData(CustomerListASO customerInformation) {
        if (Objects.nonNull(customerInformation) && !CollectionUtils.isEmpty(customerInformation.getData())) {
            return customerInformation.getData().get(0);
        }
        return null;
    }

    private static void setParticipantPropertiesFromInput(ParticipantDAO participantDAO, ParticipantDTO participant,
                                                   ApplicationConfigurationService applicationConfigurationService) {

        ContractDetailsDTO tipoContratoEmail = getGroupedByTypeContactDetailDTO(participant.getContactDetails(), ConstantUtils.EMAIL);
        ContractDetailsDTO tipoContratoMov = getGroupedByTypeContactDetailDTO(participant.getContactDetails(), ConstantUtils.MOBILE_NUMBER);
        String documentType = getDocumentTypeFromParticipant(participant);

        participantDAO.setInsuredId(safeGetInsuredId(participant));
        participantDAO.setCustomerDocumentType((documentType != null) ? applicationConfigurationService.getProperty(documentType) : null);
        participantDAO.setPersonalId(participant.getIdentityDocument().getDocumentNumber());
        participantDAO.setIsBbvaCustomerType(isBBVAClient(participant.getId()) ? ConstantUtils.YES_S : ConstantUtils.NO_N);
        participantDAO.setInsuredCustomerName(participant.getFirstName());
        participantDAO.setClientLastName(getLastNameFromParticipant(participant));
        participantDAO.setCustomerBirthDate(getBirthDateFromParticipant(participant));
        participantDAO.setGenderId((participant.getGender() != null) ? participant.getGender().getId().substring(0,1) : null);
        participantDAO.setContactDetails(getContactDetailsDAOFromInputParticipant(tipoContratoEmail, tipoContratoMov));
    }

    private static LocalDate getBirthDateFromParticipant(ParticipantDTO participant){
        if(participant.getBirthDate() != null){
            return ConvertUtils.convertDateToLocalDate(participant.getBirthDate());
        }else{
            return null;
        }
    }

    private static String getLastNameFromParticipant(ParticipantDTO participant) {
        return (participant.getLastName() != null ? participant.getLastName() : ConstantUtils.BLANK) +
                ConstantUtils.SLASH + (participant.getSecondLastName() != null ? participant.getSecondLastName() : ConstantUtils.BLANK);
    }

    private static String getDocumentTypeFromParticipant(ParticipantDTO participant) {
        return (participant.getIdentityDocument() != null && participant.getIdentityDocument().getDocumentType() != null) ?
                participant.getIdentityDocument().getDocumentType().getId() : null;
    }

    private static void setParticipantPreportiesFromCustomerData(ParticipantDAO participantDAO, QuotationLifeDTO input,
                                  CustomerBO customerData,ApplicationConfigurationService applicationConfigurationService) {

        if(Objects.nonNull(customerData)){
            participantDAO.setInsuredCustomerName(getSafeFirstNameFromCustomerData(customerData));
            participantDAO.setClientLastName(getLastNameFromCustomerData(customerData));
            participantDAO.setCustomerBirthDate(getBirthDateFromCustomerData(customerData));
            participantDAO.setGenderId(customerData.getGender() != null ? customerData.getGender().getId().substring(0, 1) : null);
            String documentType = getDocumentTypeFromCUstomerData(customerData);
            participantDAO.setCustomerDocumentType((documentType != null) ? applicationConfigurationService.getProperty(documentType) : null);
            participantDAO.setPersonalId(getDocumentNumberFromCustomerData(customerData));

            ContactDetailsBO tipoContratoEmail;
            ContactDetailsBO tipoContratoMov;

            if(customerData.getContactDetails() != null) {
                tipoContratoEmail = getGroupedByTypeContactDetailBO(customerData.getContactDetails(), ConstantUtils.EMAIL);
                tipoContratoMov = getGroupedByTypeContactDetailBO(customerData.getContactDetails(), ConstantUtils.NUMBER);
            }
            else{
                tipoContratoEmail = null;
                tipoContratoMov = null;
            }

            participantDAO.setContactDetails(getContactDetailsASOFromCustomerBO(tipoContratoEmail, tipoContratoMov));

        }else{
            HolderDTO holderDTO = input.getHolder();
            participantDAO.setCustomerDocumentType(
                    applicationConfigurationService.getProperty(holderDTO.getIdentityDocument().getDocumentType().getId()));
            participantDAO.setPersonalId(input.getHolder().getIdentityDocument().getDocumentNumber());
            participantDAO.setInsuredCustomerName(Objects.nonNull(holderDTO.getFirstName()) ? holderDTO.getFirstName() : null);
            participantDAO.setClientLastName(Objects.nonNull(holderDTO.getLastName()) ? holderDTO.getLastName() : null);
        }

        participantDAO.setInsuredId(input.getHolder().getId());
        participantDAO.setIsBbvaCustomerType(isBBVAClient(input.getHolder().getId()) ? ConstantUtils.YES_S : ConstantUtils.NO_N);

    }

    private static String getDocumentNumberFromCustomerData(CustomerBO customerData) {
        return (!CollectionUtils.isEmpty(customerData.getIdentityDocuments())
                && customerData.getIdentityDocuments().get(0) != null) ?
                customerData.getIdentityDocuments().get(0).getDocumentNumber() : null;
    }

    private static String getDocumentTypeFromCUstomerData(CustomerBO customerData) {
        return (!CollectionUtils.isEmpty(customerData.getIdentityDocuments())
                && customerData.getIdentityDocuments().get(0).getDocumentType() != null) ?
                customerData.getIdentityDocuments().get(0).getDocumentType().getId() : null;
    }

    private static ContactDetailsDAO getContactDetailsDAOFromInputParticipant(ContractDetailsDTO tipoContratoEmail, ContractDetailsDTO tipoContratoMov) {
        ContactDetailsDAO contactDetailsDAO = new ContactDetailsDAO();
        contactDetailsDAO.setPhoneId(Objects.nonNull(tipoContratoMov) ? tipoContratoMov.getContact().getNumber() : null);
        contactDetailsDAO.setUserEmailPersonalDesc(Objects.nonNull(tipoContratoEmail) ? tipoContratoEmail.getContact().getAddress() : null);
        return contactDetailsDAO;
    }

    private static ContactDetailsDAO getContactDetailsASOFromCustomerBO(ContactDetailsBO tipoContratoEmail, ContactDetailsBO tipoContratoMov) {
        ContactDetailsDAO contactDetailsDAO = new ContactDetailsDAO();
        contactDetailsDAO.setPhoneId((Objects.nonNull(tipoContratoMov) && tipoContratoMov.getContact() != null) ? tipoContratoMov.getContact() : null);
        contactDetailsDAO.setUserEmailPersonalDesc((Objects.nonNull(tipoContratoEmail) && tipoContratoEmail.getContact() != null) ? tipoContratoEmail.getContact() : null);
        return contactDetailsDAO;
    }

    public static LocalDate getBirthDateFromCustomerData(CustomerBO customerData) {
        Date birthday=null;
        if(customerData.getBirthData() != null && customerData.getBirthData().getBirthDate() != null) {
            LocalDate lc = LocalDate.parse(customerData.getBirthData().getBirthDate(), DateTimeFormatter.ofPattern(ConstantUtils.PATTERN_DATE));
            ZoneId localZone = ZoneId.of(ConstantUtils.ZONE_AMERICA_LIMA);
            birthday = Date.from(lc.atStartOfDay(localZone).toInstant());
        }
        return (Objects.nonNull(birthday)) ? birthday.toInstant().atZone(ConstantUtils.ZONE_ID).toLocalDate() : null;
    }
    private static String getSafeFirstNameFromCustomerData(CustomerBO customerData){
        return customerData.getFirstName() != null ? customerData.getFirstName() : null;
    }
    private static String getLastNameFromCustomerData(CustomerBO customerData){

        StringBuilder lastName = new StringBuilder();

        if (customerData.getLastName() != null) {
            lastName.append(customerData.getLastName());
        }else{
            lastName.append(ConstantUtils.BLANK_SPACE);
        }

        lastName.append(ConstantUtils.SLASH);

        if (customerData.getSecondLastName() != null) {
            lastName.append(customerData.getSecondLastName());
        }else{
            lastName.append(ConstantUtils.BLANK_SPACE);
        }

        return lastName.toString();
    }

    public static ContractDetailsDTO getGroupedByTypeContactDetailDTO(List<ContractDetailsDTO> contacts, String tipoContacto) {
        return !CollectionUtils.isEmpty(contacts)
        ? contacts.stream()
                .filter(contactInfo ->
                        contactInfo != null &&
                                contactInfo.getContact() != null &&
                                tipoContacto.equals(contactInfo.getContact().getContactDetailType())
                ).findFirst()
                .orElse(null)
        : null;
    }
    public static ContactDetailsBO getGroupedByTypeContactDetailBO(List<ContactDetailsBO> customer, String tipoContacto) {
        return customer.stream()
                .filter(contactInfo ->
                        contactInfo != null &&
                                contactInfo.getContact() != null &&
                                tipoContacto.equals(contactInfo.getContactType().getId())
                ).findFirst().orElse(null);
    }


}

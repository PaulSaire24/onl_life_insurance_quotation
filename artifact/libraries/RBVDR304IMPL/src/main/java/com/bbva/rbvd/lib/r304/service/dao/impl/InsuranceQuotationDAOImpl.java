package com.bbva.rbvd.lib.r304.service.dao.impl;

import com.bbva.elara.configuration.manager.application.ApplicationConfigurationService;
import com.bbva.pisd.dto.insurance.aso.CustomerListASO;
import com.bbva.pisd.dto.insurance.bo.ContactDetailsBO;
import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.pisd.dto.insurance.dao.InsuranceQuotationDAO;
import com.bbva.pisd.lib.r350.PISDR350;
import com.bbva.rbvd.dto.lifeinsrc.commons.IdentityDocumentDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.InsurancePlanDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.RefundsDTO;
import com.bbva.rbvd.dto.lifeinsrc.commons.TermDTO;
import com.bbva.rbvd.dto.lifeinsrc.dao.CommonsLifeDAO;
import com.bbva.rbvd.dto.lifeinsrc.dao.quotation.EasyesQuotationDAO;
import com.bbva.rbvd.dto.lifeinsrc.quotation.QuotationLifeDTO;
import com.bbva.rbvd.dto.lifeinsrc.rimac.quotation.QuotationLifeBO;
import com.bbva.rbvd.dto.lifeinsrc.simulation.ContractDetailsDTO;
import com.bbva.rbvd.dto.lifeinsrc.simulation.ParticipantDTO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDProperties;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDValidation;
import com.bbva.rbvd.lib.r304.pattern.impl.QuotationStore;
import com.bbva.rbvd.lib.r304.service.dao.IInsuranceQuotationDAO;
import com.bbva.rbvd.lib.r304.transfer.PayloadStore;
import com.bbva.rbvd.lib.r304.transform.bean.InsuranceQuotationBean;
import com.bbva.rbvd.lib.r304.transform.map.InsuranceQuotationMap;
import com.bbva.rbvd.lib.r304.impl.util.ConstantUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.SimpleDateFormat;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
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
    public CommonsLifeDAO createQuotationParticipant(PayloadStore payloadStore, ApplicationConfigurationService applicationConfigurationService) {
        CommonsLifeDAO quotationParticipant = new CommonsLifeDAO();
        CustomerListASO customerInformation=payloadStore.getCustomerInformation();
        LOGGER.info("***** InsuranceQuotationDAOImpl | argument customerInformation: {} *****",customerInformation);

        EasyesQuotationDAO quotationDao = payloadStore.getMyQuotation();
        QuotationLifeDTO input = payloadStore.getInput();

        BigDecimal periodNumber = safeBigDecimal(input, QuotationLifeDTO::getTerm, TermDTO::getNumber, BigDecimal.ZERO);
        List<RefundsDTO> refundPercentaje =calculateTotalReturnAmountOrRefundPercentaje(input.getRefunds(),PERCENTAGE); ;
        List<RefundsDTO> totalAmount =calculateTotalReturnAmountOrRefundPercentaje(input.getRefunds(),AMOUNT); ;
        InsurancePlanDTO plan = input.getProduct().getPlans().get(0);

        quotationParticipant.setPolicyQuotaInternalId(input.getId());
        quotationParticipant.setInsuranceProductId(quotationDao.getInsuranceProductId());
        quotationParticipant.setInsuranceModalityType(plan.getId());
        quotationParticipant.setInsuredAmount(safeGetInsuredAmount(input));
        quotationParticipant.setCurrencyId(safeGetInsuredCurrency(input));
        quotationParticipant.setPeriodType("A");
        if(Objects.isNull(input.getTerm())){
            quotationParticipant.setPeriodType(null);
        }
        quotationParticipant.setPeriodNumber(periodNumber);
        quotationParticipant.setRefundPer((!CollectionUtils.isEmpty(refundPercentaje) && refundPercentaje.get(0).getUnit().getPercentage() != null) ?
                refundPercentaje.get(0).getUnit().getPercentage(): null);
        quotationParticipant.setTotalReturnAmount((!CollectionUtils.isEmpty(totalAmount) && totalAmount.get(0).getUnit().getAmount() != null) ?
                totalAmount.get(0).getUnit().getAmount(): null);
        quotationParticipant.setCustomerEntryDate(LocalDate.now());
        quotationParticipant.setCreationUser(input.getCreationUser());
        quotationParticipant.setUserAudit(input.getUserAudit());

        ParticipantDTO participant = safeGetParticipant(input);
        if (Objects.nonNull(participant)) {
            setParticipantProperties(quotationParticipant, participant,applicationConfigurationService,input);
        } else {
            CustomerBO customerData = safeGetCustomerData(customerInformation, input);
            setCustomerDataProperties(quotationParticipant, input, customerData,applicationConfigurationService);
        }
        quotationParticipant.setInsuredId(safeGetInsuredId(input));
        quotationParticipant.setParticipantRoleId(BigDecimal.valueOf(ConstantUtils.IS_INSURED));

        return quotationParticipant;
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

    private List<RefundsDTO> calculateTotalReturnAmountOrRefundPercentaje(List<RefundsDTO> input,String tipo) {
        List<RefundsDTO> filteredRefunds = input.stream()
                .filter(refundInfo ->
                        refundInfo != null &&
                                refundInfo.getUnit() != null &&
                                tipo.equals(refundInfo.getUnit().getUnitType())
                )
                .collect(Collectors.toList());
        return filteredRefunds;

    }



    private BigDecimal safeGetInsuredAmount(QuotationLifeDTO input) {
        return (Objects.nonNull(input.getInsuredAmount())) ? input.getInsuredAmount().getAmount() : null;
    }

    private String safeGetInsuredCurrency(QuotationLifeDTO input) {
        return (Objects.nonNull(input.getInsuredAmount())) ? input.getInsuredAmount().getCurrency() : null;
    }


    private String safeGetInsuredId(QuotationLifeDTO input) {
        return (Objects.nonNull(input.getParticipants()) && Objects.nonNull(input.getParticipants().get(0).getId())) ? input.getParticipants().get(0).getId() : input.getHolder().getId();
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

    private void setParticipantProperties(CommonsLifeDAO quotationParticipant, ParticipantDTO participant,ApplicationConfigurationService applicationConfigurationService,QuotationLifeDTO input) {
        List<ContractDetailsDTO> tipoContratoEmail = getGroupedByTypeContactDetail(participant.getContactDetails(), EMAIL);
        List<ContractDetailsDTO> tipoContratoMov = getGroupedByTypeContactDetail(participant.getContactDetails(), MOBILE_NUMBER);
        String lastName = (participant.getLastName() != null ? participant.getLastName() : "") +
                "|" + (participant.getSecondLastName() != null ? participant.getSecondLastName() : "");

        Instant instant = participant.getBirthDate().toInstant();
        LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
        String documentType =(participant.getIdentityDocument() != null && participant.getIdentityDocument().getDocumentType() != null) ?
                participant.getIdentityDocument().getDocumentType().getId(): null;
        quotationParticipant.setIsBbvaCustomerType(isBBVAClient(participant.getId()) ? ConstantUtils.YES_S : ConstantUtils.NO_N);
        quotationParticipant.setInsuredCustomerName(participant.getFirstName());
        quotationParticipant.setPersonalId(participant.getIdentityDocument().getDocumentNumber());
        quotationParticipant.setCustomerDocumentType((documentType != null) ? applicationConfigurationService.getProperty(documentType):null);
        quotationParticipant.setClientLastName(lastName);
        quotationParticipant.setUserEmailPersonalDesc((!CollectionUtils.isEmpty(tipoContratoEmail) && tipoContratoEmail.get(0).getContact() != null) ?
                tipoContratoEmail.get(0).getContact().getAddress() : null);
        quotationParticipant.setPhoneId((!CollectionUtils.isEmpty(tipoContratoMov) && tipoContratoMov.get(0).getContact() != null) ?
                tipoContratoMov.get(0).getContact().getNumber() : null);
        quotationParticipant.setCustomerBirthDate(localDate);
        quotationParticipant.setInsuredId(safeGetInsuredId(input));
        quotationParticipant.setGenderId((participant.getGender() != null) ? participant.getGender().getId() : null);

    }

    private void setCustomerDataProperties(CommonsLifeDAO quotationParticipant, QuotationLifeDTO input, CustomerBO customerData,ApplicationConfigurationService applicationConfigurationService) {
        List<ContactDetailsBO> tipoContratoEmail = new ArrayList<>();
        List<ContactDetailsBO> tipoContratoMov = new ArrayList<>();
        if(Objects.nonNull(customerData)&&customerData.getContactDetails()!=null) {
             tipoContratoEmail = getGroupedByTypeContactDetailBO(customerData.getContactDetails(), EMAIL);
             tipoContratoMov = getGroupedByTypeContactDetailBO(customerData.getContactDetails(), MOBILE_NUMBER);
        }
        else{
            tipoContratoEmail = null;
             tipoContratoMov =null;
        }
        String documentType =(Objects.nonNull(customerData)&&!CollectionUtils.isEmpty(customerData.getIdentityDocuments()) &&
                customerData.getIdentityDocuments().size() > 0 && customerData.getIdentityDocuments().get(0).getDocumentType() != null) ?
                customerData.getIdentityDocuments().get(0).getDocumentType().getId() : null;
        quotationParticipant.setCustomerDocumentType((documentType != null) ? applicationConfigurationService.getProperty(documentType):null);

        quotationParticipant.setInsuredCustomerName(Objects.nonNull(customerData) && (customerData.getFirstName())!=null?
                customerData.getFirstName() : null);

        quotationParticipant.setIsBbvaCustomerType(isBBVAClient(input.getHolder().getId()) ? ConstantUtils.YES_S : ConstantUtils.NO_N);
        quotationParticipant.setClientLastName(getLastName(customerData));
        quotationParticipant.setPersonalId(
                Optional.ofNullable(customerData)
                        .map(CustomerBO::getIdentityDocuments)
                        .filter(documents -> !documents.isEmpty())
                        .map(documents -> documents.get(0).getDocumentNumber())
                        .orElse(null)
        );

        quotationParticipant.setPhoneId((!CollectionUtils.isEmpty(tipoContratoMov) && tipoContratoMov.get(0).getContact() != null) ? tipoContratoMov.get(0).getContact() : null);
        quotationParticipant.setUserEmailPersonalDesc((!CollectionUtils.isEmpty(tipoContratoEmail) && tipoContratoEmail.get(0).getContact() != null) ? tipoContratoEmail.get(0).getContact() : null);
        quotationParticipant.setInsuredId(input.getHolder().getId());
        quotationParticipant.setGenderId((Objects.nonNull(customerData)&&(customerData.getGender() != null) ? customerData.getGender().getId().substring(0, 1) : null));

    }
    private String getLastName(CustomerBO customerData){

        String lastName = "";

        if (customerData != null) {
            if (customerData.getLastName() != null) {
                lastName += customerData.getLastName();
            }

            lastName += "|";

            if (customerData.getSecondLastName() != null) {
                lastName += customerData.getSecondLastName();
            }

    }
        return lastName;
    }
    @Override
    public void insertSimulationParticipant(Map<String, Object> argumentForSaveParticipant) {
        int idNewSimulation = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_INSERT_PARTICIPANT_QOUTATION.getValue(),argumentForSaveParticipant);
        if(idNewSimulation != 1){
            throw RBVDValidation.build(RBVDErrors.QUOTATION_INSERTION_WAS_WRONG);
        }
    }
    @Override
    public void updateSimulationParticipant(Map<String, Object> argumentForSaveParticipant) {
        int idNewSimulation = this.pisdR350.executeInsertSingleRow(RBVDProperties.QUERY_UPDATE_INSURANCE_QUOTATION.getValue(),argumentForSaveParticipant);
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

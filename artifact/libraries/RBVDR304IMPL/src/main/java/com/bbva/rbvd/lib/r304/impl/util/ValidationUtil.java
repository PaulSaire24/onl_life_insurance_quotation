package com.bbva.rbvd.lib.r304.impl.util;

import com.bbva.pisd.dto.insurance.bo.ContactDetailsBO;
import com.bbva.pisd.dto.insurance.bo.customer.CustomerBO;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDValidation;
import org.apache.commons.lang3.StringUtils;
import com.bbva.rbvd.dto.lifeinsrc.simulation.ContractDetailsDTO;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


import static com.bbva.rbvd.lib.r304.impl.util.ConstantUtils.*;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.mapping;
import static org.springframework.util.CollectionUtils.isEmpty;

public final class ValidationUtil {

    private ValidationUtil() {
    }

    public static void validateSelectionQueries(Map responseQuery, RBVDErrors error) {
        if(isEmpty(responseQuery)) {
            throw RBVDValidation.build(error);
        }
    }

    public static void validateInsertionQueries(Integer result, RBVDErrors error) {
        if(result != 1) {
            throw RBVDValidation.build(error);
        }
    }

    public static String validateIsDataTreatment(Boolean isDataTreatment){
        if(Objects.nonNull(isDataTreatment)){
            return Boolean.TRUE.equals(isDataTreatment) ? "S" : "N";
        }else{
            return "N";
        }
    }
    public static boolean isBBVAClient(String clientId){
        return StringUtils.isNotEmpty(clientId) && !(clientId.matches(REGEX_CONTAIN_ONLY_LETTERS) && clientId.matches(REGEX_CONTAIN_ONLY_NUMBERS) && clientId.length()>CLIENT_BANK_LENGHT);
    }
    public static List<ContractDetailsDTO> getGroupedByTypeContactDetail(List<ContractDetailsDTO> customer, String tipoContacto) {
        List<ContractDetailsDTO> filteredContacts = customer.stream()
                .filter(contactInfo ->
                        contactInfo != null &&
                                contactInfo.getContact() != null &&
                                tipoContacto.equals(contactInfo.getContact().getContactDetailType())
                )
                .collect(Collectors.toList());
        return filteredContacts;
    }

}

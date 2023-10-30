package com.bbva.rbvd.lib.r304.impl.util;

import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDValidation;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;

import com.bbva.rbvd.lib.r304.impl.util.ConstantUtils;

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
        return StringUtils.isNotEmpty(clientId) && !(clientId.matches(ConstantUtils.REGEX_CONTAIN_ONLY_LETTERS) &&
                clientId.matches(ConstantUtils.REGEX_CONTAIN_ONLY_NUMBERS) &&
                clientId.length() > ConstantUtils.CLIENT_BANK_LENGHT);
    }


}

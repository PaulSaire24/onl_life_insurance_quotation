package com.bbva.rbvd.lib.r304.impl.util;

import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDValidation;

import java.util.Map;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

public final class ValidationUtil {

    private ValidationUtil() {
    }

    public static void validateServicesResponse(Object object, RBVDErrors error) {
        if(isNull(object)) {
            throw RBVDValidation.build(error);
        }
    }

    public static void validateSelectionQueries(Map responseQuery, RBVDErrors error) {
        if(isEmpty(responseQuery)) {
            throw RBVDValidation.build(error);
        }
    }

    public static void validateInsertionQueries(final Integer result, final RBVDErrors error) {
        if(result != 1) {
            throw RBVDValidation.build(error);
        }
    }

}

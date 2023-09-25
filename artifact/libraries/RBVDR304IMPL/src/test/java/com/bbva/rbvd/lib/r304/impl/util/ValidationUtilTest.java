package com.bbva.rbvd.lib.r304.impl.util;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import org.junit.Test;

import java.util.HashMap;



public class ValidationUtilTest{

    @Test(expected = BusinessException.class)
    public void testValidateSelectionQueries() {
        ValidationUtil.validateSelectionQueries(new HashMap<>(), RBVDErrors.INVALID_PRODUCT_TYPE_AND_MODALITY_TYPE);
    }

    @Test(expected = BusinessException.class)
    public void testValidateInsertionQueries() {
        ValidationUtil.validateInsertionQueries(0, RBVDErrors.INVALID_RIMAC_QUOTATION_ID);
    }
}
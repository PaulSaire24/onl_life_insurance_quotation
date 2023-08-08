package com.bbva.rbvd.lib.r304.impl.util;

import com.bbva.apx.exception.business.BusinessException;
import com.bbva.rbvd.dto.lifeinsrc.utils.RBVDErrors;
import org.junit.Test;

import static org.mockito.Matchers.anyMap;


public class ValidationUtilTest{

    @Test(expected = BusinessException.class)
    public void testValidateSelectionQueries() {
        ValidationUtil.validateSelectionQueries(anyMap(), RBVDErrors.INVALID_RIMAC_QUOTATION_ID);
    }

    @Test(expected = BusinessException.class)
    public void testValidateInsertionQueries() {
        ValidationUtil.validateInsertionQueries(0, RBVDErrors.INVALID_RIMAC_QUOTATION_ID);
    }
}
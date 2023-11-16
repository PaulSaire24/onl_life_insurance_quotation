package com.bbva.rbvd.lib.r304.impl.util;

import java.time.ZoneId;

public class ConstantUtils {

    private ConstantUtils(){}

    public static final String ANNUAL="A";
    public static final String AMOUNT="AMOUNT";
    public static final String VALIDATE_IF_QUOTATION_LIFE_EXISTS ="PISD.VALIDATE_IF_QUOTATION_LIFE_EXISTS";
    public static final String UPDATE_INSURED_QUOTATION_LIFE ="PISD.UPDATE_INSURED_QUOTATION_LIFE";
    public static final String FIELD_RESULT_NUMBER_LIFE ="RESULT_NUMBER_LIFE";
    public static final String DELETE_INSURED_QUOTATION_LIFE ="PISD.DELETE_INSURED_QUOTATION_LIFE";
    public static final String INSERT_INSURED_QUOTATION_LIFE ="PISD.INSERT_INSURED_QUOTATION_LIFE";
    public static final String BLANK ="";
    public static final int CLIENT_BANK_LENGHT = 8;
    public static final String EMAIL="EMAIL";
    public static final int ROLE_INSURED_ID = 2;
    public static final String MOBILE_NUMBER="MOBILE";
    public static final String NO_N = "N";
    public static final String NUMBER="MOBILE_NUMBER";
    public static final String PERCENTAGE="PERCENTAGE";
    public static final String REGEX_CONTAIN_ONLY_LETTERS=".*[a-zA-Z].*";
    public static final String REGEX_CONTAIN_ONLY_NUMBERS=".*[0-9].*";
    public static final String VERTICAL_BAR="|";
    public static final String YES_S = "S";
    public static final String BLANK_SPACE = " ";
    private static final String GMT_TIME_ZONE = "GMT";
    public static  final String ZONE_AMERICA_LIMA = "America/Lima";
    public static final ZoneId ZONE_ID = ZoneId.of(GMT_TIME_ZONE);
    public static final String PATTERN_DATE = "yyyy-MM-dd";
}

package com.bbva.rbvd.lib.r304.impl.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class ConvertUtils {

    public static LocalDate convertDateToLocalDate(Date date){
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

}

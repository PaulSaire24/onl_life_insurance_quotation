package com.bbva.rbvd.lib.r304.impl.util;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Objects;

public class ConvertUtils {

    private ConvertUtils(){}

    public static LocalDate convertDateToLocalDate(Date date){
        if(!Objects.nonNull(date)){
            return null;
        }
        return date.toInstant().atZone(ZoneId.of("GMT")).toLocalDate();
    }

    public static Date parseFecha(String fecha) {
        LocalDate lc = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        ZoneId localZone = ZoneId.of("America/Lima");
        return Date.from(lc.atStartOfDay(localZone).toInstant());
    }


}

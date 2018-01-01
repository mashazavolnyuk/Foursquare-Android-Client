package com.mashazavolnyuk.client.utils;
import java.util.Calendar;
public class DateUtil {

    public static String getDataForResponse(){
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH));
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        return year+month+day;
    }

}

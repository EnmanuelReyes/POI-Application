package me.enmanuel.util;

import java.sql.Time;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;

/**
 * Created by IntelliJ IDEA.
 * User: Enmanuel
 * Date: 04/08/2016
 * Time: 10:49 PM
 */
public class DateUtil {

    public static boolean isWorkDay(DayOfWeek dayOfWeek) {
        switch (dayOfWeek) {
            case SATURDAY:
            case SUNDAY:
                return false;
            default:
                return true;
        }
    }


    public static LocalDate toLocalDate(Date value) {
        if (value == null) {
            return null;
        }
        return new java.sql.Date(value.getTime()).toLocalDate();
    }

    public static Date toDate(LocalDate localDate) {
        return java.sql.Date.valueOf(localDate);
    }

    public static LocalTime toLocalTime(Time time) {
        return time.toLocalTime();

    }

}

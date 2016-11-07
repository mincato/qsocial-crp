package com.qsocialnow.util;

import java.util.Calendar;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;

public class DateTimeBoxComponent {

    public static Long mergeDate(Long date, Long time, TimeZone timeZone) {
        Calendar calDate = Calendar.getInstance(timeZone);
        calDate.setTimeInMillis(date);
        if (time != null) {
            Calendar calTime = Calendar.getInstance(timeZone);
            calTime.setTimeInMillis(time);
            calDate.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
            calDate.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
            calDate.set(Calendar.SECOND, calTime.get(Calendar.SECOND));
        }
        return calDate.getTimeInMillis();
    }

    public static Long extractTime(TimeZone timeZone, Calendar cal) {
        Calendar calTime = Calendar.getInstance(timeZone);
        calTime = DateUtils.truncate(calTime, Calendar.DATE);
        calTime.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
        calTime.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
        calTime.set(Calendar.SECOND, cal.get(Calendar.SECOND));
        calTime.set(Calendar.MILLISECOND, 0);
        return calTime.getTimeInMillis();
    }

    public static Long truncateTimeToday(TimeZone timeZone) {
        Calendar cal = Calendar.getInstance(timeZone);
        cal = DateUtils.truncate(cal, Calendar.DATE);
        return cal.getTimeInMillis();
    }

}

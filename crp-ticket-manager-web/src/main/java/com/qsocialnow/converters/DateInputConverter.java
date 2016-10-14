package com.qsocialnow.converters;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.web.Attributes;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

public class DateInputConverter implements Converter {

    @Override
    public Object coerceToBean(Object value, Component comp, BindContext ctx) {
        Long outputTimestamp = null;
        if (value != null) {
            Boolean endDay = (Boolean) ctx.getConverterArg("endDay");
            Date date = (Date) value;
            if (Boolean.TRUE.equals(endDay)) {
                Calendar calendar = Calendar.getInstance();
                TimeZone timeZone = (TimeZone) Executions.getCurrent().getSession()
                        .getAttribute(Attributes.PREFERRED_TIME_ZONE);
                if (timeZone != null) {
                    calendar.setTimeZone(timeZone);
                }
                calendar.setTime(date);
                calendar.set(Calendar.HOUR_OF_DAY, 23);
                calendar.set(Calendar.MINUTE, 59);
                calendar.set(Calendar.SECOND, 59);
                date = calendar.getTime();
            }
            outputTimestamp = date.getTime();
        }
        return outputTimestamp;
    }

    @Override
    public Object coerceToUi(Object value, Component comp, BindContext ctx) {
        Date outputDate = null;
        if (value != null) {
            Long timestamp = (Long) value;
            outputDate = new Date(timestamp);
        }
        return outputDate;
    }

}

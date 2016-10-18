package com.qsocialnow.converters;

import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;
import org.zkoss.zk.ui.Executions;

public class DateConverter implements Converter {

    private String timezone;

    public DateConverter(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public Object coerceToBean(Object arg0, Component arg1, BindContext arg2) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Object coerceToUi(Object value, Component comp, BindContext ctx) {
        String outputDate = null;
        if (value != null) {
            DateTime dt = new DateTime(value);
            Date date;
            if (timezone != null) {
                date = dt.withZone(DateTimeZone.forTimeZone(TimeZone.getTimeZone(timezone))).toLocalDateTime().toDate();
            } else {
                date = dt.toLocalDateTime().toDate();
            }
            Locale locale = (Locale) Executions.getCurrent().getSession()
                    .getAttribute(org.zkoss.web.Attributes.PREFERRED_LOCALE);
            if (locale != null) {
                outputDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT, locale).format(date);
            } else {
                outputDate = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(date);
            }

        }
        return outputDate;
    }

}

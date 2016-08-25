package com.qsocialnow.eventresolver.filters;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.qsocialnow.eventresolver.model.event.InPutBeanDocument;

@Component("periodDetectionCriteriaFilter")
public class PeriodDetectionCriteriaFilter implements DetectionCriteriaFilter {

    private static final Logger log = LoggerFactory.getLogger(PeriodDetectionCriteriaFilter.class);

    @Override
    public boolean match(InPutBeanDocument message, String attributeValues) {
        log.info("Executing period message filter");
        boolean match = false;
        if (attributeValues != null) {
            String[] dates = attributeValues.split("\\|", 2);
            Date startDate = parseDate(dates[0]);
            Date endDate = dates.length > 1 ? parseDate(dates[1]) : null;
            Date messageDate = message.getFechaCreacion();
            if (startDate != null && endDate != null) {
                match = messageDate.after(startDate) && messageDate.before(endDate);
            } else if (startDate != null) {
                match = messageDate.after(startDate);
            } else if (endDate != null) {
                match = messageDate.before(endDate);
            }
        }
        return match;
    }

    private Date parseDate(String dateTime) {
        if (dateTime != null && !dateTime.isEmpty()) {
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy-HH:mm");
            try {
                return sdf.parse(dateTime);
            } catch (ParseException e) {
                log.error("There was an error trying to parse dateTime", e);
            }
        }
        return null;
    }

}

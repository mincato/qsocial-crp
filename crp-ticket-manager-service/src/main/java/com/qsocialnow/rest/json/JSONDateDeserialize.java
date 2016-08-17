package com.qsocialnow.rest.json;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

@Component
public class JSONDateDeserialize extends JsonDeserializer<Date> {

    private static final String DATE_FORMAT = "dd/MM/yyyy";

    @Override
    public Date deserialize(JsonParser jp, DeserializationContext dc) throws IOException, JsonProcessingException {
        try {
            DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
            String date = jp.getText();
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(JSONDateDeserialize.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}

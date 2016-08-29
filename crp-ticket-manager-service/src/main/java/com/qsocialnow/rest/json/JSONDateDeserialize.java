package com.qsocialnow.rest.json;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.stereotype.Component;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

@Component
public class JSONDateDeserialize implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
        try {
            DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.SHORT);
            String date = json.getAsString();
            return dateFormat.parse(date);
        } catch (ParseException ex) {
            Logger.getLogger(JSONDateDeserialize.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

}

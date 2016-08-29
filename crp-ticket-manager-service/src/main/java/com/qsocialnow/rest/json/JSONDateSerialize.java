/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.qsocialnow.rest.json;

import java.lang.reflect.Type;
import java.text.DateFormat;
import java.util.Date;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JSONDateSerialize implements JsonSerializer<Date> {

    @Override
    public JsonElement serialize(Date date, Type type, JsonSerializationContext context) {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT);
        return new JsonPrimitive(df.format(date));
    }
}

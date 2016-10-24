package com.qsocialnow.service;

import java.util.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

public class CaseReportUtil {

	static public String getDescriptions(String keys, Map<String, String> map) {
		if (keys == null) {
			return "";
		}
		JsonParser parser = new JsonParser();
		JsonElement element = parser.parse(keys);
		JsonArray array = element.getAsJsonArray();
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < array.size(); ++i) {
			String key = array.get(i).getAsString();
			builder.append(map.get(key));
			if (i != array.size() - 1) {
				builder.append(", ");
			}
		}
		return builder.toString();
	}

}

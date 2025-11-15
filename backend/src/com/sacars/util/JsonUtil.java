package com.sacars.util;

import java.util.*;
import java.util.regex.*;

public class JsonUtil {
    public static String toJson(Object obj) {
        if (obj instanceof Map) {
            return mapToJson((Map) obj);
        } else if (obj instanceof List) {
            return listToJson((List) obj);
        }
        return "\"" + obj.toString() + "\"";
    }
    
    private static String mapToJson(Map map) {
        StringBuilder json = new StringBuilder("{");
        boolean first = true;
        for (Object key : map.keySet()) {
            if (!first) json.append(",");
            json.append("\"").append(key).append("\":");
            json.append(toJson(map.get(key)));
            first = false;
        }
        json.append("}");
        return json.toString();
    }
    
    private static String listToJson(List list) {
        StringBuilder json = new StringBuilder("[");
        boolean first = true;
        for (Object item : list) {
            if (!first) json.append(",");
            json.append(toJson(item));
            first = false;
        }
        json.append("]");
        return json.toString();
    }
    
    public static Map<String, String> parseJson(String json) {
        Map<String, String> map = new HashMap<>();
        Pattern pattern = Pattern.compile("\"([^\"]+)\"\\s*:\\s*\"([^\"]+)\"");
        Matcher matcher = pattern.matcher(json);
        
        while (matcher.find()) {
            map.put(matcher.group(1), matcher.group(2));
        }
        return map;
    }
}

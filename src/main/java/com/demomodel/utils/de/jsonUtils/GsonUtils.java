package com.demomodel.utils.de.jsonUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtils {

	 /**
     * 将object对象转成json格式字符串
     */
    public static String toJson(Object object) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        Gson gson = gsonBuilder.create();
        return gson.toJson(object);
    }
}

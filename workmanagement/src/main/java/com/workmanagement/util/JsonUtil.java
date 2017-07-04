package com.workmanagement.util;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * JSON工具类
 *
 * @author Administrator
 */
public class JsonUtil {

    private static GsonBuilder instance;

    private static synchronized GsonBuilder build() {
        if (instance == null) {
            instance = new GsonBuilder();
        }
        return instance;
    }

    public static GsonBuilder getInstance() {
        return build();
    }

    /**
     * 格式化JSON
     *
     * @param obj
     * @return
     */
    public static String toJson(Object obj) {
        return getInstance().disableHtmlEscaping().create().toJson(obj);
    }

    /**
     * 格式化JSON
     *
     * @param obj
     * @return
     */
    public static String toJson2(Object obj) {
        return JSON.toJSONString(obj);
    }

    /**
     * 解析JSON
     *
     * @param json
     * @param typeOfT
     * @return
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return getGson().fromJson(json, typeOfT);
    }

    public static Gson getGson() {
        return getInstance().create();
    }

    public static void main(String[] args) {
        String s = "[1,2,3,4,5]";
        Type oiTypeToken = new TypeToken<List<Integer>>() {
        }.getType();
        List<Integer> l = getInstance().create().fromJson(s, oiTypeToken);
        System.out.println(l);
        if (!l.contains(6))
            l.add(6);

        System.out.println(toJson(l));
    }
}

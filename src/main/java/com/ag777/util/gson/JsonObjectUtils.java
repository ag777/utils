package com.ag777.util.gson;

import com.ag777.util.lang.exception.model.JsonSyntaxException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.math.BigDecimal;

/**
 * com.google.gson.JsonObject操作类
 * @author ag777＜ag777@vip.qq.com＞
 * @version 2024/2/16 15:38
 */
public class JsonObjectUtils {

    /**
     * 从JsonObject中获取Integer类型的值
     *
     * @param jo  JsonObject对象
     * @param key 键
     * @return Integer类型的值，如果不存在则返回null
     * @throws JsonSyntaxException Json语法异常
     */
    public static Integer getInt(JsonObject jo, String key) throws JsonSyntaxException {
        JsonElement element = get(jo, key);
        if (element == null) {
            return null;
        }
        try {
            return element.getAsInt();
        } catch (Exception e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * 从JsonObject中获取Integer类型的值，如果不存在则返回默认值
     *
     * @param jo           JsonObject对象
     * @param key          键
     * @param defaultValue 默认值
     * @return Integer类型的值，如果不存在则返回默认值
     * @throws JsonSyntaxException Json语法异常
     */
    public static int getInt(JsonObject jo, String key, int defaultValue) throws JsonSyntaxException {
        Integer val = getInt(jo, key);
        if (val == null) {
            return defaultValue;
        }
        return val;
    }

    /**
     * 从JsonObject中获取Double类型的值
     *
     * @param jo  JsonObject对象
     * @param key 键
     * @return Double类型的值，如果不存在则返回null
     * @throws JsonSyntaxException Json语法异常
     */
    public static Double getDouble(JsonObject jo, String key) throws JsonSyntaxException {
        JsonElement element = get(jo, key);
        if (element == null) {
            return null;
        }
        try {
            return element.getAsDouble();
        } catch (Exception e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * 从JsonObject中获取Double类型的值，如果不存在则返回默认值
     *
     * @param jo           JsonObject对象
     * @param key          键
     * @param defaultValue 默认值
     * @return Double类型的值，如果不存在则返回默认值
     * @throws JsonSyntaxException Json语法异常
     */
    public static double getDouble(JsonObject jo, String key, double defaultValue) throws JsonSyntaxException {
        Double val = getDouble(jo, key);
        if (val == null) {
            return defaultValue;
        }
        return val;
    }

    /**
     * 从JsonObject中获取Float类型的值
     *
     * @param jo  JsonObject对象
     * @param key 键
     * @return Float类型的值，如果不存在则返回null
     * @throws JsonSyntaxException Json语法异常
     */
    public static Float getFloat(JsonObject jo, String key) throws JsonSyntaxException {
        JsonElement element = get(jo, key);
        if (element == null) {
            return null;
        }
        try {
            return element.getAsFloat();
        } catch (Exception e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * 从JsonObject中获取Float类型的值，如果不存在则返回默认值
     *
     * @param jo           JsonObject对象
     * @param key          键
     * @param defaultValue 默认值
     * @return Float类型的值，如果不存在则返回默认值
     * @throws JsonSyntaxException Json语法异常
     */
    public static float getFloat(JsonObject jo, String key, float defaultValue) throws JsonSyntaxException {
        Float val = getFloat(jo, key);
        if (val == null) {
            return defaultValue;
        }
        return val;
    }

    /**
     * 从JsonObject中获取Long类型的值
     *
     * @param jo  JsonObject对象
     * @param key 键
     * @return Long类型的值，如果不存在则返回null
     * @throws JsonSyntaxException Json语法异常
     */
    public static Long getLong(JsonObject jo, String key) throws JsonSyntaxException {
        JsonElement element = get(jo, key);
        if (element == null) {
            return null;
        }
        try {
            return element.getAsLong();
        } catch (Exception e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * 从JsonObject中获取Long类型的值，如果不存在则返回默认值
     *
     * @param jo           JsonObject对象
     * @param key          键
     * @param defaultValue 默认值
     * @return Long类型的值，如果不存在则返回默认值
     * @throws JsonSyntaxException Json语法异常
     */
    public static long getLong(JsonObject jo, String key, long defaultValue) throws JsonSyntaxException {
        Long val = getLong(jo, key);
        if (val == null) {
            return defaultValue;
        }
        return val;
    }

    /**
     * 从JsonObject中获取BigInteger类型的值
     *
     * @param jo  JsonObject对象
     * @param key 键
     * @return BigInteger类型的值，如果不存在则返回null
     * @throws JsonSyntaxException Json语法异常
     */
    public static java.math.BigInteger getBigInteger(JsonObject jo, String key) throws JsonSyntaxException {
        JsonElement element = get(jo, key);
        if (element == null) {
            return null;
        }
        try {
            return element.getAsBigInteger();
        } catch (Exception e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * 从JsonObject中获取BigInteger类型的值，如果不存在则返回默认值
     *
     * @param jo           JsonObject对象
     * @param key          键
     * @param defaultValue 默认值
     * @return BigInteger类型的值，如果不存在则返回默认值
     * @throws JsonSyntaxException Json语法异常
     */
    public static java.math.BigInteger getBigInteger(JsonObject jo, String key, java.math.BigInteger defaultValue) throws JsonSyntaxException {
        java.math.BigInteger val = getBigInteger(jo, key);
        if (val == null) {
            return defaultValue;
        }
        return val;
    }

    /**
     * 从JsonObject中获取BigDecimal类型的值
     *
     * @param jo  JsonObject对象
     * @param key 键
     * @return BigDecimal类型的值，如果不存在则返回null
     * @throws JsonSyntaxException Json语法异常
     */
    public static BigDecimal getBigDecimal(JsonObject jo, String key) throws JsonSyntaxException {
        JsonElement element = get(jo, key);
        if (element == null) {
            return null;
        }
        try {
            return element.getAsBigDecimal();
        } catch (Exception e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * 从JsonObject中获取BigDecimal类型的值，如果不存在则返回默认值
     *
     * @param jo           JsonObject对象
     * @param key          键
     * @param defaultValue 默认值
     * @return BigDecimal类型的值，如果不存在则返回默认值
     * @throws JsonSyntaxException Json语法异常
     */
    public static BigDecimal getBigDecimal(JsonObject jo, String key, BigDecimal defaultValue) throws JsonSyntaxException {
        BigDecimal val = getBigDecimal(jo, key);
        if (val == null) {
            return defaultValue;
        }
        return val;
    }

    /**
     * 从JsonObject中获取Number类型的值
     *
     * @param jo  JsonObject对象
     * @param key 键
     * @return Number类型的值，如果不存在则返回null
     * @throws JsonSyntaxException Json语法异常
     */
    public static Number getNumber(JsonObject jo, String key) throws JsonSyntaxException {
        JsonElement element = get(jo, key);
        if (element == null) {
            return null;
        }
        try {
            return element.getAsNumber();
        } catch (Exception e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * 从JsonObject中获取Number类型的值，如果不存在则返回默认值
     *
     * @param jo           JsonObject对象
     * @param key          键
     * @param defaultValue 默认值
     * @return Number类型的值，如果不存在则返回默认值
     * @throws JsonSyntaxException Json语法异常
     */
    public static Number getNumber(JsonObject jo, String key, Number defaultValue) throws JsonSyntaxException {
        Number val = getNumber(jo, key);
        if (val == null) {
            return defaultValue;
        }
        return val;
    }

    /**
     * 从JsonObject中获取Byte类型的值
     *
     * @param jo  JsonObject对象
     * @param key 键
     * @return Byte类型的值，如果不存在则返回null
     * @throws JsonSyntaxException Json语法异常
     */
    public static Byte getByte(JsonObject jo, String key) throws JsonSyntaxException {
        JsonElement element = get(jo, key);
        if (element == null) {
            return null;
        }
        try {
            return element.getAsByte();
        } catch (Exception e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * 从JsonObject中获取Byte类型的值，如果不存在则返回默认值
     *
     * @param jo           JsonObject对象
     * @param key          键
     * @param defaultValue 默认值
     * @return Byte类型的值，如果不存在则返回默认值
     * @throws JsonSyntaxException Json语法异常
     */
    public static byte getByte(JsonObject jo, String key, byte defaultValue) throws JsonSyntaxException {
        Byte val = getByte(jo, key);
        if (val == null) {
            return defaultValue;
        }
        return val;
    }

    /**
     * 从JsonObject中获取Boolean类型的值
     *
     * @param jo  JsonObject对象
     * @param key 键
     * @return Boolean类型的值，如果不存在则返回null
     * @throws JsonSyntaxException Json语法异常
     */
    public static Boolean getBool(JsonObject jo, String key) throws JsonSyntaxException {
        JsonElement element = get(jo, key);
        if (element == null) {
            return null;
        }
        try {
            return element.getAsBoolean();
        } catch (Exception e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * 从JsonObject中获取Boolean类型的值，如果不存在则返回默认值
     *
     * @param jo           JsonObject对象
     * @param key          键
     * @param defaultValue 默认值
     * @return Boolean类型的值，如果不存在则返回默认值
     * @throws JsonSyntaxException Json语法异常
     */
    public static boolean getBool(JsonObject jo, String key, boolean defaultValue) throws JsonSyntaxException {
        Boolean val = getBool(jo, key);
        if (val == null) {
            return defaultValue;
        }
        return val;
    }

    /**
     * 从JsonObject中获取String类型的值
     *
     * @param jo  JsonObject对象
     * @param key 键
     * @return String类型的值，如果不存在则返回null
     * @throws JsonSyntaxException Json语法异常
     */
    public static String getStr(JsonObject jo, String key) throws JsonSyntaxException {
        JsonElement element = get(jo, key);
        if (element == null) {
            return null;
        }
        try {
            return element.getAsString();
        } catch (Exception e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * 从JsonObject中获取String类型的值
     *
     * @param jo  JsonObject对象
     * @param key 键
     * @param defaultValue 默认值
     * @return String类型的值，如果不存在则返回默认值
     * @throws JsonSyntaxException Json语法异常
     */
    public static String getStr(JsonObject jo, String key, String defaultValue) throws JsonSyntaxException {
        String val = getStr(jo, key);
        if (val == null) {
            return defaultValue;
        }
        return val;
    }

    /**
     * 从JsonObject中获取另一个JsonObject对象
     *
     * @param jo  JsonObject对象
     * @param key 键
     * @return JsonObject对象，如果不存在则返回null
     * @throws JsonSyntaxException Json语法异常
     */
    public static JsonObject getJsonObject(JsonObject jo, String key) throws JsonSyntaxException {
        JsonElement element = get(jo, key);
        if (element == null) {
            return null;
        }
        try {
            return element.getAsJsonObject();
        } catch (Exception e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * 从JsonObject中获取JsonArray数组
     *
     * @param jo  JsonObject对象
     * @param key 键
     * @return JsonArray数组，如果不存在则返回null
     * @throws JsonSyntaxException Json语法异常
     */
    public static JsonArray getJsonArray(JsonObject jo, String key) throws JsonSyntaxException {
        JsonElement element = get(jo, key);
        if (element == null) {
            return null;
        }
        try {
            return element.getAsJsonArray();
        } catch (Exception e) {
            throw new JsonSyntaxException(e);
        }
    }

    /**
     * 从JsonObject中获取Json字符串
     *
     * @param jo  JsonObject对象
     * @param key 键
     * @return Json字符串，如果不存在则返回null
     */
    public static String getJsonStr(JsonObject jo, String key) {
        JsonElement element = get(jo, key);
        if (element == null) {
            return null;
        }
        return element.toString();
    }

    /**
     * 从JsonObject中获取JsonElement元素
     *
     * @param jo  JsonObject对象
     * @param key 键
     * @return JsonElement元素，如果不存在或值为JsonNull则返回null
     */
    private static JsonElement get(JsonObject jo, String key) {
        JsonElement element = jo.get(key);
        if (element == null || element.isJsonNull()) {
            return null;
        }
        return element;
    }
}

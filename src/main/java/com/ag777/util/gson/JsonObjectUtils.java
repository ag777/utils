package com.ag777.util.gson;

import com.ag777.util.lang.exception.model.JsonSyntaxException;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.math.BigDecimal;

/**
 * com.google.gson.JsonObject操作类
 * @author ag777＜ag777@vip.qq.com＞
 * @version 2024/10/27 16:31
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
     * 从JsonObject中获取第一个非空的整数值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回null
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @return 第一个非空的整数值，如果找不到则返回null
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static Integer getFirstInt(JsonObject jo, String[] keys) throws JsonSyntaxException {
        for (String key : keys) {
            Integer val = getInt(jo, key);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    /**
     * 从JsonObject中获取第一个非空的整数值，如果找不到则返回默认值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回指定的默认值
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @param defaultValue 如果找不到非空值时返回的默认值
     * @return 第一个非空的整数值，如果找不到则返回默认值
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static int getFirstInt(JsonObject jo, String[] keys, int defaultValue) throws JsonSyntaxException {
        Integer val = getFirstInt(jo, keys);
        if (val != null) {
            return val;
        }
        return defaultValue;
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
     * 从JsonObject中获取第一个非空的整数值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回null
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @return 第一个非空的整数值，如果找不到则返回null
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static Double getFirstDouble(JsonObject jo, String[] keys) throws JsonSyntaxException {
        for (String key : keys) {
            Double val = getDouble(jo, key);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    /**
     * 从JsonObject中获取第一个非空的整数值，如果找不到则返回默认值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回指定的默认值
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @param defaultValue 如果找不到非空值时返回的默认值
     * @return 第一个非空的整数值，如果找不到则返回默认值
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static double getFirstDouble(JsonObject jo, String[] keys, double defaultValue) throws JsonSyntaxException {
        Double val = getFirstDouble(jo, keys);
        if (val != null) {
            return val;
        }
        return defaultValue;
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
     * 从JsonObject中获取第一个非空的整数值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回null
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @return 第一个非空的整数值，如果找不到则返回null
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static Float getFirstFloat(JsonObject jo, String[] keys) throws JsonSyntaxException {
        for (String key : keys) {
            Float val = getFloat(jo, key);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    /**
     * 从JsonObject中获取第一个非空的整数值，如果找不到则返回默认值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回指定的默认值
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @param defaultValue 如果找不到非空值时返回的默认值
     * @return 第一个非空的整数值，如果找不到则返回默认值
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static float getFirstFloat(JsonObject jo, String[] keys, float defaultValue) throws JsonSyntaxException {
        Float val = getFirstFloat(jo, keys);
        if (val != null) {
            return val;
        }
        return defaultValue;
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
     * 从JsonObject中获取第一个非空的整数值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回null
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @return 第一个非空的整数值，如果找不到则返回null
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static Long getFirstLong(JsonObject jo, String[] keys) throws JsonSyntaxException {
        for (String key : keys) {
            Long val = getLong(jo, key);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    /**
     * 从JsonObject中获取第一个非空的整数值，如果找不到则返回默认值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回指定的默认值
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @param defaultValue 如果找不到非空值时返回的默认值
     * @return 第一个非空的整数值，如果找不到则返回默认值
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static long getFirstLong(JsonObject jo, String[] keys, long defaultValue) throws JsonSyntaxException {
        Long val = getFirstLong(jo, keys);
        if (val != null) {
            return val;
        }
        return defaultValue;
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
     * 从JsonObject中获取第一个非空的整数值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回null
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @return 第一个非空的整数值，如果找不到则返回null
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static java.math.BigInteger getFirstBigInteger(JsonObject jo, String[] keys) throws JsonSyntaxException {
        for (String key : keys) {
            java.math.BigInteger val = getBigInteger(jo, key);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    /**
     * 从JsonObject中获取第一个非空的整数值，如果找不到则返回默认值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回指定的默认值
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @param defaultValue 如果找不到非空值时返回的默认值
     * @return 第一个非空的整数值，如果找不到则返回默认值
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static java.math.BigInteger getFirstLong(JsonObject jo, String[] keys, java.math.BigInteger defaultValue) throws JsonSyntaxException {
        java.math.BigInteger val = getFirstBigInteger(jo, keys);
        if (val != null) {
            return val;
        }
        return defaultValue;
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
     * 从JsonObject中获取第一个非空的整数值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回null
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @return 第一个非空的整数值，如果找不到则返回null
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static BigDecimal getFirstBigDecimal(JsonObject jo, String[] keys) throws JsonSyntaxException {
        for (String key : keys) {
            BigDecimal val = getBigDecimal(jo, key);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    /**
     * 从JsonObject中获取第一个非空的整数值，如果找不到则返回默认值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回指定的默认值
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @param defaultValue 如果找不到非空值时返回的默认值
     * @return 第一个非空的整数值，如果找不到则返回默认值
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static BigDecimal getFirstBigDecimal(JsonObject jo, String[] keys, BigDecimal defaultValue) throws JsonSyntaxException {
        BigDecimal val = getFirstBigDecimal(jo, keys);
        if (val != null) {
            return val;
        }
        return defaultValue;
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
     * 从JsonObject中获取第一个非空的整数值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回null
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @return 第一个非空的整数值，如果找不到则返回null
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static Number getFirstNumber(JsonObject jo, String[] keys) throws JsonSyntaxException {
        for (String key : keys) {
            Number val = getNumber(jo, key);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    /**
     * 从JsonObject中获取第一个非空的整数值，如果找不到则返回默认值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回指定的默认值
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @param defaultValue 如果找不到非空值时返回的默认值
     * @return 第一个非空的整数值，如果找不到则返回默认值
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static Number getFirstNumber(JsonObject jo, String[] keys, Number defaultValue) throws JsonSyntaxException {
        Number val = getFirstNumber(jo, keys);
        if (val != null) {
            return val;
        }
        return defaultValue;
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
     * 从JsonObject中获取第一个非空的整数值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回null
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @return 第一个非空的整数值，如果找不到则返回null
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static Byte getFirstByte(JsonObject jo, String[] keys) throws JsonSyntaxException {
        for (String key : keys) {
            Byte val = getByte(jo, key);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    /**
     * 从JsonObject中获取第一个非空的整数值，如果找不到则返回默认值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回指定的默认值
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @param defaultValue 如果找不到非空值时返回的默认值
     * @return 第一个非空的整数值，如果找不到则返回默认值
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static byte getByte(JsonObject jo, String[] keys, byte defaultValue) throws JsonSyntaxException {
        Byte val = getFirstByte(jo, keys);
        if (val != null) {
            return val;
        }
        return defaultValue;
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
     * 从JsonObject中获取第一个非空的整数值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回null
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @return 第一个非空的整数值，如果找不到则返回null
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static Boolean getFirstBool(JsonObject jo, String[] keys) throws JsonSyntaxException {
        for (String key : keys) {
            Boolean val = getBool(jo, key);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    /**
     * 从JsonObject中获取第一个非空的整数值，如果找不到则返回默认值
     * 该方法尝试通过一组键来获取JsonObject中的整数值，返回第一个成功获取到的非空值
     * 如果所有指定的键都没有对应的值，则返回指定的默认值
     *
     * @param jo 包含待检索数据的JsonObject
     * @param keys 用于检索的键数组
     * @param defaultValue 如果找不到非空值时返回的默认值
     * @return 第一个非空的整数值，如果找不到则返回默认值
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static boolean getFirstBool(JsonObject jo, String[] keys, boolean defaultValue) throws JsonSyntaxException {
        Boolean val = getFirstBool(jo, keys);
        if (val != null) {
            return val;
        }
        return defaultValue;
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
     * 从JsonObject中获取第一个非空的字符串值
     * 该方法尝试通过提供的键数组从JsonObject中获取第一个非空字符串值
     * 如果所有键对应的值都为空，则返回null
     *
     * @param jo    包含待检索数据的JsonObject
     * @param keys  用于检索的键数组
     * @return      第一个非空的字符串值，如果所有键对应的值都为空，则返回null
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static String getFirstStr(JsonObject jo, String[] keys) throws JsonSyntaxException {
        for (String key : keys) {
            String val = getStr(jo, key);
            if (val != null) {
                return val;
            }
        }
        return null;
    }

    /**
     * 从JsonObject中获取第一个非空的字符串值，如果所有值都为空，则返回默认值
     * 该方法是getFirstStr的重载版本，增加了默认值参数，以便在所有键对应的值都为空时返回
     *
     * @param jo          包含待检索数据的JsonObject
     * @param keys        用于检索的键数组
     * @param defaultValue 如果所有键对应的值都为空时返回的默认值
     * @return            第一个非空的字符串值，如果所有键对应的值都为空，则返回默认值
     * @throws JsonSyntaxException 如果解析Json时发生错误
     */
    public static String getFirstStr(JsonObject jo, String[] keys, String defaultValue) throws JsonSyntaxException {
        String val = getFirstStr(jo, keys);
        if (val != null) {
            return val;
        }
        return defaultValue;
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
     * 获取JsonObject中第一个存在的对象
     * 当给定的键在JsonObject中存在时，返回该键对应的JsonObject
     * 如果没有找到任何给定键对应的对象，则返回null
     * 此方法主要用于简化对象的查找过程，避免手动检查每个键是否存在
     *
     * @param jo   要搜索的JsonObject
     * @param keys 一个字符串数组，包含要查找的键的优先级顺序
     * @return 如果找到，则返回第一个存在的JsonObject；否则返回null
     * @throws JsonSyntaxException 如果解析Json时出现语法错误
     */
    public static JsonObject getFirstJsonObject(JsonObject jo, String[] keys) throws JsonSyntaxException {
        // 遍历键数组，尝试获取对应的JsonObject
        for (String key : keys) {
            JsonObject val = getJsonObject(jo, key);
            // 如果找到非空的JsonObject，立即返回
            if (val != null) {
                return val;
            }
        }
        // 如果所有键都未找到对应的JsonObject，返回null
        return null;
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
     * 获取JsonObject中第一个非空的JsonArray
     *
     * @param jo    包含Json数据的JsonObject
     * @param keys  用于查询JsonArray的键数组
     * @return      返回第一个非空的JsonArray，如果所有键对应的值都为空，则返回null
     * @throws JsonSyntaxException 如果解析Json时发生语法错误
     */
    public static JsonArray getFirstJsonArray(JsonObject jo, String[] keys) throws JsonSyntaxException {
        // 遍历键数组，尝试获取对应的JsonArray
        for (String key : keys) {
            // 尝试根据键获取JsonArray
            JsonArray val = getJsonArray(jo, key);
            // 如果找到非空的JsonJsonArray，立即返回
            if (val != null) {
                return val;
            }
        }
        // 如果所有键都未找到对应的JsonJsonArray，返回null
        return null;
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
     * 从JsonObject中获取第一个非空的字符串值
     * 该方法会尝试从JsonObject中获取每个指定键的值，直到找到第一个非空的值为止
     * 如果所有指定的键对应的值都为空，则返回null
     * 此方法主要用于简化从JsonObject中提取数据的过程，避免手动检查每个键值对是否为空
     *
     * @param jo    包含JSON数据的JsonObject对象
     * @param keys  一个字符串数组，包含待检查的键名列表
     * @return 第一个非空的字符串值，如果没有找到则返回null
     * @throws JsonSyntaxException 如果解析JSON过程中出现语法错误
     */
    public static String getFirstJsonStr(JsonObject jo, String[] keys) throws JsonSyntaxException {
        // 遍历每个键名，尝试获取非空的值
        for (String key : keys) {
            // 调用getJsonStr方法获取当前键名对应的字符串值
            String val = getJsonStr(jo, key);
            // 如果获取到的值非空，则立即返回该值
            if (val != null) {
                return val;
            }
        }
        // 如果所有键名对应的值都为空，则返回null
        return null;
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

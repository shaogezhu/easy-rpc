package com.shaogezhu.easy.rpc.core.common.config;

import com.shaogezhu.easy.rpc.core.common.utils.CommonUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * @Author peng
 * @Date 2023/3/11
 * @description: 配置加载器
 */
public class PropertiesLoader {

    private static Properties properties;

    private static Map<String, String> propertiesMap = new HashMap<>();

    private static final String DEFAULT_PROPERTIES_FILE = "rpc.properties";

    public static void loadConfiguration() throws IOException {
        if (properties != null) {
            return;
        }
        properties = new Properties();
        InputStream in = PropertiesLoader.class.getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE);
        properties.load(in);
    }

    /**
     * 根据键值获取配置属性
     */
    public static String getPropertiesStr(String key) {
        if (properties == null || CommonUtil.isEmpty(key)) {
            return null;
        }
        if (!propertiesMap.containsKey(key)) {
            String value = properties.getProperty(key);
            propertiesMap.put(key, value);
        }
        return propertiesMap.get(key);
    }

    public static String getPropertiesNotBlank(String key) {
        String val = getPropertiesStr(key);
        if (CommonUtil.isEmpty(val)) {
            throw new IllegalArgumentException(key + " 配置为空异常");
        }
        return val;
    }

    public static String getPropertiesStrDefault(String key, String defaultVal) {
        String val = getPropertiesStr(key);
        return CommonUtil.isEmpty(val) ? defaultVal : val;
    }

    /**
     * 根据键值获取配置属性
     */
    public static Integer getPropertiesInteger(String key) {
        if (properties == null || CommonUtil.isEmpty(key)) {
            return null;
        }
        if (!propertiesMap.containsKey(key)) {
            String value = properties.getProperty(key);
            propertiesMap.put(key, value);
        }
        return Integer.valueOf(propertiesMap.get(key));
    }

    /**
     * 根据键值获取配置属性
     */
    public static Integer getPropertiesIntegerDefault(String key, Integer defaultVal) {
        if (properties == null || CommonUtil.isEmpty(key)) {
            return defaultVal;
        }
        String value = properties.getProperty(key);
        if (CommonUtil.isEmpty(value)) {
            propertiesMap.put(key, String.valueOf(defaultVal));
            return defaultVal;
        }
        if (!propertiesMap.containsKey(key)) {
            propertiesMap.put(key, value);
        }
        return Integer.valueOf(propertiesMap.get(key));
    }
}
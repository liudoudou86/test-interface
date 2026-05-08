package com.qa.utils;

import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author LiuZhen
 * @Date 2023/03/22
 * @Description
 */
@Slf4j
public class YamlUtil {
    private static HashMap<Object, Object> properties = new HashMap<>();

    /**
     * 加载配置文件&&初始化
     */
    public static final YamlUtil INSTANCE = new YamlUtil();

    static {
        Yaml yaml = new Yaml();
        try (InputStream in = YamlUtil.class.getClassLoader().getResourceAsStream("config/stg.yml")) {
            properties = yaml.loadAs(in, HashMap.class);
        } catch (Exception e) {
            log.error("Init yaml failed !", e);
        }
    }

    /**
     * 获取键值对
     *
     * @param key 键
     * @return value
     */
    public Object getValueByKey(String key) {
        String separator = ".";
        String[] separatorKeys;
        if (key.contains(separator)) {
            separatorKeys = key.split("\\.");
        } else {
            return properties.get(key);
        }
        Map<String, Map<String, Object>> finalValue = new HashMap<>();
        for (int i = 0; i < separatorKeys.length - 1; i++) {
            if (i == 0) {
                finalValue = (Map) properties.get(separatorKeys[i]);
                continue;
            }
            if (finalValue == null) {
                break;
            }
            finalValue = (Map) finalValue.get(separatorKeys[i]);
        }
        return finalValue == null ? null : finalValue.get(separatorKeys[separatorKeys.length - 1]);
    }
}

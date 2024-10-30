package org.springframework.cloud.contract.verifier.converter;

import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;

class Utils {

    private Utils() {
        throw new AssertionError("Utility class");
    }

    static final Map<String, Object> EMPTY_MAP = Map.of();
    static final List<Map<String, Object>> EMPTY_LIST = List.of();

    static <T> T getOrDefault(Map<String, Object> object, String name, T defaultValue) {
        if (object == null) {
            return defaultValue;
        }
        T t = get(object, name);
        return t == null || isBlank(t) ? defaultValue : t;
    }

    private static <T> boolean isBlank(T t) {
        return t instanceof String value && StringUtils.isBlank(value);
    }

    static <T> T get(Map<String, Object> object, String name) {
        return (T) object.get(name);
    }
}

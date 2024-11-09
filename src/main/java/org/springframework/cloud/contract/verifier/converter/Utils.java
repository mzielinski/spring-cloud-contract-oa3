package org.springframework.cloud.contract.verifier.converter;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Utils {

    private Utils() {
        throw new AssertionError("Utility class");
    }

    static final Map<String, Object> EMPTY_MAP = Map.of();

    public static <T> Stream<T> toStream(Iterator<T> iterator) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, 0),
                false
        );
    }

    public static Stream<JsonNode> findSubNodes(JsonNode parentNode, String nodeName, String subNodeName) {
        return Optional.ofNullable(parentNode.get(nodeName))
                .map(JsonNode::fields)
                .map(fields -> toStream(fields)
                        .filter(operationNode -> operationNode.getKey().equalsIgnoreCase(subNodeName))
                        .flatMap(operationNode -> toStream(operationNode.getValue().iterator())))
                .orElseGet(Stream::empty);
    }

    public static Optional<JsonNode> find(JsonNode node, String nodeName) {
        return Optional.ofNullable(node)
                .filter(contract -> contract.get(nodeName) != null)
                .map(contract -> contract.get(nodeName));
    }

    public static Map<String, JsonNode> toMap(JsonNode contract) {
        return Optional.ofNullable(contract)
                .map(JsonNode::fields)
                .map(fields -> toStream(fields)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)))
                .orElseGet((Map::of));
    }

    public static boolean toBoolean(JsonNode node) {
        return node != null && node.isBoolean() && node.asBoolean();
    }

    public static Integer toInteger(JsonNode node) {
        return node != null && node.isInt() ? node.asInt() : null;
    }

    public static String toText(JsonNode node) {
        return node != null ? node.asText(null) : null;
    }

    public static <T> T getOrDefault(Map<String, Object> object, String name, T defaultValue) {
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

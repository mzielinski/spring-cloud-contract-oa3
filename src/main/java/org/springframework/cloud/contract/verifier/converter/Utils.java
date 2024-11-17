package org.springframework.cloud.contract.verifier.converter;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Spliterators;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Utils {

    private Utils() {
        throw new AssertionError("Utility class");
    }

    public static <T> Stream<T> toStream(Iterator<T> iterator) {
        return StreamSupport.stream(
                Spliterators.spliteratorUnknownSize(iterator, 0),
                false
        );
    }

    @Deprecated
    public static Stream<JsonNode> findSubNodes(JsonNode parentNode, String nodeName, String subNodeName) {
        return Optional.ofNullable(parentNode.get(nodeName))
                .map(JsonNode::fields)
                .map(fields -> toStream(fields)
                        .filter(operationNode -> operationNode.getKey().equalsIgnoreCase(subNodeName))
                        .flatMap(operationNode -> toStream(operationNode.getValue().iterator())))
                .orElseGet(Stream::empty);
    }

    @Deprecated
    public static Optional<JsonNode> find(JsonNode node, String nodeName) {
        return Optional.ofNullable(node)
                .filter(contract -> contract.get(nodeName) != null)
                .map(contract -> contract.get(nodeName));
    }

    @Deprecated
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
}

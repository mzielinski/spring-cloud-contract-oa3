package org.springframework.cloud.contract.verifier.converter.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.Utils.toStream;
import static org.springframework.cloud.contract.verifier.converter.converters.JsonPathConstants.CONTRACT_ID_FILTER;
import static org.springframework.cloud.contract.verifier.converter.converters.JsonPathConstants.PARAM_IN_FILTER;

public class JsonPathTraverser {

    private final Configuration configuration;

    public JsonPathTraverser(Configuration configuration) {
        this.configuration = configuration;
    }

    public Map<String, JsonNode> requestParameterContracts(JsonNode parentNode, String contractId, String fieldName, String parameterName) {
        return jsonNodeIterator(parentNode, "$.parameters[?]", PARAM_IN_FILTER.apply(parameterName))
                .filter(jsonNode -> getContractsForField(jsonNode, contractId, fieldName).findAny().isPresent())
                .collect(Collectors.toMap(
                        parameter -> parameter.get(NAME).asText(),
                        parameter -> getContractsForField(parameter, contractId, fieldName).findAny().orElseThrow()
                ));
    }

    public Map<String, JsonNode> requestBodyContracts(JsonNode parentNode, String contractId, String fieldName) {
        Map<String, JsonNode> result = new LinkedHashMap<>();
        List<JsonNode> items = jsonNodeIterator(parentNode,
                "$.requestBody.x-contracts[?]." + fieldName,
                CONTRACT_ID_FILTER.apply(contractId)).toList();
        result.putAll(items.stream()
                .flatMap(it -> toStream(it.iterator()))
                .filter(it -> it.has(KEY))
                .collect(Collectors.toMap(
                        entry -> entry.get(KEY).asText(),
                        entry -> entry.get(VALUE))));
        result.putAll(items.stream()
                .flatMap(it -> toStream(it.fields()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue)));
        return result;
    }

    public Optional<String> requestBodyContentType(JsonNode parentNode) {
        return Optional.ofNullable(jsonNode(parentNode, "$.requestBody"))
                .map(requestBody -> requestBody.get(CONTENT))
                .flatMap(a -> toStream(a.fieldNames()).findFirst());
    }

    public List<JsonNode> requestBodyContractMatchers(JsonNode parentNode, String contractId, String fieldName) {
        return jsonNodeIterator(parentNode,
                "$.requestBody.x-contracts[?].matchers." + fieldName,
                CONTRACT_ID_FILTER.apply(contractId)).flatMap(it -> toStream(it.iterator()))
                .toList();
    }

    private Stream<JsonNode> getContractsForField(JsonNode parameterNode, String contractId, String fieldName) {
        JsonNode parentNode = parameterNode.get(X_CONTRACTS);
        return parentNode != null
                ? jsonNodeIterator(parentNode, "$.[?]." + fieldName, CONTRACT_ID_FILTER.apply(contractId))
                : Stream.empty();
    }

    private Stream<JsonNode> jsonNodeIterator(JsonNode parentNode, String jsonPath, Predicate... predicates) {
        JsonNode node = jsonNode(parentNode, jsonPath, predicates);
        return toStream(node.iterator());
    }

    private JsonNode jsonNode(JsonNode parentNode, String jsonPath, Predicate... predicates) {
        return JsonPath.using(configuration)
                .parse(parentNode)
                .read(jsonPath, predicates);
    }
}

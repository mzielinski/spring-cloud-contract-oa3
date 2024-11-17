package org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath;

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
import static org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathConstants.CONTRACT_ID_FILTER;
import static org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathConstants.PARAM_IN_FILTER;

public class JsonPathTraverser {

    private final Configuration configuration;

    public JsonPathTraverser(Configuration configuration) {
        this.configuration = configuration;
    }

    public Map<String, JsonNode> requestParameterContracts(JsonNode parentNode, String contractId, String fieldName, String... parameterName) {
        return jsonNodeIterator(parentNode, "$.parameters[?]", PARAM_IN_FILTER.apply(parameterName))
                .filter(jsonNode -> findContractsForField(jsonNode, contractId, fieldName).findAny().isPresent())
                .collect(Collectors.toMap(
                        parameter -> parameter.get(NAME).asText(),
                        parameter -> findContractsForField(parameter, contractId, fieldName).findAny().orElseThrow()
                ));
    }

    public Map<String, JsonNode> requestBodyContracts(JsonNode parentNode, String contractId, String fieldName) {
        List<JsonNode> parameters = requestBodyContractList(parentNode, contractId, fieldName);
        return convertToMap(parameters);
    }

    public List<JsonNode> requestBodyContractList(JsonNode parentNode, String contractId, String fieldName) {
        return jsonNodeIterator(parentNode,
                "$.requestBody.x-contracts[?]." + fieldName,
                CONTRACT_ID_FILTER.apply(contractId)).toList();
    }

    public Map<String, JsonNode> requestContracts(JsonNode parentNode, String contractId, String fieldName) {
        List<JsonNode> parameters = findContractsForField(parentNode, contractId, fieldName).toList();
        return convertToMap(parameters);
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

    public Stream<JsonNode> findContractsForField(JsonNode parameterNode, String contractId, String fieldName) {
        JsonNode parentNode = parameterNode.get(X_CONTRACTS);
        return parentNode != null
                ? jsonNodeIterator(parentNode, "$.[?]." + fieldName, CONTRACT_ID_FILTER.apply(contractId))
                : Stream.empty();
    }

    private static Map<String, JsonNode> convertToMap(List<JsonNode> parameters) {
        Map<String, JsonNode> result = new LinkedHashMap<>();
        result.putAll(parameters.stream()
                .flatMap(it -> toStream(it.iterator()))
                .filter(it -> it.has(KEY))
                .collect(Collectors.toMap(
                        entry -> entry.get(KEY).asText(),
                        entry -> entry.get(VALUE))));
        result.putAll(parameters.stream()
                .flatMap(it -> toStream(it.fields()))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue)));
        return result;
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

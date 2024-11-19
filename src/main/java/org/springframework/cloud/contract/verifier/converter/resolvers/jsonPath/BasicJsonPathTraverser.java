package org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;
import com.jayway.jsonpath.Predicate;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.KEY;
import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.VALUE;
import static org.springframework.cloud.contract.verifier.converter.Utils.toStream;

abstract class BasicJsonPathTraverser implements JsonPathTraverser {

    private final Configuration configuration;

    public BasicJsonPathTraverser(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public Map<String, JsonNode> rootContracts(JsonNode parentNode, String contractId, String fieldName) {
        List<JsonNode> parameters = findContractsForField(parentNode, contractId, fieldName).toList();
        return convertToMap(parameters);
    }

    @Override
    public Map<String, JsonNode> contractsForField(JsonNode parentNode, String contractId, String fieldName) {
        List<JsonNode> parameters = contractList(parentNode, contractId, fieldName);
        return convertToMap(parameters);
    }

    @Override
    public List<JsonNode> contractMatcherList(JsonNode parentNode, String contractId, String fieldName) {
        return contractMatcherStream(parentNode, contractId, fieldName)
                .flatMap(it -> toStream(it.iterator()))
                .toList();
    }

    @Override
    public Map<String, JsonNode> contractMatchers(JsonNode parentNode, String contractId, String fieldName) {
        List<JsonNode> matchers = contractMatcherStream(parentNode, contractId, fieldName).toList();
        return convertToMap(matchers);
    }

    protected Map<String, JsonNode> convertToMap(List<JsonNode> parameters) {
        Map<String, JsonNode> result = new TreeMap<>();
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

    protected Stream<JsonNode> jsonNodeIterator(JsonNode parentNode, String jsonPath, Predicate... predicates) {
        JsonNode node = jsonNode(parentNode, jsonPath, predicates);
        return toStream(node.iterator());
    }

    protected JsonNode jsonNode(JsonNode parentNode, String jsonPath, Predicate... predicates) {
        return JsonPath.using(configuration)
                .parse(parentNode)
                .read(jsonPath, predicates);
    }
}

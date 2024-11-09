package org.springframework.cloud.contract.verifier.converter.converters;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.*;

import java.util.List;
import java.util.Map;
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

    public Map<String, JsonNode> queryParameters(JsonNode parentNode, String contractId, String parameterName) {
        return getJsonNode(parentNode, "$.parameters[?]", PARAM_IN_FILTER.apply(QUERY))
                .filter(parameter -> getContractsForParameter(parameter, contractId, parameterName).findAny().isPresent())
                .collect(Collectors.toMap(
                        parameter -> parameter.get(NAME).asText(),
                        parameter -> getContractsForParameter(parameter, contractId, parameterName).findAny().orElseThrow()
                ));
    }

    public Map<String, JsonNode> requestBodyQueryParameters(JsonNode parentNode, String contractId) {
        return getJsonNode(parentNode,
                "$.requestBody.x-contracts[?].queryParameters",
                CONTRACT_ID_FILTER.apply(contractId)).flatMap(it -> toStream(it.iterator()))
                .collect(Collectors.toMap(
                        entry -> entry.get(KEY).asText(),
                        entry -> entry.get(VALUE)));
    }

    public List<JsonNode> requestBodyQueryParameterMatchers(JsonNode parentNode, String contractId) {
        return getJsonNode(parentNode,
                "$.requestBody.x-contracts[?].matchers.queryParameters",
                CONTRACT_ID_FILTER.apply(contractId)).flatMap(it -> toStream(it.iterator()))
                .toList();
    }

    private Stream<JsonNode> getContractsForParameter(JsonNode parameterNode, String contractId, String parameterName) {
        JsonNode parentNode = parameterNode.get(X_CONTRACTS);
        return parentNode != null
                ? getJsonNode(parentNode, "$.[?]." + parameterName, CONTRACT_ID_FILTER.apply(contractId))
                : Stream.empty();
    }

    private Stream<JsonNode> getJsonNode(JsonNode parentNode, String jsonPath, Predicate... predicates) {
        JsonNode node = JsonPath.using(configuration)
                .parse(parentNode)
                .read(jsonPath, predicates);
        return toStream(node.iterator());
    }
}

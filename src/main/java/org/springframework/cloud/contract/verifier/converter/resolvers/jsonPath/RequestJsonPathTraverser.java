package org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import org.springframework.cloud.contract.verifier.converter.Utils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.X_CONTRACTS;
import static org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathConstants.CONTRACT_ID_FILTER;
import static org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathConstants.PARAM_IN_FILTER;

public class RequestJsonPathTraverser extends BasicJsonPathTraverser {

    public RequestJsonPathTraverser(Configuration configuration) {
        super(configuration);
    }

    @Override
    public Map<String, JsonNode> parameterContracts(JsonNode parentNode, String contractId, String fieldName, String... parameterName) {
        return jsonNodeIterator(parentNode,
                "$.%s[?]".formatted(PARAMETERS),
                PARAM_IN_FILTER.apply(parameterName))
                .filter(jsonNode -> findContractsForField(jsonNode, contractId, fieldName).findAny().isPresent())
                .collect(Collectors.toMap(
                        parameter -> parameter.get(NAME).asText(),
                        parameter -> findContractsForField(parameter, contractId, fieldName).findAny().orElseThrow()
                ));
    }

    @Override
    public List<JsonNode> contractList(JsonNode parentNode, String contractId, String fieldName) {
        return jsonNodeIterator(parentNode,
                "$.%s.%s[?].%s".formatted(REQUEST_BODY, X_CONTRACTS, fieldName),
                CONTRACT_ID_FILTER.apply(contractId)).toList();
    }

    @Override
    public Optional<String> contentType(JsonNode parentNode) {
        return Optional.ofNullable(jsonNode(parentNode, "$.requestBody.content"))
                .map(JsonNode::fieldNames)
                .map(Utils::toStream)
                .flatMap(Stream::findFirst);
    }

    @Override
    public Stream<JsonNode> contractMatcherStream(JsonNode parentNode, String contractId, String fieldName) {
        return jsonNodeIterator(parentNode,
                "$.%s.%s[?].%s.%s".formatted(REQUEST_BODY, X_CONTRACTS, MATCHERS, fieldName),
                CONTRACT_ID_FILTER.apply(contractId));
    }

    @Override
    public Stream<JsonNode> findContractsForField(JsonNode parameterNode, String contractId, String fieldName) {
        JsonNode parentNode = parameterNode.get(X_CONTRACTS);
        return parentNode != null
                ? jsonNodeIterator(parentNode, "$.[?].%s".formatted(fieldName), CONTRACT_ID_FILTER.apply(contractId))
                : Stream.empty();
    }
}

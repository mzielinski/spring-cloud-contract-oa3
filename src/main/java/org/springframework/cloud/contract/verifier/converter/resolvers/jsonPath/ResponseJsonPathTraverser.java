package org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath;

import com.fasterxml.jackson.databind.JsonNode;
import com.jayway.jsonpath.Configuration;
import org.springframework.cloud.contract.verifier.converter.Utils;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.RESPONSES;
import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.X_CONTRACTS;
import static org.springframework.cloud.contract.verifier.converter.Utils.toStream;
import static org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathConstants.CONTRACT_ID_FILTER;

public class ResponseJsonPathTraverser extends BasicJsonPathTraverser {

    public ResponseJsonPathTraverser(Configuration configuration) {
        super(configuration);
    }

    @Override
    public List<JsonNode> contractList(JsonNode parentNode, String contractId, String fieldName) {
        return jsonNodeIterator(parentNode,
                "$.%s.*.%s[?].%s".formatted(RESPONSES, X_CONTRACTS, fieldName),
                CONTRACT_ID_FILTER.apply(contractId)).toList();
    }

    @Override
    public Optional<String> contentType(JsonNode parentNode) {
        return Optional.ofNullable(jsonNode(parentNode, "$.responses.*.content"))
                .map(JsonNode::iterator)
                .map(Utils::toStream)
                .flatMap(stream -> stream.findFirst()
                        .flatMap(content -> toStream(content.fieldNames()).findFirst()));
    }

    @Override
    public Stream<JsonNode> findContractsForField(JsonNode parameterNode, String contractId, String fieldName) {
        JsonNode parentNode = parameterNode.get(X_CONTRACTS);
        return parentNode != null
                ? jsonNodeIterator(parentNode, "$.[?].%s".formatted(fieldName), CONTRACT_ID_FILTER.apply(contractId))
                : Stream.empty();
    }

    @Override
    public Stream<JsonNode> contractMatcherStream(JsonNode parentNode, String contractId, String fieldName) {
        return Stream.empty();
    }

    @Override
    public Map<String, JsonNode> parameterContracts(JsonNode parentNode, String contractId, String fieldName, String... parameterName) {
        return Map.of();
    }
}

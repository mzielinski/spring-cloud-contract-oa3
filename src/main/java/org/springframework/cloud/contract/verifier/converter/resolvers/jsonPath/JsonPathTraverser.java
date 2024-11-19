package org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public interface JsonPathTraverser {

    Optional<String> contentType(JsonNode parentNode);

    Map<String, JsonNode> parameterContracts(JsonNode parentNode, String contractId, String fieldName, String... parameterName);

    Map<String, JsonNode> rootContracts(JsonNode parentNode, String contractId, String fieldName);

    Map<String, JsonNode> contractsForField(JsonNode parentNode, String contractId, String fieldName);

    List<JsonNode> contractList(JsonNode parentNode, String contractId, String fieldName);

    Stream<JsonNode> findContractsForField(JsonNode parameterNode, String contractId, String fieldName);

    Stream<JsonNode> contractMatcherStream(JsonNode parentNode, String contractId, String fieldName);

    List<JsonNode> contractMatcherList(JsonNode parentNode, String contractId, String fieldName);

    Map<String, JsonNode> contractMatchers(JsonNode parentNode, String contractId, String fieldName);
}

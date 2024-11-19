package org.springframework.cloud.contract.verifier.converter.resolvers.response.headers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.AbstractResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.ResponseJsonPathTraverser;

import java.util.Map;
import java.util.TreeMap;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.CONTENT_TYPE_HTTP_HEADER;
import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.HEADERS;
import static org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathConstants.JSON_PATH_CONFIGURATION;

public class ResponseHeaderResolver extends AbstractResolver<Map<String, Object>> {

    public ResponseHeaderResolver(Oa3Spec spec, String contractId) {
        super(spec, contractId, new ResponseJsonPathTraverser(JSON_PATH_CONFIGURATION));
    }

    @Override
    public Map<String, Object> resolve() {
        Map<String, JsonNode> contractHeaders = traverser().contractsForField(operationNode(), contractId(), HEADERS);
        Map<String, Object> headers = new TreeMap<>(contractHeaders);
        traverser().contentType(operationNode())
                .ifPresent(content -> headers.putIfAbsent(CONTENT_TYPE_HTTP_HEADER, content));
        return headers;
    }
}

package org.springframework.cloud.contract.verifier.converter.resolvers.request.body;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.AbstractResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.RequestJsonPathTraverser;

import java.util.Map;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.BODY;
import static org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathConstants.JSON_PATH_CONFIGURATION;

public class RequestBodyResolver extends AbstractResolver<Map<String, JsonNode>> {

    public RequestBodyResolver(Oa3Spec spec, String contractId) {
        super(spec, contractId, new RequestJsonPathTraverser(JSON_PATH_CONFIGURATION));
    }

    @Override
    public Map<String, JsonNode> resolve() {
        Map<String, JsonNode> body = traverser().rootContracts(operationNode(), contractId(), BODY);
        return !body.isEmpty() ? body : null;
    }
}

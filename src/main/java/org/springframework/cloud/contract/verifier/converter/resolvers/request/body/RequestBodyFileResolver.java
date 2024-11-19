package org.springframework.cloud.contract.verifier.converter.resolvers.request.body;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.AbstractResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.RequestJsonPathTraverser;

import static org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathConstants.JSON_PATH_CONFIGURATION;

public class RequestBodyFileResolver extends AbstractResolver<String> {

    private final String fieldName;

    public RequestBodyFileResolver(Oa3Spec spec, String contractId, String fieldName) {
        super(spec, contractId, new RequestJsonPathTraverser(JSON_PATH_CONFIGURATION));
        this.fieldName = fieldName;
    }

    @Override
    public String resolve() {
        return traverser().contractList(operationNode(), contractId(), fieldName).stream()
                .findFirst()
                .map(JsonNode::asText)
                .orElse(null);
    }
}

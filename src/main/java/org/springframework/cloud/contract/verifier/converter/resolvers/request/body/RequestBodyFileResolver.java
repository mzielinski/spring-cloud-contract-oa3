package org.springframework.cloud.contract.verifier.converter.resolvers.request.body;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.AbstractResolver;

public class RequestBodyFileResolver extends AbstractResolver<String> {

    private final String fieldName;

    public RequestBodyFileResolver(Oa3Spec spec, String contractId, String fieldName) {
        super(spec, contractId);
        this.fieldName = fieldName;
    }

    @Override
    public String resolve() {
        return traverser().requestBodyContractList(operationNode(), contractId(), fieldName).stream()
                .findFirst()
                .map(JsonNode::asText)
                .orElse(null);
    }
}

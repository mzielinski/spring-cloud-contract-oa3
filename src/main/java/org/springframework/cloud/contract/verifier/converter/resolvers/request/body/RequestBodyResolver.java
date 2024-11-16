package org.springframework.cloud.contract.verifier.converter.resolvers.request.body;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.AbstractResolver;

import java.util.Map;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.BODY;

public class RequestBodyResolver extends AbstractResolver<Map<String, JsonNode>> {

    public RequestBodyResolver(Oa3Spec spec, String contractId) {
        super(spec, contractId);
    }

    @Override
    public Map<String, JsonNode> resolve() {
        Map<String, JsonNode> body = traverser().requestBodyContracts(operationNode(), contractId(), BODY);
        return !body.isEmpty() ? body : null;
    }
}

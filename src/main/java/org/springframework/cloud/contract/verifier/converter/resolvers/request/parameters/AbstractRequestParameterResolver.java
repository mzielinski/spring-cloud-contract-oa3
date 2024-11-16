package org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.AbstractResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.RequestElement;

import java.util.Map;
import java.util.TreeMap;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.VALUE;

abstract class AbstractRequestParameterResolver extends AbstractResolver<Map<String, Object>> {

    private final RequestElement type;

    AbstractRequestParameterResolver(Oa3Spec spec, String contractId, RequestElement type) {
        super(spec, contractId);
        this.type = type;
    }

    @Override
    public Map<String, Object> resolve() {
        Map<String, Object> parameters = new TreeMap<>();
        parameters.putAll(traverser().requestParameterContracts(operationNode(), contractId(), VALUE, type.paramField()));
        parameters.putAll(traverser().requestBodyContracts(operationNode(), contractId(), type.requestField()));
        parameters.putAll(traverser().requestContracts(operationNode(), contractId(), type.requestField()));
        return parameters;
    }
}

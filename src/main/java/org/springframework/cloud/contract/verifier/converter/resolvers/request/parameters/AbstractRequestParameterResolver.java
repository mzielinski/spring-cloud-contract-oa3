package org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.AbstractResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.RequestJsonPathTraverser;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.RequestElement;

import java.util.Map;
import java.util.TreeMap;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.VALUE;
import static org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathConstants.JSON_PATH_CONFIGURATION;

public abstract class AbstractRequestParameterResolver extends AbstractResolver<Map<String, Object>> {

    private final RequestElement type;

    public AbstractRequestParameterResolver(Oa3Spec spec, String contractId, RequestElement type) {
        super(spec, contractId, new RequestJsonPathTraverser(JSON_PATH_CONFIGURATION));
        this.type = type;
    }

    @Override
    public Map<String, Object> resolve() {
        Map<String, Object> parameters = new TreeMap<>();
        parameters.putAll(traverser().parameterContracts(operationNode(), contractId(), VALUE, type.paramField()));
        parameters.putAll(traverser().rootContracts(operationNode(), contractId(), type.requestField()));
        parameters.putAll(traverser().contractsForField(operationNode(), contractId(), type.requestField()));
        return parameters;
    }
}

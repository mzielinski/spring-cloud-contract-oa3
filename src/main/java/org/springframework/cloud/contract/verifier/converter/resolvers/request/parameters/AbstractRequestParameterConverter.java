package org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.RequestElement;
import org.springframework.cloud.contract.verifier.converter.resolvers.JsonPathTraverser;

import java.util.Map;
import java.util.TreeMap;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.VALUE;
import static org.springframework.cloud.contract.verifier.converter.resolvers.JsonPathConstants.JSON_PATH_CONFIGURATION;

abstract class AbstractRequestParameterConverter {

    private final JsonPathTraverser traverser = new JsonPathTraverser(JSON_PATH_CONFIGURATION);
    private final Oa3Spec spec;
    private final String contractId;
    private final RequestElement type;

    AbstractRequestParameterConverter(Oa3Spec spec, String contractId, RequestElement type) {
        this.spec = spec;
        this.contractId = contractId;
        this.type = type;
    }

    public Map<String, Object> convert() {
        Map<String, Object> parameters = new TreeMap<>();
        parameters.putAll(traverser.requestParameterContracts(spec.operationNode(), contractId, VALUE, type.paramField()));
        parameters.putAll(traverser.requestBodyContracts(spec.operationNode(), contractId, type.requestField()));
        parameters.putAll(traverser.requestContracts(spec.operationNode(), contractId, type.requestField()));
        return parameters;
    }

    protected Oa3Spec getSpec() {
        return spec;
    }

    protected JsonPathTraverser getTraverser() {
        return traverser;
    }
}

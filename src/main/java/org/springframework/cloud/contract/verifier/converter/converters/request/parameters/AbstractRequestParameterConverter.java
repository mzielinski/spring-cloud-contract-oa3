package org.springframework.cloud.contract.verifier.converter.converters.request.parameters;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.converters.request.RequestElement;
import org.springframework.cloud.contract.verifier.converter.converters.JsonPathTraverser;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.VALUE;
import static org.springframework.cloud.contract.verifier.converter.converters.JsonPathConstants.JSON_PATH_CONFIGURATION;

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
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.putAll(traverser.requestParameterContracts(spec.operationNode(), contractId, VALUE, type.getParameter()));
        parameters.putAll(traverser.requestBodyContracts(spec.operationNode(), contractId, type.getRequestBody()));
        return parameters;
    }

    protected Oa3Spec getSpec() {
        return spec;
    }

    protected JsonPathTraverser getTraverser() {
        return traverser;
    }
}

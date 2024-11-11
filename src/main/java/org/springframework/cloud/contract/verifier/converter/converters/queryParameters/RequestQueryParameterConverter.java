package org.springframework.cloud.contract.verifier.converter.converters.queryParameters;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.converters.JsonPathTraverser;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.VALUE;
import static org.springframework.cloud.contract.verifier.converter.converters.JsonPathConstants.JSON_PATH_CONFIGURATION;

public class RequestQueryParameterConverter {

    private final JsonPathTraverser traverser = new JsonPathTraverser(JSON_PATH_CONFIGURATION);
    private final Oa3Spec spec;
    private final String contractId;

    public RequestQueryParameterConverter(Oa3Spec spec, String contractId) {
        this.spec = spec;
        this.contractId = contractId;
    }

    public Map<String, Object> convert() {
        Map<String, Object> queryParameters = new LinkedHashMap<>();
        queryParameters.putAll(traverser.requestBodyQueryParameters(spec.operationNode(), contractId));
        queryParameters.putAll(traverser.parameterQueries(spec.operationNode(), contractId, VALUE));
        return queryParameters;
    }
}

package org.springframework.cloud.contract.verifier.converter.converters.headers;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.converters.JsonPathTraverser;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.CONTENT_TYPE_HTTP_HEADER;
import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.VALUE;
import static org.springframework.cloud.contract.verifier.converter.converters.JsonPathConstants.JSON_PATH_CONFIGURATION;

public class RequestHeaderConverter {

    private final JsonPathTraverser traverser = new JsonPathTraverser(JSON_PATH_CONFIGURATION);
    private final Oa3Spec spec;
    private final String contractId;

    public RequestHeaderConverter(Oa3Spec spec, String contractId) {
        this.spec = spec;
        this.contractId = contractId;
    }

    public Map<String, Object> convert() {
        Map<String, Object> headers = new LinkedHashMap<>();
        traverser.requestBodyContentType(spec.operationNode())
                .ifPresent(contentType -> headers.put(CONTENT_TYPE_HTTP_HEADER, contentType));
        headers.putAll(traverser.parameterHeaders(spec.operationNode(), contractId, VALUE));
        headers.putAll(traverser.requestBodyHeaders(spec.operationNode(), contractId));
        return headers;
    }
}

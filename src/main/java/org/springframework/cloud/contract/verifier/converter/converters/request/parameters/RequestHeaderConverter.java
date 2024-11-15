package org.springframework.cloud.contract.verifier.converter.converters.request.parameters;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.converters.request.RequestElement;

import java.util.Map;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.CONTENT_TYPE_HTTP_HEADER;

public class RequestHeaderConverter extends AbstractRequestParameterConverter {

    public RequestHeaderConverter(Oa3Spec spec, String contractId) {
        super(spec, contractId, RequestElement.HEADER);
    }

    @Override
    public Map<String, Object> convert() {
        Map<String, Object> headers = super.convert();
        headers.putIfAbsent(CONTENT_TYPE_HTTP_HEADER, getTraverser()
                .requestBodyContentType(getSpec().operationNode())
                .orElse(null));
        return headers;
    }
}

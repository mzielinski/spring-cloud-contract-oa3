package org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.RequestElement;

import java.util.Map;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.CONTENT_TYPE_HTTP_HEADER;

public class RequestHeaderResolver extends AbstractRequestParameterResolver {

    public RequestHeaderResolver(Oa3Spec spec, String contractId) {
        super(spec, contractId, RequestElement.HEADER);
    }

    @Override
    public Map<String, Object> resolve() {
        Map<String, Object> headers = super.resolve();
        getTraverser().requestBodyContentType(getSpec().operationNode())
                .ifPresent(contentType -> {
                    headers.putIfAbsent(CONTENT_TYPE_HTTP_HEADER, contentType);
                });
        return headers;
    }
}

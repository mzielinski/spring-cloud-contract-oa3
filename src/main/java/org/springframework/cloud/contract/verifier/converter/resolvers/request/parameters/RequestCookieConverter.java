package org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.RequestElement;

public class RequestCookieConverter extends AbstractRequestParameterConverter {

    public RequestCookieConverter(Oa3Spec spec, String contractId) {
        super(spec, contractId, RequestElement.COOKIE);
    }
}

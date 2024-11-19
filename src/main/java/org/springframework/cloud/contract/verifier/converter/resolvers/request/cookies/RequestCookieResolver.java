package org.springframework.cloud.contract.verifier.converter.resolvers.request.cookies;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.RequestElement;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters.AbstractRequestParameterResolver;

public class RequestCookieResolver extends AbstractRequestParameterResolver {

    public RequestCookieResolver(Oa3Spec spec, String contractId) {
        super(spec, contractId, RequestElement.COOKIE);
    }
}

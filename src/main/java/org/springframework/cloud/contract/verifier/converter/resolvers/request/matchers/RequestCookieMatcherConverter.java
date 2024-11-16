package org.springframework.cloud.contract.verifier.converter.resolvers.request.matchers;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.RequestElement;

public class RequestCookieMatcherConverter extends AbstractRequestKeyValueMatcherConverter {

    public RequestCookieMatcherConverter(Oa3Spec spec, String contractId) {
        super(spec, contractId, RequestElement.COOKIE);
    }
}

package org.springframework.cloud.contract.verifier.converter.converters.request.matchers;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.converters.request.RequestElement;

public class RequestHeaderMatcherConverter extends AbstractRequestKeyValueMatcherConverter {

    public RequestHeaderMatcherConverter(Oa3Spec spec, String contractId) {
        super(spec, contractId, RequestElement.HEADER);
    }
}

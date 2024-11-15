package org.springframework.cloud.contract.verifier.converter.converters.request.parameters;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.converters.request.RequestElement;

public class RequestQueryParameterConverter extends AbstractRequestParameterConverter {

    public RequestQueryParameterConverter(Oa3Spec spec, String contractId) {
        super(spec, contractId, RequestElement.QUERY_PARAMETERS);
    }
}

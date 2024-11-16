package org.springframework.cloud.contract.verifier.converter.converters.request;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;

public enum RequestElement {

    COOKIE(Oa3Spec.COOKIES, Oa3Spec.COOKIE),
    HEADER(Oa3Spec.HEADERS, Oa3Spec.HEADER),
    QUERY_PARAMETERS(Oa3Spec.QUERY_PARAMETERS, Oa3Spec.QUERY, Oa3Spec.PATH);

    private final String requestBody;
    private final String[] parameter;

    RequestElement(String requestBody, String... parameter) {
        this.requestBody = requestBody;
        this.parameter = parameter;
    }

    public String requestField() {
        return requestBody;
    }

    public String[] paramField() {
        return parameter;
    }
}

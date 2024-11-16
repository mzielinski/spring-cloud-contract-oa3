package org.springframework.cloud.contract.verifier.converter.resolvers.request;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;

public class UrlPathResolver implements Resolver<String> {

    private final Oa3Spec spec;
    private final String contractId;

    public UrlPathResolver(Oa3Spec spec, String contractId) {
        this.spec = spec;
        this.contractId = contractId;
    }

    @Override
    public String resolve() {
        return null;
    }
}

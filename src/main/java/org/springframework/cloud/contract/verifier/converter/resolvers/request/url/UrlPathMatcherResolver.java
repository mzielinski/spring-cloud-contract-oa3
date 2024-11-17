package org.springframework.cloud.contract.verifier.converter.resolvers.request.url;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.AbstractResolver;

public class UrlPathMatcherResolver extends AbstractResolver<YamlContract.KeyValueMatcher> {

    public UrlPathMatcherResolver(Oa3Spec spec, String contractId) {
        super(spec, contractId);
    }

    @Override
    public YamlContract.KeyValueMatcher resolve() {
//        buildKeyValueMatcher(getOrDefault(matchers, URL, EMPTY_MAP));
        return null;
    }
}

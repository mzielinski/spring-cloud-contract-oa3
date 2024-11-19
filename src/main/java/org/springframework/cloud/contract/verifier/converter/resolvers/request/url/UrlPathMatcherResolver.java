package org.springframework.cloud.contract.verifier.converter.resolvers.request.url;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract.KeyValueMatcher;
import org.springframework.cloud.contract.verifier.converter.resolvers.builders.SccModelBuilder;
import org.springframework.cloud.contract.verifier.converter.resolvers.AbstractResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.RequestJsonPathTraverser;

import static org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathConstants.JSON_PATH_CONFIGURATION;

public class UrlPathMatcherResolver extends AbstractResolver<KeyValueMatcher> {

    public UrlPathMatcherResolver(Oa3Spec spec, String contractId) {
        super(spec, contractId, new RequestJsonPathTraverser(JSON_PATH_CONFIGURATION));
    }

    @Override
    public KeyValueMatcher resolve() {
        return traverser().contractMatcherStream(operationNode(), contractId(), Oa3Spec.URL)
                .map(SccModelBuilder::toKeyValueMatcher)
                .findFirst()
                .orElse(null);
    }
}

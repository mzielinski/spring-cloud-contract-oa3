package org.springframework.cloud.contract.verifier.converter.resolvers.request.body;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract.BodyStubMatcher;
import org.springframework.cloud.contract.verifier.converter.resolvers.builders.SccModelBuilder;
import org.springframework.cloud.contract.verifier.converter.resolvers.AbstractResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.RequestJsonPathTraverser;

import java.util.List;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.BODY;
import static org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathConstants.JSON_PATH_CONFIGURATION;

public class RequestBodyMatcherResolver extends AbstractResolver<List<BodyStubMatcher>> {

    public RequestBodyMatcherResolver(Oa3Spec spec, String contractId) {
        super(spec, contractId, new RequestJsonPathTraverser(JSON_PATH_CONFIGURATION));
    }

    @Override
    public List<BodyStubMatcher> resolve() {
        return traverser()
                .contractMatcherList(operationNode(), contractId(), BODY).stream()
                .map(SccModelBuilder::toBodyStubMatcher)
                .toList();
    }
}

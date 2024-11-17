package org.springframework.cloud.contract.verifier.converter.resolvers.request.body;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract.BodyStubMatcher;
import org.springframework.cloud.contract.verifier.converter.resolvers.builders.SccModelBuilder;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.AbstractResolver;

import java.util.List;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.BODY;

public class RequestBodyMatcherResolver extends AbstractResolver<List<BodyStubMatcher>> {

    public RequestBodyMatcherResolver(Oa3Spec spec, String contractId) {
        super(spec, contractId);
    }

    @Override
    public List<BodyStubMatcher> resolve() {
        return traverser()
                .requestBodyContractMatcherList(operationNode(), contractId(), BODY).stream()
                .map(SccModelBuilder::toBodyStubMatcher)
                .toList();
    }
}

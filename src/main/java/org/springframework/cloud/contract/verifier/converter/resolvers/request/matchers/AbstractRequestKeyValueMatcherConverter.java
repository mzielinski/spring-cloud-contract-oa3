package org.springframework.cloud.contract.verifier.converter.resolvers.request.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.converter.resolvers.builders.SccModelBuilder;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.RequestElement;

abstract class AbstractRequestKeyValueMatcherConverter extends AbstractRequestMatcherConverter<YamlContract.KeyValueMatcher> {

    AbstractRequestKeyValueMatcherConverter(Oa3Spec spec, String contractId, RequestElement type) {
        super(spec, contractId, type);
    }

    @Override
    protected YamlContract.KeyValueMatcher mapToMatchers(String name, JsonNode matcher) {
        return SccModelBuilder.toKeyValueMatcher(name, matcher);
    }
}

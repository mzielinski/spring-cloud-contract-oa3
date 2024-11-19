package org.springframework.cloud.contract.verifier.converter.resolvers.request;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.converter.resolvers.builders.SccModelBuilder;

public abstract class AbstractRequestKeyValueMatcherConverter extends AbstractRequestMatcherConverter<YamlContract.KeyValueMatcher> {

    public AbstractRequestKeyValueMatcherConverter(Oa3Spec spec, String contractId, RequestElement type) {
        super(spec, contractId, type);
    }

    @Override
    protected YamlContract.KeyValueMatcher mapToMatchers(String name, JsonNode matcher) {
        return SccModelBuilder.toKeyValueMatcher(name, matcher);
    }
}

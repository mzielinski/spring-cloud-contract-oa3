package org.springframework.cloud.contract.verifier.converter.resolvers.request.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract.QueryParameterMatcher;
import org.springframework.cloud.contract.verifier.converter.resolvers.builders.SccModelBuilder;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.RequestElement;

public class RequestQueryParameterMatcherConverter extends AbstractRequestMatcherConverter<QueryParameterMatcher> {

    public RequestQueryParameterMatcherConverter(Oa3Spec spec, String contractId) {
        super(spec, contractId, RequestElement.QUERY_PARAMETERS);
    }

    @Override
    protected QueryParameterMatcher mapToMatchers(String parameterName, JsonNode matcher) {
        return SccModelBuilder.toQueryParameterMatcher(parameterName, matcher);
    }
}

package org.springframework.cloud.contract.verifier.converter.resolvers.request.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract.QueryParameterMatcher;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.RequestElement;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.TYPE;
import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.VALUE;
import static org.springframework.cloud.contract.verifier.converter.SccUtils.createMatchingType;

public class RequestQueryParameterMatcherConverter extends AbstractRequestMatcherConverter<QueryParameterMatcher> {

    public RequestQueryParameterMatcherConverter(Oa3Spec spec, String contractId) {
        super(spec, contractId, RequestElement.QUERY_PARAMETERS);
    }

    @Override
    protected QueryParameterMatcher mapToMatchers(String parameterName, JsonNode matcher) {
        QueryParameterMatcher queryParameterMatcher = new QueryParameterMatcher();
        queryParameterMatcher.key = parameterName;
        queryParameterMatcher.value = matcher.get(VALUE);
        queryParameterMatcher.type = createMatchingType(matcher.get(TYPE).asText());
        return queryParameterMatcher;
    }
}

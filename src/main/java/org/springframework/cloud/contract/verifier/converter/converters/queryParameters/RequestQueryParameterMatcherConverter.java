package org.springframework.cloud.contract.verifier.converter.converters.queryParameters;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.converter.converters.JsonPathTraverser;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.SccUtils.createMatchingType;
import static org.springframework.cloud.contract.verifier.converter.Utils.toStream;
import static org.springframework.cloud.contract.verifier.converter.converters.JsonPathConstants.JSON_PATH_CONFIGURATION;

public class RequestQueryParameterMatcherConverter {

    private final JsonPathTraverser traverser = new JsonPathTraverser(JSON_PATH_CONFIGURATION);
    private final Oa3Spec spec;
    private final String contractId;

    public RequestQueryParameterMatcherConverter(Oa3Spec spec, String contractId) {
        this.spec = spec;
        this.contractId = contractId;
    }

    public List<YamlContract.QueryParameterMatcher> convert() {
        List<YamlContract.QueryParameterMatcher> queryParameterMatchers = new ArrayList<>();
        traverser.requestBodyQueryParameterMatchers(spec.operationNode(), contractId)
                .forEach(matcher -> queryParameterMatchers
                        .addAll(toQueryParameterMatcher(matcher, matcher.get(KEY).asText())));
        traverser.queryParameters(spec.operationNode(), contractId, MATCHERS)
                .forEach((parameterName, matcher) -> queryParameterMatchers
                        .addAll(toQueryParameterMatcher(matcher, parameterName)));
        return queryParameterMatchers;
    }

    private static List<YamlContract.QueryParameterMatcher> toQueryParameterMatcher(JsonNode matcher, String parameterName) {
        return matcher.isArray()
                ? toStream(matcher.iterator()).map(subMatcher -> mapQueryParameterMatcher(parameterName, subMatcher)).toList()
                : List.of(mapQueryParameterMatcher(parameterName, matcher));
    }

    private static YamlContract.QueryParameterMatcher mapQueryParameterMatcher(String parameterName, JsonNode matcher) {
        YamlContract.QueryParameterMatcher queryParameterMatcher = new YamlContract.QueryParameterMatcher();
        queryParameterMatcher.key = parameterName;
        queryParameterMatcher.value = matcher.get(VALUE);
        queryParameterMatcher.type = createMatchingType(matcher.get(TYPE).asText());
        return queryParameterMatcher;
    }
}

package org.springframework.cloud.contract.verifier.converter.converters;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.SccUtils.createMatchingType;
import static org.springframework.cloud.contract.verifier.converter.Utils.*;
import static org.springframework.cloud.contract.verifier.converter.Utils.toStream;

public record RequestQueryParameterConverter(Oa3Spec spec, String contractId) {

    public void convert(YamlContract.Request yamlRequest) {
        requestBodyQueryParameters(yamlRequest);
        operationQueryParameters(yamlRequest);
        requestBodyQueryParameterMatchers(yamlRequest);
    }

    private void requestBodyQueryParameterMatchers(YamlContract.Request yamlRequest) {
        findContract(spec.operationNode().get(REQUEST_BODY), contractId)
                .map(node -> node.get(MATCHERS))
                .ifPresent(node -> toStream(node.fields())
                        .filter(parameter -> QUERY_PARAMETERS.equalsIgnoreCase(parameter.getKey()))
                        .map(Map.Entry::getValue)
                        .forEach(parameter -> toStream(parameter.iterator()).forEach(matcher -> {
                            var queryParameterMatcher = toQueryParameterMatcher(matcher, matcher.get(KEY).asText());
                            yamlRequest.matchers.queryParameters.add(queryParameterMatcher);
                        })));
    }

    private void operationQueryParameters(YamlContract.Request yamlRequest) {
        getOrElse(spec.operationNode(), PARAMETERS)
                .ifPresent(parameters -> toStream(parameters.iterator())
                        .filter(parameter -> QUERY.equals(toText(parameter.get(IN))))
                        .forEach(parameter -> findContract(parameter, contractId)
                                .ifPresent(contract -> {
                                    String parameterName = toText(parameter.get(NAME));
                                    yamlRequest.queryParameters.put(parameterName, contract.get(VALUE));
                                    getOrElse(contract, MATCHERS).ifPresent(matchers ->
                                            toStream(matchers.iterator()).forEach(matcher -> {
                                                var queryParameterMatcher = toQueryParameterMatcher(matcher, parameterName);
                                                yamlRequest.matchers.queryParameters.add(queryParameterMatcher);
                                            }));
                                })
                        ));
    }

    private void requestBodyQueryParameters(YamlContract.Request yamlRequest) {
        findContract(spec.operationNode().get(REQUEST_BODY), contractId)
                .map(node -> node.get(QUERY_PARAMETERS))
                .map(node -> toStream(node.iterator())
                        .collect(Collectors.toMap(
                                entry -> entry.get(KEY).asText(),
                                entry -> entry.get(VALUE))))
                .ifPresent(queryParams -> yamlRequest.queryParameters.putAll(queryParams));
    }


    private static YamlContract.QueryParameterMatcher toQueryParameterMatcher(JsonNode matcher, String parameterName) {
        YamlContract.QueryParameterMatcher queryParameterMatcher = new YamlContract.QueryParameterMatcher();
        queryParameterMatcher.key = parameterName;
        queryParameterMatcher.value = matcher.get(VALUE);
        queryParameterMatcher.type = createMatchingType(matcher.get(TYPE).asText());
        return queryParameterMatcher;
    }
}

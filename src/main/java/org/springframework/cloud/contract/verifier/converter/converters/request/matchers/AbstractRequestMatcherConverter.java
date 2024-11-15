package org.springframework.cloud.contract.verifier.converter.converters.request.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.converters.request.RequestElement;
import org.springframework.cloud.contract.verifier.converter.converters.JsonPathTraverser;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.KEY;
import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.MATCHERS;
import static org.springframework.cloud.contract.verifier.converter.Utils.toStream;
import static org.springframework.cloud.contract.verifier.converter.converters.JsonPathConstants.JSON_PATH_CONFIGURATION;

abstract class AbstractRequestMatcherConverter<T> {

    private final JsonPathTraverser traverser = new JsonPathTraverser(JSON_PATH_CONFIGURATION);
    private final Oa3Spec spec;
    private final String contractId;
    private final RequestElement type;

    AbstractRequestMatcherConverter(Oa3Spec spec, String contractId, RequestElement type) {
        this.spec = spec;
        this.contractId = contractId;
        this.type = type;
    }

    public List<T> convert() {
        List<T> matchers = new ArrayList<>();
        traverser.requestParameterContracts(spec.operationNode(), contractId, MATCHERS, type.getParameter())
                .forEach((parameterName, matcher) -> matchers.addAll(toMatcher(matcher, parameterName)));
        traverser.requestBodyContractMatchers(spec.operationNode(), contractId, type.getRequestBody())
                .forEach(matcher -> matchers.addAll(toMatcher(matcher, matcher.get(KEY).asText())));
        return matchers;
    }

    abstract protected T mapToMatchers(String headerName, JsonNode matcher);

    private List<T> toMatcher(JsonNode matcher, String headerName) {
        return matcher.isArray()
                ? toStream(matcher.iterator()).map(subMatcher -> mapToMatchers(headerName, subMatcher)).toList()
                : List.of(mapToMatchers(headerName, matcher));
    }
}

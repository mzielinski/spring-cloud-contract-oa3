package org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.AbstractResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.RequestElement;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.KEY;
import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.MATCHERS;
import static org.springframework.cloud.contract.verifier.converter.Utils.toStream;

abstract class AbstractRequestMatcherConverter<T> extends AbstractResolver<List<T>> {

    private final RequestElement type;

    AbstractRequestMatcherConverter(Oa3Spec spec, String contractId, RequestElement type) {
        super(spec, contractId);
        this.type = type;
    }

    @Override
    public List<T> resolve() {
        List<T> matchers = new ArrayList<>();
        traverser().requestParameterContracts(operationNode(), contractId(), MATCHERS, type.paramField())
                .forEach((parameterName, matcher) -> matchers.addAll(toMatcher(matcher, parameterName)));
        traverser().requestBodyContractMatchers(operationNode(), contractId(), type.requestField())
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

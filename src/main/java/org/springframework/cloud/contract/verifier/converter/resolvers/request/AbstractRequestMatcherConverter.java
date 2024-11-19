package org.springframework.cloud.contract.verifier.converter.resolvers.request;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.AbstractResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.RequestJsonPathTraverser;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.KEY;
import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.MATCHERS;
import static org.springframework.cloud.contract.verifier.converter.Utils.toStream;
import static org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathConstants.JSON_PATH_CONFIGURATION;

public abstract class AbstractRequestMatcherConverter<T> extends AbstractResolver<List<T>> {

    private final RequestElement type;

    public AbstractRequestMatcherConverter(Oa3Spec spec, String contractId, RequestElement type) {
        super(spec, contractId, new RequestJsonPathTraverser(JSON_PATH_CONFIGURATION));
        this.type = type;
    }

    @Override
    public List<T> resolve() {
        List<T> matchers = new ArrayList<>();
        traverser().parameterContracts(operationNode(), contractId(), MATCHERS, type.paramField())
                .forEach((parameterName, matcher) -> matchers.addAll(toMatcher(matcher, parameterName)));
        traverser().contractMatcherList(operationNode(), contractId(), type.requestField())
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

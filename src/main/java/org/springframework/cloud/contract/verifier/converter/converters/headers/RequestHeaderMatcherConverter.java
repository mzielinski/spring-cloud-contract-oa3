package org.springframework.cloud.contract.verifier.converter.converters.headers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.converter.converters.JsonPathTraverser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.REGEX_TYPE;
import static org.springframework.cloud.contract.verifier.converter.SccUtils.createPredefinedRegex;
import static org.springframework.cloud.contract.verifier.converter.SccUtils.createRegexType;
import static org.springframework.cloud.contract.verifier.converter.Utils.toStream;
import static org.springframework.cloud.contract.verifier.converter.Utils.toText;
import static org.springframework.cloud.contract.verifier.converter.converters.JsonPathConstants.JSON_PATH_CONFIGURATION;

public class RequestHeaderMatcherConverter {

    private final JsonPathTraverser traverser = new JsonPathTraverser(JSON_PATH_CONFIGURATION);
    private final Oa3Spec spec;
    private final String contractId;

    public RequestHeaderMatcherConverter(Oa3Spec spec, String contractId) {
        this.spec = spec;
        this.contractId = contractId;
    }

    public List<YamlContract.HeadersMatcher> convert() {
        List<YamlContract.HeadersMatcher> headersMatchers = new ArrayList<>();
        traverser.requestBodyHeaderMatchers(spec.operationNode(), contractId)
                .forEach(matcher -> headersMatchers
                        .addAll(toHeaderMatcher(matcher, matcher.get(KEY).asText())));
        traverser.parameterHeaders(spec.operationNode(), contractId, MATCHERS)
                .forEach((parameterName, matcher) -> headersMatchers
                        .addAll(toHeaderMatcher(matcher, parameterName)));
        return headersMatchers;
    }

    private List<YamlContract.HeadersMatcher> toHeaderMatcher(JsonNode matcher, String headerName) {
        return matcher.isArray()
                ? toStream(matcher.iterator()).map(subMatcher -> mapHeaderMatchers(headerName, subMatcher)).toList()
                : List.of(mapHeaderMatchers(headerName, matcher));
    }

    private YamlContract.HeadersMatcher mapHeaderMatchers(String headerName, JsonNode matcher) {
        YamlContract.HeadersMatcher headersMatcher = new YamlContract.HeadersMatcher();
        headersMatcher.key = headerName;
        headersMatcher.regex = toText(matcher.get(REGEX));
        headersMatcher.predefined = createPredefinedRegex(toText(matcher.get(PREDEFINED)));
        headersMatcher.command = toText(matcher.get(COMMAND));
        headersMatcher.regexType = createRegexType(toText(matcher.get(REGEX_TYPE)));
        return headersMatcher;
    }
}

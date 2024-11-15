package org.springframework.cloud.contract.verifier.converter.converters.request.matchers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.converter.converters.request.RequestElement;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.SccUtils.createPredefinedRegex;
import static org.springframework.cloud.contract.verifier.converter.SccUtils.createRegexType;
import static org.springframework.cloud.contract.verifier.converter.Utils.toText;

abstract class AbstractRequestKeyValueMatcherConverter extends AbstractRequestMatcherConverter<YamlContract.KeyValueMatcher> {

    AbstractRequestKeyValueMatcherConverter(Oa3Spec spec, String contractId, RequestElement type) {
        super(spec, contractId, type);
    }

    @Override
    protected YamlContract.KeyValueMatcher mapToMatchers(String headerName, JsonNode matcher) {
        YamlContract.KeyValueMatcher keyValueMatcher = new YamlContract.KeyValueMatcher();
        keyValueMatcher.key = headerName;
        keyValueMatcher.regex = toText(matcher.get(REGEX));
        keyValueMatcher.predefined = createPredefinedRegex(toText(matcher.get(PREDEFINED)));
        keyValueMatcher.command = toText(matcher.get(COMMAND));
        keyValueMatcher.regexType = createRegexType(toText(matcher.get(REGEX_TYPE)));
        return keyValueMatcher;
    }
}

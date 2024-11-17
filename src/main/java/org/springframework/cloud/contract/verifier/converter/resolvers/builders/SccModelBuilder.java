package org.springframework.cloud.contract.verifier.converter.resolvers.builders;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.contract.verifier.converter.YamlContract;

import java.util.Optional;
import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.Utils.toInteger;
import static org.springframework.cloud.contract.verifier.converter.Utils.toText;

public class SccModelBuilder {

    private static final Logger log = LoggerFactory.getLogger(SccModelBuilder.class);

    public SccModelBuilder() {
        throw new IllegalStateException("Utility class");
    }

    public static YamlContract.BodyStubMatcher toBodyStubMatcher(JsonNode node) {
        YamlContract.BodyStubMatcher matcher = new YamlContract.BodyStubMatcher();
        matcher.path = toText(node.get(PATH));
        matcher.value = toText(node.get(VALUE));
        matcher.predefined = createPredefinedRegex(toText(node.get(PREDEFINED)));
        matcher.minOccurrence = toInteger(node.get(MIN_OCCURRENCE));
        matcher.maxOccurrence = toInteger(node.get(MAX_OCCURRENCE));
        matcher.regexType = createRegexType(toText(node.get(REGEX_TYPE)));
        matcher.type = Optional.ofNullable(node.get(TYPE))
                .map(JsonNode::asText)
                .map(YamlContract.StubMatcherType::valueOf)
                .orElse(null);
        return matcher;
    }

    public static YamlContract.BodyTestMatcher toBodyTestMatcher(JsonNode node) {
        YamlContract.BodyTestMatcher bodyStubMatcher = new YamlContract.BodyTestMatcher();
        bodyStubMatcher.path = toText(node.get(PATH));
        bodyStubMatcher.value = toText(node.get(VALUE));
        bodyStubMatcher.predefined = createPredefinedRegex(toText(node.get(PREDEFINED)));
        bodyStubMatcher.minOccurrence = toInteger(node.get(MIN_OCCURRENCE));
        bodyStubMatcher.maxOccurrence = toInteger(node.get(MAX_OCCURRENCE));
        bodyStubMatcher.regexType = createRegexType(toText(node.get(REGEX_TYPE)));
        if (node.get(TYPE) != null) {
            bodyStubMatcher.type = YamlContract.TestMatcherType.valueOf(toText(node.get(TYPE)));
        }
        return bodyStubMatcher;
    }

    public static YamlContract.TestHeaderMatcher toTestHeaderMatcher(JsonNode node) {
        YamlContract.TestHeaderMatcher headersMatcher = new YamlContract.TestHeaderMatcher();
        headersMatcher.key = toText(node.get(KEY));
        headersMatcher.regex = toText(node.get(REGEX));
        headersMatcher.predefined = createPredefinedRegex(toText(node.get(PREDEFINED)));
        headersMatcher.command = toText(node.get(COMMAND));
        headersMatcher.regexType = createRegexType(toText(node.get(REGEX_TYPE)));
        return headersMatcher;
    }

    public static YamlContract.TestCookieMatcher toTestCookieMatcher(JsonNode node) {
        YamlContract.TestCookieMatcher testCookieMatcher = new YamlContract.TestCookieMatcher();
        testCookieMatcher.key = toText(node.get(KEY));
        testCookieMatcher.regex = toText(node.get(REGEX));
        testCookieMatcher.predefined = createPredefinedRegex(toText(node.get(PREDEFINED)));
        testCookieMatcher.command = toText(node.get(COMMAND));
        testCookieMatcher.regexType = createRegexType(toText(node.get(REGEX_TYPE)));
        return testCookieMatcher;
    }

    public static YamlContract.QueryParameterMatcher toQueryParameterMatcher(String parameterName, JsonNode matcher) {
        YamlContract.QueryParameterMatcher queryParameterMatcher = new YamlContract.QueryParameterMatcher();
        queryParameterMatcher.key = parameterName;
        queryParameterMatcher.value = matcher.get(VALUE);
        queryParameterMatcher.type = createMatchingType(matcher.get(TYPE).asText());
        return queryParameterMatcher;
    }

    public static  YamlContract.KeyValueMatcher toKeyValueMatcher(String name, JsonNode matcher) {
        YamlContract.KeyValueMatcher keyValueMatcher = new YamlContract.KeyValueMatcher();
        keyValueMatcher.key = name;
        keyValueMatcher.regex = toText(matcher.get(REGEX));
        keyValueMatcher.predefined = createPredefinedRegex(toText(matcher.get(PREDEFINED)));
        keyValueMatcher.command = toText(matcher.get(COMMAND));
        keyValueMatcher.regexType = createRegexType(toText(matcher.get(REGEX_TYPE)));
        return keyValueMatcher;
    }

    private static YamlContract.MatchingType createMatchingType(String val) {
        return tryToParse(val, YamlContract.MatchingType::valueOf);
    }

    private static YamlContract.PredefinedRegex createPredefinedRegex(String val) {
        return tryToParse(val, YamlContract.PredefinedRegex::valueOf);
    }

    private static YamlContract.RegexType createRegexType(String val) {
        return tryToParse(val, YamlContract.RegexType::valueOf);
    }

    private static <T> T tryToParse(String val, Function<String, T> function) {
        if (isNotBlank(val)) {
            try {
                return function.apply(val);
            } catch (Exception e) {
                log.error("Error parsing value {}", val, e);
            }
        }
        return null;
    }
}

package org.springframework.cloud.contract.verifier.converter.resolvers.builders;

import com.fasterxml.jackson.databind.JsonNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.contract.verifier.converter.YamlContract.*;

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

    public static Named toNamed(JsonNode node) {
        Named named = new Named();
        named.paramName = toText(node.get(PARAM_NAME));
        named.fileName = toText(node.get(FILE_NAME));
        named.fileContent = toText(node.get(FILE_CONTENT));
        named.fileContentAsBytes = toText(node.get(FILE_CONTENT_AS_BYTES));
        named.fileContentFromFileAsBytes = toText(node.get(FILE_CONTENT_FROM_FILE_AS_BYTES));
        named.contentType = toText(node.get(CONTENT_TYPE));
        named.fileNameCommand = toText(node.get(FILE_NAME_COMMAND));
        named.fileContentCommand = toText(node.get(FILE_CONTENT_COMMAND));
        named.contentTypeCommand = toText(node.get(CONTENT_TYPE_COMMAND));
        return named;
    }

    public static BodyStubMatcher toBodyStubMatcher(JsonNode node) {
        BodyStubMatcher matcher = new BodyStubMatcher();
        matcher.path = toText(node.get(PATH));
        matcher.value = toText(node.get(VALUE));
        matcher.predefined = createPredefinedRegex(toText(node.get(PREDEFINED)));
        matcher.minOccurrence = toInteger(node.get(MIN_OCCURRENCE));
        matcher.maxOccurrence = toInteger(node.get(MAX_OCCURRENCE));
        matcher.regexType = createRegexType(toText(node.get(REGEX_TYPE)));
        matcher.type = createStubMatcherType(toText(node.get(TYPE)));
        return matcher;
    }

    public static BodyTestMatcher toBodyTestMatcher(JsonNode node) {
        BodyTestMatcher matcher = new BodyTestMatcher();
        matcher.path = toText(node.get(PATH));
        matcher.value = toText(node.get(VALUE));
        matcher.predefined = createPredefinedRegex(toText(node.get(PREDEFINED)));
        matcher.minOccurrence = toInteger(node.get(MIN_OCCURRENCE));
        matcher.maxOccurrence = toInteger(node.get(MAX_OCCURRENCE));
        matcher.regexType = createRegexType(toText(node.get(REGEX_TYPE)));
        matcher.type = createTestMatcherType(toText(node.get(TYPE)));
        return matcher;
    }

    public static TestHeaderMatcher toTestHeaderMatcher(JsonNode node) {
        TestHeaderMatcher matcher = new TestHeaderMatcher();
        matcher.key = toText(node.get(KEY));
        matcher.regex = toText(node.get(REGEX));
        matcher.predefined = createPredefinedRegex(toText(node.get(PREDEFINED)));
        matcher.command = toText(node.get(COMMAND));
        matcher.regexType = createRegexType(toText(node.get(REGEX_TYPE)));
        return matcher;
    }

    public static TestCookieMatcher toTestCookieMatcher(JsonNode node) {
        TestCookieMatcher matcher = new TestCookieMatcher();
        matcher.key = toText(node.get(KEY));
        matcher.regex = toText(node.get(REGEX));
        matcher.predefined = createPredefinedRegex(toText(node.get(PREDEFINED)));
        matcher.command = toText(node.get(COMMAND));
        matcher.regexType = createRegexType(toText(node.get(REGEX_TYPE)));
        return matcher;
    }

    public static QueryParameterMatcher toQueryParameterMatcher(String parameterName, JsonNode node) {
        QueryParameterMatcher matcher = new QueryParameterMatcher();
        matcher.key = parameterName;
        matcher.value = node.get(VALUE);
        matcher.type = createMatchingType(node.get(TYPE).asText());
        return matcher;
    }

    public static KeyValueMatcher toKeyValueMatcher(String name, JsonNode node) {
        KeyValueMatcher matcher = new KeyValueMatcher();
        matcher.key = name;
        matcher.regex = toText(node.get(REGEX));
        matcher.predefined = createPredefinedRegex(toText(node.get(PREDEFINED)));
        matcher.command = toText(node.get(COMMAND));
        matcher.regexType = createRegexType(toText(node.get(REGEX_TYPE)));
        return matcher;
    }

    private static TestMatcherType createTestMatcherType(String val) {
        return tryToParse(val, TestMatcherType::valueOf);
    }

    private static StubMatcherType createStubMatcherType(String val) {
        return tryToParse(val, StubMatcherType::valueOf);
    }

    private static MatchingType createMatchingType(String val) {
        return tryToParse(val, MatchingType::valueOf);
    }

    private static PredefinedRegex createPredefinedRegex(String val) {
        return tryToParse(val, PredefinedRegex::valueOf);
    }

    private static RegexType createRegexType(String val) {
        return tryToParse(val, RegexType::valueOf);
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

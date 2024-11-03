package org.springframework.cloud.contract.verifier.converter;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.YamlContract.BodyTestMatcher;
import org.springframework.cloud.contract.verifier.converter.YamlContract.TestCookieMatcher;
import org.springframework.cloud.contract.verifier.converter.YamlContract.TestHeaderMatcher;
import org.springframework.cloud.contract.verifier.converter.YamlContract.TestMatcherType;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.SccUtils.createPredefinedRegex;
import static org.springframework.cloud.contract.verifier.converter.SccUtils.createRegexType;
import static org.springframework.cloud.contract.verifier.converter.Utils.*;

class Oa3ToSccResponse {

    private final Oa3Spec spec;
    private final String contractId;

    Oa3ToSccResponse(Oa3Spec spec, String contractId) {
        this.spec = spec;
        this.contractId = contractId;
    }

    YamlContract.Response convertToResponse() {
        JsonNode responseNode = spec.operationNode().get(RESPONSES);
        return toStream(responseNode.fields())
                .map(entry -> findContract(responseNode, entry.getKey(), contractId)
                        .map(contract -> convertToResponse(entry.getKey(), entry.getValue(), contract))
                        .orElseGet(YamlContract.Response::new))
                .findAny()
                .orElseGet(YamlContract.Response::new);
    }

    private YamlContract.Response convertToResponse(String responseCode, JsonNode spec, JsonNode contract) {
        YamlContract.Response yamlResponse = new YamlContract.Response();

        // response basic
        yamlResponse.status = Integer.parseInt(responseCode.replaceAll("[^a-zA-Z0-9 ]+", ""));
        yamlResponse.body = contract.get(BODY); // Convert to expected object
        yamlResponse.bodyFromFile = toText(contract.get(BODY_FROM_FILE_AS_BYTES));
        yamlResponse.bodyFromFileAsBytes = toText(contract.get(BODY_FROM_FILE_AS_BYTES));

        // response headers
        getContentType(spec).ifPresent(contentType -> yamlResponse.headers.put(CONTENT_TYPE_HTTP_HEADER, contentType));
        yamlResponse.headers.putAll(toMap(contract.get(HEADERS)));

        // response body matchers
        yamlResponse.matchers.body.addAll(findSubNodes(contract, MATCHERS, BODY)
                .map(this::buildBodyTestMatcher).toList());

        // response header matchers
        yamlResponse.matchers.headers.addAll(findSubNodes(contract, MATCHERS, HEADERS)
                .map(this::buildTestHeaderMatchers).toList());

        // response cookies matchers
        yamlResponse.matchers.cookies.addAll(findSubNodes(contract, MATCHERS, COOKIES)
                .map(this::buildTestCookieMatchers).toList());

        return yamlResponse;
    }

    private TestHeaderMatcher buildTestHeaderMatchers(JsonNode node) {
        TestHeaderMatcher headersMatcher = new TestHeaderMatcher();
        headersMatcher.key = toText(node.get(KEY));
        headersMatcher.regex = toText(node.get(REGEX));
        headersMatcher.predefined = createPredefinedRegex(toText(node.get(PREDEFINED)));
        headersMatcher.command = toText(node.get(COMMAND));
        headersMatcher.regexType = createRegexType(toText(node.get(REGEX_TYPE)));
        return headersMatcher;
    }

    private TestCookieMatcher buildTestCookieMatchers(JsonNode node) {
        TestCookieMatcher testCookieMatcher = new TestCookieMatcher();
        testCookieMatcher.key = toText(node.get(KEY));
        testCookieMatcher.regex = toText(node.get(REGEX));
        testCookieMatcher.predefined = createPredefinedRegex(toText(node.get(PREDEFINED)));
        testCookieMatcher.command = toText(node.get(COMMAND));
        testCookieMatcher.regexType = createRegexType(toText(node.get(REGEX_TYPE)));
        return testCookieMatcher;
    }

    private BodyTestMatcher buildBodyTestMatcher(JsonNode node) {
        BodyTestMatcher bodyStubMatcher = new BodyTestMatcher();
        bodyStubMatcher.path = toText(node.get(PATH));
        bodyStubMatcher.value = toText(node.get(VALUE));
        bodyStubMatcher.predefined = createPredefinedRegex(toText(node.get(PREDEFINED)));
        bodyStubMatcher.minOccurrence = toInteger(node.get(MIN_OCCURRENCE));
        bodyStubMatcher.maxOccurrence = toInteger(node.get(MAX_OCCURRENCE));
        bodyStubMatcher.regexType = createRegexType(toText(node.get(REGEX_TYPE)));
        if (node.get(TYPE) != null) {
            bodyStubMatcher.type = TestMatcherType.valueOf(toText(node.get(TYPE)));
        }
        return bodyStubMatcher;
    }
}

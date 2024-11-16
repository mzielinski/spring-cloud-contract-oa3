package org.springframework.cloud.contract.verifier.converter;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.matchers.RequestCookieMatcherConverter;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.matchers.RequestHeaderMatcherConverter;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.matchers.RequestQueryParameterMatcherConverter;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters.RequestCookieConverter;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters.RequestHeaderConverter;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters.RequestQueryParameterConverter;

import java.util.Map;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.SccUtils.createPredefinedRegex;
import static org.springframework.cloud.contract.verifier.converter.SccUtils.createRegexType;
import static org.springframework.cloud.contract.verifier.converter.Utils.*;

class Oa3ToSccRequest {

    private final Oa3Spec spec;
    private final String contractId;
    private final JsonNode contract;

    Oa3ToSccRequest(Oa3Spec spec, String contractId) {
        this.spec = spec;
        this.contractId = contractId;
        this.contract = findContract(spec.operationNode(), contractId)
                .orElseThrow(() -> new IllegalArgumentException("Contract " + contractId + " not found"));
    }

    YamlContract.Request convertToRequest() {
        YamlContract.Request yamlRequest = new YamlContract.Request();

        // basic parameters
        yamlRequest.urlPath = find(contract, CONTRACT_PATH)
                .map(Utils::toText)
                .orElseGet(() -> spec.calculatePath(contractId));
        yamlRequest.method = spec.httpMethod().toUpperCase();

        // query parameters
        yamlRequest.queryParameters.putAll(new RequestQueryParameterConverter(spec, contractId).convert());
        yamlRequest.matchers.queryParameters.addAll(new RequestQueryParameterMatcherConverter(spec, contractId).convert());

        // headers
        yamlRequest.headers.putAll(new RequestHeaderConverter(spec, contractId).convert());
        yamlRequest.matchers.headers.addAll(new RequestHeaderMatcherConverter(spec, contractId).convert());

        // cookies
        yamlRequest.cookies.putAll(new RequestCookieConverter(spec, contractId).convert());
        yamlRequest.matchers.cookies.addAll(new RequestCookieMatcherConverter(spec, contractId).convert());

//        yamlRequest.body = get(requestBody, BODY);
//        yamlRequest.bodyFromFile = get(requestBody, BODY_FROM_FILE);
//        yamlRequest.bodyFromFileAsBytes = get(requestBody, BODY_FROM_FILE_AS_BYTES);
//
//        // request body multipart
//        Map<String, Object> requestBodyMultipart = getOrDefault(requestBody, MULTIPART, EMPTY_MAP);
//        if (!requestBodyMultipart.isEmpty()) {
//            yamlRequest.multipart = new YamlContract.Multipart();
//            yamlRequest.matchers.multipart = new YamlContract.MultipartStubMatcher();
//            yamlRequest.multipart.params.putAll(getOrDefault(requestBodyMultipart, PARAMS, Map.of()));
//            yamlRequest.multipart.named.addAll(getOrDefault(requestBodyMultipart, NAMED, EMPTY_LIST).stream()
//                    .map(contractNamed -> {
//                        YamlContract.Named named = new YamlContract.Named();
//                        named.paramName = get(contractNamed, PARAM_NAME);
//                        named.fileName = get(contractNamed, FILE_NAME);
//                        named.fileContent = get(contractNamed, FILE_CONTENT);
//                        named.fileContentAsBytes = get(contractNamed, FILE_CONTENT_AS_BYTES);
//                        named.fileContentFromFileAsBytes = get(contractNamed, FILE_CONTENT_FROM_FILE_AS_BYTES);
//                        named.contentType = get(contractNamed, CONTENT_TYPE);
//                        named.fileNameCommand = get(contractNamed, FILE_NAME_COMMAND);
//                        named.fileContentCommand = get(contractNamed, FILE_CONTENT_COMMAND);
//                        named.contentTypeCommand = get(contractNamed, CONTENT_TYPE_COMMAND);
//                        return named;
//                    }).toList());
//        }
//
        // matchers
//        convertMatchers(resquestBody, yamlRequest);

        return yamlRequest;
    }



    private void convertMatchers(JsonNode requestNode, YamlContract.Request request) {
        // request body url matchers

//        request.matchers.url = buildKeyValueMatcher(getOrDefault(matchers, URL, EMPTY_MAP));

//        // request body matchers
//        List<YamlContract.BodyStubMatcher> requestBodyStubMatchers = getOrDefault(matchers, BODY, EMPTY_LIST).stream()
//                .map(this::buildBodyStubMatcher).toList();
//        request.matchers.body.addAll(requestBodyStubMatchers);
//
//        Map<String, Object> matchersMultipart = getOrDefault(matchers, MULTIPART, EMPTY_MAP);
//        if (!matchersMultipart.isEmpty()) {
//            // params
//            List<YamlContract.KeyValueMatcher> multipartParams = getOrDefault(matchersMultipart, PARAMS, EMPTY_LIST).stream()
//                    .map(this::buildKeyValueMatcher)
//                    .toList();
//            request.matchers.multipart.params.addAll(multipartParams);
//
//            // named
//            var stubMatchers = getOrDefault(matchersMultipart, NAMED, EMPTY_LIST).stream()
//                    .map(multipartNamed -> {
//                        YamlContract.MultipartNamedStubMatcher stubMatcher = new YamlContract.MultipartNamedStubMatcher();
//                        stubMatcher.paramName = get(multipartNamed, PARAM_NAME);
//                        stubMatcher.fileName = buildValueMatcher(multipartNamed, FILE_NAME);
//                        stubMatcher.fileContent = buildValueMatcher(multipartNamed, FILE_CONTENT);
//                        stubMatcher.contentType = buildValueMatcher(multipartNamed, CONTENT_TYPE_HTTP_HEADER);
//                        return stubMatcher;
//                    }).toList();
//            request.matchers.multipart.named.addAll(stubMatchers);
//        }
    }



    private YamlContract.BodyStubMatcher buildBodyStubMatcher(JsonNode node) {
        YamlContract.BodyStubMatcher bodyStubMatcher = new YamlContract.BodyStubMatcher();
        bodyStubMatcher.path = toText(node.get(PATH));
        bodyStubMatcher.value = toText(node.get(VALUE));
        bodyStubMatcher.predefined = createPredefinedRegex(toText(node.get(PREDEFINED)));
        bodyStubMatcher.minOccurrence = toInteger(node.get(MIN_OCCURRENCE));
        bodyStubMatcher.maxOccurrence = toInteger(node.get(MAX_OCCURRENCE));
        bodyStubMatcher.regexType = createRegexType(toText(node.get(REGEX_TYPE)));
        if (node.get(TYPE) != null) {
            bodyStubMatcher.type = YamlContract.StubMatcherType.valueOf(toText(node.get(TYPE)));
        }
        return bodyStubMatcher;
    }

    private YamlContract.ValueMatcher buildValueMatcher(Map<String, Object> matcher, String key) {
        Map<String, Object> map = getOrDefault(matcher, key, EMPTY_MAP);
        if (map.isEmpty()) {
            return null;
        }
        YamlContract.ValueMatcher valueMatcher = new YamlContract.ValueMatcher();
        valueMatcher.regex = getOrDefault(map, REGEX, null);
        valueMatcher.predefined = createPredefinedRegex(get(map, PREDEFINED));
        return valueMatcher;
    }
}

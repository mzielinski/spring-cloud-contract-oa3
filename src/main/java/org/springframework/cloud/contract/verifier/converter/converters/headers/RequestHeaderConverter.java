package org.springframework.cloud.contract.verifier.converter.converters.headers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.converter.converters.JsonPathTraverser;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.SccUtils.createPredefinedRegex;
import static org.springframework.cloud.contract.verifier.converter.SccUtils.createRegexType;
import static org.springframework.cloud.contract.verifier.converter.Utils.*;
import static org.springframework.cloud.contract.verifier.converter.Utils.toStream;
import static org.springframework.cloud.contract.verifier.converter.converters.JsonPathConstants.JSON_PATH_CONFIGURATION;

public class RequestHeaderConverter {

    private final JsonPathTraverser traverser = new JsonPathTraverser(JSON_PATH_CONFIGURATION);
    private final Oa3Spec spec;
    private final String contractId;

    public RequestHeaderConverter(Oa3Spec spec, String contractId) {
        this.spec = spec;
        this.contractId = contractId;
    }

    public Map<String, Object> convert() {
        Map<String, Object> headers = new LinkedHashMap<>();
        traverser.requestBodyContentType(spec.operationNode())
                .ifPresent(contentType -> headers.put(CONTENT_TYPE_HTTP_HEADER, contentType));
        System.out.println(headers);

        // request headers
//        getContentType(spec.operationNode().get(REQUEST_BODY))
//                .ifPresent(contentType -> );
//        yamlRequest.headers.putAll(toMap(contract.get(HEADERS)));
//
//        yamlRequest.headers.putAll(getOrDefault(requestBody, HEADERS, EMPTY_MAP));
//
//        find(spec.operationNode(), PARAMETERS)
//                .ifPresent(parameters -> toStream(parameters.iterator())
//                        .filter(parameter -> HEADER.equalsIgnoreCase(toText(parameter.get(IN))))
//                        .forEach(parameter -> findContract(parameter, contractId)
//                                .ifPresent(contract -> {
//                                    String parameterName = toText(parameter.get(NAME));
//                                    yamlRequest.headers.put(parameterName, contract.get(VALUE));
//                                    toStream(contract.fields())
//                                            .filter(fields -> MATCHERS.equalsIgnoreCase(fields.getKey()))
//                                            .map(Map.Entry::getValue)
//                                            .forEach(matcher -> yamlRequest.matchers.headers.addAll(
//                                                    buildHeaderMatchers(toStream(matcher.iterator()), parameterName)
//                                            ));
//                                })
//                        ));

        return headers;
    }

    private List<YamlContract.HeadersMatcher> buildHeaderMatchers(Stream<JsonNode> contracts, String name) {
        return contracts.map(matcher -> {
            YamlContract.HeadersMatcher headersMatcher = new YamlContract.HeadersMatcher();
            headersMatcher.key = name;
            headersMatcher.regex = toText(matcher.get(REGEX));
            headersMatcher.predefined = createPredefinedRegex(toText(matcher.get(PREDEFINED)));
            headersMatcher.command = toText(matcher.get(COMMAND));
            headersMatcher.regexType = createRegexType(toText(matcher.get(REGEX_TYPE)));
            return headersMatcher;
        }).toList();
    }
}

package org.springframework.cloud.contract.verifier.converter.converters.headers;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.converter.converters.JsonPathTraverser;

import static org.springframework.cloud.contract.verifier.converter.converters.JsonPathConstants.JSON_PATH_CONFIGURATION;

public class RequestHeaderMatcherConverter {

    private final JsonPathTraverser traverser = new JsonPathTraverser(JSON_PATH_CONFIGURATION);
    private final Oa3Spec spec;
    private final String contractId;

    public RequestHeaderMatcherConverter(Oa3Spec spec, String contractId) {
        this.spec = spec;
        this.contractId = contractId;
    }

    public void convert(YamlContract.Request yamlRequest) {

        // request body headers matchers
//        request.matchers.headers.addAll(getOrDefault(matchers, HEADERS, EMPTY_LIST).stream()
//                .map(this::buildKeyValueMatcher).toList());


//        // request body headers matchers
//        request.matchers.headers.addAll(getOrDefault(matchers, HEADERS, EMPTY_LIST).stream()
//                .map(this::buildKeyValueMatcher).toList());
//

    }
}

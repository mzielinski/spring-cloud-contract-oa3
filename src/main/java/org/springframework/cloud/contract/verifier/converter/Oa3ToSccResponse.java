package org.springframework.cloud.contract.verifier.converter;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.resolvers.builders.SccModelBuilder;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
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
                .map(SccModelBuilder::toBodyTestMatcher).toList());

        // response header matchers
        yamlResponse.matchers.headers.addAll(findSubNodes(contract, MATCHERS, HEADERS)
                .map(SccModelBuilder::toTestHeaderMatcher).toList());

        // response cookies matchers
        yamlResponse.matchers.cookies.addAll(findSubNodes(contract, MATCHERS, COOKIES)
                .map(SccModelBuilder::toTestCookieMatcher).toList());

        return yamlResponse;
    }


}

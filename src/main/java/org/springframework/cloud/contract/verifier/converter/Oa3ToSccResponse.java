package org.springframework.cloud.contract.verifier.converter;

import org.springframework.cloud.contract.verifier.converter.resolvers.response.headers.ResponseHeaderResolver;

class Oa3ToSccResponse {

    private final Oa3Spec spec;
    private final String contractId;

    Oa3ToSccResponse(Oa3Spec spec, String contractId) {
        this.spec = spec;
        this.contractId = contractId;
    }

    YamlContract.Response resolveResponse() {
        YamlContract.Response yamlResponse = new YamlContract.Response();

        // response basic
//        yamlResponse.status = Integer.parseInt(responseCode.replaceAll("[^a-zA-Z0-9 ]+", ""));

        // headers
        yamlResponse.headers.putAll(new ResponseHeaderResolver(spec, contractId).resolve());

//        yamlResponse.matchers.headers.addAll(new RequestHeaderMatcherConverter(spec, contractId).resolve());

        // cookies
//        yamlResponse.cookies.putAll(new RequestCookieResolver(spec, contractId).resolve());
//        yamlResponse.matchers.cookies.addAll(new RequestCookieMatcherConverter(spec, contractId).resolve());

        // body
//        yamlResponse.body = new RequestBodyResolver(spec, contractId).resolve();
//        yamlResponse.bodyFromFile = new RequestBodyFileResolver(spec, contractId, BODY_FROM_FILE).resolve();
//        yamlResponse.bodyFromFileAsBytes = new RequestBodyFileResolver(spec, contractId, BODY_FROM_FILE_AS_BYTES).resolve();
//        yamlResponse.matchers.body = new RequestBodyMatcherResolver(spec, contractId).resolve();

//
//        // response body matchers
//        yamlResponse.matchers.body.addAll(findSubNodes(contract, MATCHERS, BODY)
//                .map(SccModelBuilder::toBodyTestMatcher).toList());
//
//        // response header matchers
//        yamlResponse.matchers.headers.addAll(findSubNodes(contract, MATCHERS, HEADERS)
//                .map(SccModelBuilder::toTestHeaderMatcher).toList());
//
//        // response cookies matchers
//        yamlResponse.matchers.cookies.addAll(findSubNodes(contract, MATCHERS, COOKIES)
//                .map(SccModelBuilder::toTestCookieMatcher).toList());

        return yamlResponse;
    }
}

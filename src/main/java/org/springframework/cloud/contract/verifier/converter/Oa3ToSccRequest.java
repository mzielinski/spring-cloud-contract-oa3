package org.springframework.cloud.contract.verifier.converter;

import org.springframework.cloud.contract.verifier.converter.resolvers.request.body.RequestBodyFileResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.body.RequestBodyMatcherResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.body.RequestBodyResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters.RequestCookieMatcherConverter;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters.RequestHeaderMatcherConverter;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters.RequestQueryParameterMatcherConverter;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.multipart.RequestMultipartMatcherResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.multipart.RequestMultipartResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters.RequestCookieResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters.RequestHeaderResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters.RequestQueryParameterResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.url.UrlPathMatcherResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.url.UrlPathResolver;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.BODY_FROM_FILE;
import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.BODY_FROM_FILE_AS_BYTES;

class Oa3ToSccRequest {

    private final Oa3Spec spec;
    private final String contractId;

    Oa3ToSccRequest(Oa3Spec spec, String contractId) {
        this.spec = spec;
        this.contractId = contractId;
    }

    YamlContract.Request resolveRequest() {
        YamlContract.Request yamlRequest = new YamlContract.Request();

        // basic parameters
        yamlRequest.method = spec.httpMethod().toUpperCase();

        // url
        yamlRequest.urlPath = new UrlPathResolver(spec, contractId).resolve();
        yamlRequest.matchers.url = new UrlPathMatcherResolver(spec, contractId).resolve();

        // query parameters
        yamlRequest.queryParameters.putAll(new RequestQueryParameterResolver(spec, contractId).resolve());
        yamlRequest.matchers.queryParameters.addAll(new RequestQueryParameterMatcherConverter(spec, contractId).resolve());

        // headers
        yamlRequest.headers.putAll(new RequestHeaderResolver(spec, contractId).resolve());
        yamlRequest.matchers.headers.addAll(new RequestHeaderMatcherConverter(spec, contractId).resolve());

        // cookies
        yamlRequest.cookies.putAll(new RequestCookieResolver(spec, contractId).resolve());
        yamlRequest.matchers.cookies.addAll(new RequestCookieMatcherConverter(spec, contractId).resolve());

        // body
        yamlRequest.body = new RequestBodyResolver(spec, contractId).resolve();
        yamlRequest.bodyFromFile = new RequestBodyFileResolver(spec, contractId, BODY_FROM_FILE).resolve();
        yamlRequest.bodyFromFileAsBytes = new RequestBodyFileResolver(spec, contractId, BODY_FROM_FILE_AS_BYTES).resolve();
        yamlRequest.matchers.body = new RequestBodyMatcherResolver(spec, contractId).resolve();

        // request body multipart
        yamlRequest.multipart = new RequestMultipartResolver(spec, contractId).resolve();
        yamlRequest.matchers.multipart = new RequestMultipartMatcherResolver(spec, contractId).resolve();

        return yamlRequest;
    }
}

package org.springframework.cloud.contract.verifier.converter.resolvers.request.multipart;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.Utils;
import org.springframework.cloud.contract.verifier.converter.YamlContract.MultipartStubMatcher;
import org.springframework.cloud.contract.verifier.converter.resolvers.builders.SccModelBuilder;
import org.springframework.cloud.contract.verifier.converter.resolvers.AbstractResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.RequestJsonPathTraverser;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathConstants.JSON_PATH_CONFIGURATION;

public class RequestMultipartMatcherResolver extends AbstractResolver<MultipartStubMatcher> {

    public RequestMultipartMatcherResolver(Oa3Spec spec, String contractId) {
        super(spec, contractId, new RequestJsonPathTraverser(JSON_PATH_CONFIGURATION));
    }

    @Override
    public MultipartStubMatcher resolve() {
        var stubMatcher = new MultipartStubMatcher();
        var multipartMatchers = traverser().contractMatchers(operationNode(), contractId(), MULTIPART);
        stubMatcher.params = Optional.ofNullable(multipartMatchers.get(PARAMS))
                .map(JsonNode::iterator)
                .map(Utils::toStream)
                .orElseGet(Stream::empty)
                .map(SccModelBuilder::toKeyValueMatcher).toList();

        stubMatcher.named = Optional.ofNullable(multipartMatchers.get(NAMED))
                .map(JsonNode::iterator)
                .map(Utils::toStream)
                .map(Stream::toList)
                .map(nodes -> nodes.stream()
                        .map(SccModelBuilder::toMultipartNamedStubMatcher)
                        .toList())
                .orElse(List.of());
        return stubMatcher;
    }
}

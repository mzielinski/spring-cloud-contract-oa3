package org.springframework.cloud.contract.verifier.converter.resolvers.request.multipart;

import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract.MultipartStubMatcher;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.AbstractResolver;

public class RequestMultipartMatcherResolver extends AbstractResolver<MultipartStubMatcher> {

    public RequestMultipartMatcherResolver(Oa3Spec spec, String contractId) {
        super(spec, contractId);
    }

    @Override
    public MultipartStubMatcher resolve() {

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

        return new MultipartStubMatcher();
    }

//    private YamlContract.ValueMatcher buildValueMatcher(Map<String, Object> matcher, String key) {
//        Map<String, Object> map = getOrDefault(matcher, key, EMPTY_MAP);
//        if (map.isEmpty()) {
//            return null;
//        }
//        YamlContract.ValueMatcher valueMatcher = new YamlContract.ValueMatcher();
//        valueMatcher.regex = getOrDefault(map, REGEX, null);
//        valueMatcher.predefined = createPredefinedRegex(get(map, PREDEFINED));
//        return valueMatcher;
//    }
}

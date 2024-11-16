package org.springframework.cloud.contract.verifier.converter.resolvers.request.matchers

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cloud.contract.verifier.converter.Oa3Spec
import org.springframework.cloud.contract.verifier.converter.YamlContract
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class RequestCookieMatchersConverterTest extends Specification {

    private static final String CONTRACT_ID = 'contract1'
    private final def objectMapper = new ObjectMapper()

    @Shared
    String json = getClass().getResourceAsStream('/unit/oa3.json').getText()

    def 'should successfully convert cookie matchers'() {
        given:
        def converter = new RequestCookieMatcherConverter(
                new Oa3Spec(
                        "/check-matchers/1",
                        "post",
                        objectMapper.readTree(json),
                        null
                ), CONTRACT_ID
        )

        when:
        List<YamlContract.KeyValueMatcher> result = converter.convert()

        then:
        result.size() == 2
        result.contains(new YamlContract.KeyValueMatcher(
                key: 'cookieFoo',
                regex: '[0-9]'
        ))
        result.contains(new YamlContract.KeyValueMatcher(
                key: 'cookieBar',
                command: 'equals($it)'
        ))
    }

    @Unroll
    def 'should return empty list when cookies matchers cannot be found for given contract'() {
        given:
        def converter = new RequestCookieMatcherConverter(
                new Oa3Spec(
                        "/check-matchers/1",
                        "post",
                        objectMapper.readTree(content),
                        null
                ), contractId
        )

        expect:
        converter.convert().size() == 0

        where:
        contractId  | content
        CONTRACT_ID | '{}'
        'unknown'   | json
    }
}

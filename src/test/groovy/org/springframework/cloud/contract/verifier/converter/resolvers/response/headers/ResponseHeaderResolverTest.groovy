package org.springframework.cloud.contract.verifier.converter.resolvers.response.headers

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cloud.contract.verifier.converter.Oa3Spec
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class ResponseHeaderResolverTest extends Specification {

    private static final String CONTRACT_ID = 'contract1'
    private final def objectMapper = new ObjectMapper()

    @Shared
    String json = getClass().getResourceAsStream('/unit/oa3.json').getText()

    def 'should successfully convert headers'() {
        given:
        def converter = new ResponseHeaderResolver(
                new Oa3Spec(
                        "/check-matchers/1",
                        "post",
                        objectMapper.readTree(json),
                        null
                ), CONTRACT_ID
        )

        when:
        Map<String, String> result = converter.resolve()
                .collectEntries {
                    [(it.key): it.value as String]
                }

        then:
        result.size() == 2
        result['Content-Type'] == '"application/json"'
        result['X-RateLimit-Limit'] == '60'
    }

    @Unroll
    def 'should return empty list when headers cannot be found for given contract'() {
        given:
        def converter = new ResponseHeaderResolver(
                new Oa3Spec(
                        "/check-matchers/1",
                        "post",
                        objectMapper.readTree(content),
                        null
                ), contractId
        )

        when:
        Map<String, Object> headers = converter.resolve()

        then:
        headers.size() == 1
        headers['Content-Type'] == 'application/json'

        where:
        contractId  | content
        CONTRACT_ID | '{"responses":{"200": {"content":{"application/json":""}}}}'
        'unknown'   | json
    }
}

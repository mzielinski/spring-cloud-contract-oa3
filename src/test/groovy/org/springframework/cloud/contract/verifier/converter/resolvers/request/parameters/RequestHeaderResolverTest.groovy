package org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cloud.contract.verifier.converter.Oa3Spec
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class RequestHeaderResolverTest extends Specification {

    private static final String CONTRACT_ID = 'contract1'
    private final def objectMapper = new ObjectMapper()

    @Shared
    String json = getClass().getResourceAsStream('/unit/oa3.json').getText()

    def 'should successfully convert headers'() {
        given:
        def converter = new RequestHeaderResolver(
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
        result.size() == 3
        result['Content-Type'] == '"application/json;charset=UTF-8"'
        result['headerFoo'] == '"should be overridden by definition in parameters"'
        result['headerFoo2'] == '"should be overridden by definition in parameters"'
    }

    @Unroll
    def 'should return empty list when headers cannot be found for given contract'() {
        given:
        def converter = new RequestHeaderResolver(
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
        CONTRACT_ID | '{"requestBody":{"content":{"application/json":""}}}'
        'unknown'   | json
    }
}

package org.springframework.cloud.contract.verifier.converter.resolvers.request.parameters

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cloud.contract.verifier.converter.Oa3Spec
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class RequestQueryParameterResolverTest extends Specification {

    private static final String CONTRACT_ID = 'contract1'
    private final def objectMapper = new ObjectMapper()

    @Shared
    String json = getClass().getResourceAsStream('/unit/oa3.json').getText()

    def 'should successfully convert query parameters'() {
        given:
        def converter = new RequestQueryParameterResolver(
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
        result.size() == 8
        result['offset'] == '20'
        result['limit'] == '20'
        result['filter'] == '"email"'
        result['search'] == '55'
        result['name'] == '"John.Doe"'
        result['sort'] == '"name"'
        result['age'] == '99'
        result['email'] == '"bob@email.com"'
    }

    @Unroll
    def 'should return empty list when query parameters cannot be found for given contract'() {
        given:
        def converter = new RequestQueryParameterMatcherConverter(
                new Oa3Spec(
                        "/check-matchers/1",
                        "post",
                        objectMapper.readTree(content),
                        null
                ), contractId
        )

        expect:
        converter.resolve().isEmpty()

        where:
        contractId  | content
        CONTRACT_ID | '{}'
        'unknown'   | json
    }

}

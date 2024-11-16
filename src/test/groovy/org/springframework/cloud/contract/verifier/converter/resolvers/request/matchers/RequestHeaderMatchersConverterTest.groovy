package org.springframework.cloud.contract.verifier.converter.resolvers.request.matchers

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.cloud.contract.verifier.converter.Oa3Spec
import org.springframework.cloud.contract.verifier.converter.YamlContract
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.cloud.contract.verifier.converter.YamlContract.PredefinedRegex.*
import static org.springframework.cloud.contract.verifier.converter.YamlContract.RegexType.as_string

class RequestHeaderMatchersConverterTest extends Specification {

    private static final String CONTRACT_ID = 'contract1'
    private final def objectMapper = new ObjectMapper()

    @Shared
    String json = getClass().getResourceAsStream('/unit/oa3.json').getText()

    def 'should successfully convert header matchers'() {
        given:
        def converter = new RequestHeaderMatcherConverter(
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
        result.size() == 3
        result.contains(new YamlContract.KeyValueMatcher(
                key: 'Content-Type',
                regex: 'application/json.*'
        ))
        result.contains(new YamlContract.KeyValueMatcher(
                key: 'headerFoo',
                regex: 'bar.*',
                predefined: only_alpha_unicode,
                command: 'thing($it)',
                regexType: as_string
        ))
        result.contains(new YamlContract.KeyValueMatcher(
                key: 'headerFoo',
                regex: 'barTest',
                predefined: only_alpha_unicode,
                command: 'thing($it)',
                regexType: as_string
        ))
    }

    @Unroll
    def 'should return empty list when headers matchers cannot be found for given contract'() {
        given:
        def converter = new RequestHeaderMatcherConverter(
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

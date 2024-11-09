package org.springframework.cloud.contract.verifier.converter.converters

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

import static org.springframework.cloud.contract.verifier.converter.converters.JsonPathConstants.JSON_PATH_CONFIGURATION

class JsonPathTraverserTest extends Specification {

    private static final String CONTRACT_ID = 'contract1'
    private final def objectMapper = new ObjectMapper()
    private final def traverser = new JsonPathTraverser(JSON_PATH_CONFIGURATION)

    @Shared
    String json = getClass().getResourceAsStream('/unit/oa3.json').getText()

    def 'should find all matchers for query parameters for given contract'() {
        given:
        JsonNode input = objectMapper.readTree(json)

        when:
        List<String> result = traverser.requestBodyQueryParameterMatchers(input, CONTRACT_ID).collect {
            it.toString()
        }

        then:
        result.size() == 7
        result.contains('{"key":"limit","type":"equal_to","value":20}')
        result.contains('{"key":"offset","type":"containing","value":20}')
        result.contains('{"key":"sort","type":"equal_to","value":"name"}')
        result.contains('{"key":"search","type":"not_matching","value":"^[0-9]{2}$"}')
        result.contains('{"key":"age","type":"not_matching","value":"^\\\\w*$"}')
        result.contains('{"key":"name","type":"matching","value":"John.*"}')
        result.contains('{"key":"hello","type":"absent"}')
    }

    @Unroll
    def 'should return empty list when query parameters cannot be found for given contract'() {
        given:
        JsonNode input = objectMapper.readTree(content)

        expect:
        traverser.requestBodyQueryParameterMatchers(input, contractId).size() == 0
        traverser.requestBodyQueryParameters(input, contractId).size() == 0
        traverser.queryParameters(input, contractId, 'value').size() == 0

        where:
        contractId | content
        CONTRACT_ID | '{}'
        'unknown'  | json
    }

    def 'should find all request body query parameters for given contract'() {
        given:
        JsonNode input = objectMapper.readTree(json)

        when:
        Map<String, String> result = traverser.requestBodyQueryParameters(input, CONTRACT_ID)
                .collectEntries {
                    [(it.key): it.value as String]
                }

        then:
        result.size() == 2
        result['offset'] == '20'
        result['limit'] == '20'
    }

    def 'should find all query parameters for given contract'() {
        given:
        JsonNode input = objectMapper.readTree(json)

        when:
        Map<String, String> result = traverser.queryParameters(input, CONTRACT_ID, 'value')
                .collectEntries {
                    [(it.key): it.value as String]
                }

        then:
        result.size() == 8
        result['filter'] == '"email"'
        result['search'] == '55'
        result['offset'] == '[20]'
        result['limit'] == '10'
        result['name'] == '"John.Doe"'
        result['sort'] == '"name"'
        result['age'] == '99'
        result['email'] == '"bob@email.com"'
    }

    def 'should find all query parameters matchers for given contract'() {
        given:
        JsonNode input = objectMapper.readTree(json)

        when:
        Map<String, String> result = traverser.queryParameters(input, CONTRACT_ID, 'matchers')
                .collectEntries {
                    [(it.key): it.value as String]
                }

        then:
        result.size() == 1
        result['filter'] == '[{"type":"matching","value":"(email|name)"},{"type":"equal_to","value":"email"}]'
    }
}

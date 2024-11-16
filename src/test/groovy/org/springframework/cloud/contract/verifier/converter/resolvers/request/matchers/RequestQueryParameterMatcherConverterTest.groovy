package org.springframework.cloud.contract.verifier.converter.resolvers.request.matchers

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.node.IntNode
import com.fasterxml.jackson.databind.node.TextNode
import org.springframework.cloud.contract.verifier.converter.Oa3Spec
import org.springframework.cloud.contract.verifier.converter.YamlContract
import spock.lang.Shared
import spock.lang.Specification
import spock.lang.Unroll

class RequestQueryParameterMatcherConverterTest extends Specification {

    private static final String CONTRACT_ID = 'contract1'
    private final def objectMapper = new ObjectMapper()

    @Shared
    String json = getClass().getResourceAsStream('/unit/oa3.json').getText()

    def 'should successfully convert query parameters matchers'() {
        given:
        def converter = new RequestQueryParameterMatcherConverter(
                new Oa3Spec(
                        "/check-matchers/1",
                        "post",
                        objectMapper.readTree(json),
                        null
                ), CONTRACT_ID
        )

        when:
        List<YamlContract.QueryParameterMatcher> result = converter.resolve()

        then:
        result.size() == 9
        result.contains(new YamlContract.QueryParameterMatcher(
                key: "limit",
                type: YamlContract.MatchingType.equal_to,
                value: new IntNode(20)
        ))
        result.contains(new YamlContract.QueryParameterMatcher(
                key: "offset",
                type: YamlContract.MatchingType.containing,
                value: new IntNode(20)
        ))
        result.contains(new YamlContract.QueryParameterMatcher(
                key: "sort",
                type: YamlContract.MatchingType.equal_to,
                value: new TextNode("name")
        ))
        result.contains(new YamlContract.QueryParameterMatcher(
                key: "search",
                type: YamlContract.MatchingType.not_matching,
                value: new TextNode("^[0-9]{2}\$")
        ))
        result.contains(new YamlContract.QueryParameterMatcher(
                key: "age",
                type: YamlContract.MatchingType.not_matching,
                value: new TextNode("^\\w*\$")
        ))
        result.contains(new YamlContract.QueryParameterMatcher(
                key: "name",
                type: YamlContract.MatchingType.matching,
                value: new TextNode("John.*")
        ))
        result.contains(new YamlContract.QueryParameterMatcher(
                key: "hello",
                type: YamlContract.MatchingType.absent
        ))
        result.contains(new YamlContract.QueryParameterMatcher(
                key: "filter",
                type: YamlContract.MatchingType.matching,
                value: new TextNode("(email|name)")
        ))
        result.contains(new YamlContract.QueryParameterMatcher(
                key: "filter",
                type: YamlContract.MatchingType.equal_to,
                value: new TextNode("email")
        ))
    }

    @Unroll
    def 'should return empty list when query parameters matchers cannot be found for given contract'() {
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
        converter.resolve().size() == 0

        where:
        contractId  | content
        CONTRACT_ID | '{}'
        'unknown'   | json
    }
}

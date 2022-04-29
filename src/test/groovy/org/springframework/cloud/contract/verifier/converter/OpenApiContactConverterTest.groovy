package org.springframework.cloud.contract.verifier.converter

import org.springframework.cloud.contract.spec.Contract
import spock.lang.Specification
import spock.lang.Unroll

class OpenApiContactConverterTest extends Specification {

    OpenApiContractConverter objectUnderTest = new OpenApiContractConverter()
    YamlContractConverter yamlContractConverter = new YamlContractConverter()

    @Unroll
    def 'should accept valid oa3 documentations #filename'() {
        given:
        File file = loadFile("openapi/$filename")

        expect:
        objectUnderTest.isAccepted(file)

        when:
        Collection<Contract> contracts = objectUnderTest.convertFrom(file)

        then:
        contracts.size() == expectedNumberOfContracts

        where:
        filename                      || expectedNumberOfContracts
        'verify_fraud_service.yml'    || 6
        'verify_matchers.yml'         || 1
        'sample/swagger_petstore.yml' || 3
        'sample/payor.yml'            || 4
        'sample/velo_payments.yml'    || 10
    }

    @Unroll
    def 'should reject invalid oa3 documentation #filename and do not throw exception'() {
        when:
        boolean accepted = objectUnderTest.isAccepted(file)

        then:
        noExceptionThrown()
        !accepted

        where:
        file << [
                new File('does-not-exists.yaml'),
                loadFile('openapi/verify_invalid_oa3.yml')
        ]
        filename = file.name
    }

    def 'should verify that contracts generated from #oa3Filename documentation are the same as expected #contractFilename'() {
        when:
        Collection<Contract> expectedContracts = yamlContractConverter.convertFrom(loadFile("yml/$contractFilename"))
        Collection<Contract> oa3Contracts = objectUnderTest.convertFrom(loadFile("openapi/$oa3Filename"))

        then:
        oa3Contracts.each { contract ->
            assert expectedContracts.find { it.name == contract.name } == contract
        }

        where:
        oa3Filename                || contractFilename
        'verify_fraud_service.yml' || 'contract_fraud_service.yml'
        'verify_matchers.yml'      || 'contract_verify_matchers.yml'
    }


    private static File loadFile(filepath) {
        new File(OpenApiContactConverterTest.classLoader.getResource(filepath).toURI())
    }
}

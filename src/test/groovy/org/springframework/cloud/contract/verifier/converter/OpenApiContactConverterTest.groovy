package org.springframework.cloud.contract.verifier.converter

import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.FromFileProperty
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
        filename                             || expectedNumberOfContracts
        'verify_oa3.yml'                     || 3
        'verify_body_from_file_as_bytes.yml' || 1
        'verify_fraud_service.yml'           || 6
        'verify_swagger_petstore.yml'        || 3
        'sample/payor.yml'                   || 4
        'sample/velo_payments.yml'           || 10
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

    @Unroll
    def 'should verify that contracts generated from #oa3Filename documentation are the same as expected #contractFilename'() {
        when:
        Collection<Contract> expectedContracts = yamlContractConverter.convertFrom(loadFile("yml/$contractFilename"))
        Collection<Contract> oa3Contracts = objectUnderTest.convertFrom(loadFile("openapi/$oa3Filename"))

        then:
        oa3Contracts.each { contract ->
            contract == expectedContracts.find { it.name == contract.name }
        }

        where:
        oa3Filename                   || contractFilename
        'verify_swagger_petstore.yml' || 'contract_swagger_petstore.yml'
        'verify_fraud_service.yml'    || 'contract_fraud_service.yml'
        'verify_oa3.yml'              || 'contract_oa3.yml'
    }

    def 'should verify that bodyFromFileAsBytes is properly converted to contract'() {
        when:
        Contract oa3Contract = objectUnderTest.convertFrom(loadFile('openapi/verify_body_from_file_as_bytes.yml')).first()

        then:
        oa3Contract.name == 'Should verify body from file as bytes'
        with(oa3Contract.request.body) {
            verifyFromFileAsBytes(it.clientValue, 'request.json')
            verifyFromFileAsBytes(it.serverValue, 'request.json')
        }
        with(oa3Contract.response.body) {
            verifyFromFileAsBytes(it.clientValue, 'response.json')
            verifyFromFileAsBytes(it.serverValue, 'response.json')
        }
    }

    private static void verifyFromFileAsBytes(def value, String filename) {
        assert value instanceof FromFileProperty
        assert value.type == byte[].class
        assert value.file.name == filename
    }

    private static File loadFile(filepath) {
        new File(OpenApiContactConverterTest.classLoader.getResource(filepath).toURI())
    }
}

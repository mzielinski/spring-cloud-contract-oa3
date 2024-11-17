package org.springframework.cloud.contract.verifier.converter

import groovy.json.JsonOutput
import groovy.yaml.YamlSlurper
import org.springframework.cloud.contract.spec.Contract
import org.springframework.cloud.contract.spec.internal.FromFileProperty
import spock.lang.Specification
import spock.lang.Unroll

class OpenApiContactConverterTest extends Specification {

    OpenApiContractConverter objectUnderTest = new OpenApiContractConverter()
    YamlContractConverter yamlContractConverter = new YamlContractConverter()

    @Unroll
    def 'should not accept valid oa3 documentations without contracts'() {
        given:
        File file = loadFile('spec/verify_swagger_petstore_without_contracts.yml')

        expect:
        !objectUnderTest.isAccepted(file)
    }

    @Unroll
    def 'should accept valid oa3 documentations #filename'() {
        given:
        File file = loadFile("spec/$filename")

        expect:
        objectUnderTest.isAccepted(file)

        when:
        Collection<Contract> contracts = objectUnderTest.convertFrom(file)

        then:
        contracts.size() == expectedNumberOfContracts

        where:
        filename                             || expectedNumberOfContracts
        'verify_playground.yml'              || 1
        'verify_oa3.yml'                     || 4
        'verify_body_from_file_as_bytes.yml' || 1
        'verify_fraud_service.yml'           || 6
        'verify_swagger_petstore.yml'        || 3
        'openapi_payor.yml'                  || 4
        'openapi_velo_payments.yml'          || 10
        'openapi_sccoa3_basic.json'          || 3
        'openapi_sccoa3_basic.yml'           || 3
//        'asyncapi_sccoa3_basic.yml'          || 1
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
                loadFile('spec/verify_invalid_oa3.yml')
        ]
        filename = file.name
    }

    @Unroll
    def 'should verify that contracts generated from #oa3Filename documentation are the same as expected #contractFilename'() {
        given:
        File oa3Yaml = loadFile("spec/$oa3Filename")
        Collection<Contract> expectedContracts = yamlContractConverter.convertFrom(loadFile("contracts/$contractFilename"))

        expect: 'YAML support'
        objectUnderTest.convertFrom(oa3Yaml).each { contract ->
            assert contract == expectedContracts.find { it.name == contract.name }
        }

        when:
        String filename = oa3Yaml.name.replaceAll('.yml', '.json')
        File jsonFile = saveToFile(filename, convertYamlToJson(oa3Yaml))

        then: 'JSON support'
        objectUnderTest.convertFrom(jsonFile).each { contract ->
            assert contract == expectedContracts.find { it.name == contract.name }
        }

        where:
        oa3Filename                   || contractFilename
        'verify_swagger_petstore.yml' || 'contract_swagger_petstore.yml'
        'verify_fraud_service.yml'    || 'contract_fraud_service.yml'
        'verify_oa3.yml'              || 'contract_oa3.yml'
        'verify_playground.yml'       || 'contract_playground.yml'
        'verify_path_parameter.yml'   || 'contract_path_parameter.yml'
//        'asyncapi_sccoa3_basic.yml'   || 'contract_asyncapi_sccoa3_basic.yml'
    }

    def 'should verify that bodyFromFileAsBytes is properly converted to contract'() {
        when:
        Contract oa3Contract = objectUnderTest.convertFrom(loadFile('spec/verify_body_from_file_as_bytes.yml')).first()

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

    private static File saveToFile(String filename, String content) {
        File targetDir = new File('target/test-classes/openapi-json')
        targetDir.mkdir()
        File jsonFile = new File(targetDir, filename)
        jsonFile.withWriter('UTF-8') { writer ->
            writer.write(content)
        }
        return jsonFile
    }

    private static String convertYamlToJson(File yamlFile) {
        def yamlContent = new YamlSlurper().parse(yamlFile)
        JsonOutput.prettyPrint(JsonOutput.toJson(yamlContent))
    }
}

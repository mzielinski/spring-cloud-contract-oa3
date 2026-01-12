package org.springframework.cloud.contract.verifier.openapivalidation;

import org.junit.jupiter.api.Test;

import java.net.URISyntaxException;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiContractsVerifierTest {

    @Test
    void should_report_all_contract_mismatches_when_contracts_do_not_match_openapi() throws Exception {
        // given
        Path openApiSpec = resourcePath("openapi/verify_contract_validation.yml");
        Path contractsDir = resourcePath("contracts/validation/invalid");
        OpenApiContractsVerifier verifier = new OpenApiContractsVerifier();

        // when
        OpenApiVerificationReport report = verifier.verify(openApiSpec, contractsDir);

        // then
        assertThat(report.hasViolations()).isTrue();
        assertThat(report.violations()).hasSize(2);
        assertThat(report.render()).contains("POST /foo");
        assertThat(report.render()).contains("POST /bar/123");
    }

    @Test
    void should_accept_all_contracts_when_contracts_match_openapi() throws Exception {
        // given
        Path openApiSpec = resourcePath("openapi/verify_contract_validation.yml");
        Path contractsDir = resourcePath("contracts/validation/valid");
        OpenApiContractsVerifier verifier = new OpenApiContractsVerifier();

        // when
        OpenApiVerificationReport report = verifier.verify(openApiSpec, contractsDir);

        // then
        assertThat(report.hasViolations()).isFalse();
        assertThat(report.violations()).isEmpty();
    }

    private static Path resourcePath(String resource) throws URISyntaxException {
        return Path.of(OpenApiContractsVerifierTest.class.getClassLoader().getResource(resource).toURI());
    }
}

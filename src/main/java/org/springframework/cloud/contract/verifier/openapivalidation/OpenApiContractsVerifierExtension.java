package org.springframework.cloud.contract.verifier.openapivalidation;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.nio.file.Path;

/**
 * JUnit 5 extension that validates SCC contracts against OpenAPI specs.
 *
 * @since 5.0.2
 */
public class OpenApiContractsVerifierExtension implements BeforeAllCallback {

    static final String OPENAPI_SPEC_PROPERTY = "scc.oa3.spec";
    static final String CONTRACTS_DIR_PROPERTY = "scc.contracts.dir";

    private final OpenApiContractsVerifier verifier = new OpenApiContractsVerifier();

    @Override
    /**
     * Runs validation once before all tests in the class.
     *
     * @since 5.0.2
     */
    public void beforeAll(ExtensionContext context) {
        VerificationConfiguration configuration = resolveConfiguration(context);
        OpenApiVerificationReport report = verifier.verify(configuration.openApiSpec(), configuration.contractsDir());
        if (report.hasViolations()) {
            throw new AssertionError(report.render());
        }
    }

    private VerificationConfiguration resolveConfiguration(ExtensionContext context) {
        VerifyContractsAgainstOpenApi annotation = context.getRequiredTestClass()
                .getAnnotation(VerifyContractsAgainstOpenApi.class);
        String openApiSpec = annotation != null ? annotation.openApiSpec() : "";
        String contractsDir = annotation != null ? annotation.contractsDir() : "";

        if (openApiSpec.isBlank()) {
            openApiSpec = System.getProperty(OPENAPI_SPEC_PROPERTY, "");
        }
        if (contractsDir.isBlank()) {
            contractsDir = System.getProperty(CONTRACTS_DIR_PROPERTY, "");
        }

        if (openApiSpec.isBlank() || contractsDir.isBlank()) {
            throw new IllegalStateException("OpenAPI spec and contracts directory must be configured via "
                    + "@VerifyContractsAgainstOpenApi or system properties");
        }

        return new VerificationConfiguration(Path.of(openApiSpec), Path.of(contractsDir));
    }

    private record VerificationConfiguration(Path openApiSpec, Path contractsDir) {
    }
}

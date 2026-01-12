package org.springframework.cloud.contract.verifier.openapivalidation;

import java.nio.file.Path;

/**
 * CLI entry point for validating contracts against OpenAPI specs.
 *
 * @since 5.0.2
 */
public final class OpenApiContractsVerifierMain {

    private OpenApiContractsVerifierMain() {
        throw new AssertionError("Utility class");
    }

    /**
     * Runs validation from the command line.
     *
     * @since 5.0.2
     */
    public static void main(String[] args) {
        if (args.length != 2) {
            System.err.println("Usage: <openapi-spec-path> <contracts-directory>");
            System.exit(2);
        }

        Path specPath = Path.of(args[0]);
        Path contractsDir = Path.of(args[1]);

        OpenApiVerificationReport report = new OpenApiContractsVerifier().verify(specPath, contractsDir);
        if (report.hasViolations()) {
            System.err.println(report.render());
            System.exit(1);
        }
        System.out.println(report.render());
    }
}

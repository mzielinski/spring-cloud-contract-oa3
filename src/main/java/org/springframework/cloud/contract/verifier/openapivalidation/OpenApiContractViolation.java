package org.springframework.cloud.contract.verifier.openapivalidation;

import java.nio.file.Path;

/**
 * Represents a single OpenAPI contract validation violation.
 *
 * @since 5.0.2
 */
public record OpenApiContractViolation(
        /**
         * Contract source path.
         *
         * @since 5.0.2
         */
        Path sourcePath,
        /**
         * Contract name.
         *
         * @since 5.0.2
         */
        String contractName,
        /**
         * Violation message.
         *
         * @since 5.0.2
         */
        String message) {
}

package org.springframework.cloud.contract.verifier.openapivalidation;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enables OpenAPI contract verification for JUnit 5 tests.
 *
 * @since 5.0.2
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(OpenApiContractsVerifierExtension.class)
public @interface VerifyContractsAgainstOpenApi {

    /**
     * OpenAPI spec path.
     *
     * @since 5.0.2
     */
    String openApiSpec() default "";

    /**
     * Contracts directory path.
     *
     * @since 5.0.2
     */
    String contractsDir() default "";
}

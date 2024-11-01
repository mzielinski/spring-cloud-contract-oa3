# Spring Cloud Contract OpenAPI 3.0 Contract Converter

[![Maven Central](https://img.shields.io/maven-central/v/io.github.mzielinski/spring-cloud-contract-oa3.svg)](https://search.maven.org/artifact/io.github.mzielinski/spring-cloud-contract-oa3)
[![CircleCI](https://circleci.com/gh/mzielinski/spring-cloud-contract-oa3.svg?style=svg)](https://circleci.com/gh/mzielinski/spring-cloud-contract-oa3)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mzielinski_spring-cloud-contract-oa3&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mzielinski_spring-cloud-contract-oa3)

This project is an extension for [**Spring Cloud Contract**](https://spring.io/projects/spring-cloud-contract) allowing
contracts to be defined directly within the [**OpenAPI Specification**](https://swagger.io/specification/). It supports
both **YAML** and **JSON** formats, aiming to provide a **Single Source of Truth** for both the API and Contract
definitions.

This extension is built on top of
a [springframeworkguru repository](https://github.com/springframeworkguru/spring-cloud-contract-oa3 ), which is no
longer
maintained. The entire project was rewritten in **Java 17**, moving away from the previous **Groovy**-based
implementation. The rewrite also introduced new features and comprehensive tests to ensure stability.

## Releases

[List of all releases](https://github.com/mzielinski/spring-cloud-contract-oa3/releases)

## Key Features

- **Single Source of Truth**: The OpenAPI specification defines the API and Contracts using `x-contract` extensions,
  ensuring consistency across development and testing.
- **Contract Generation**: Contracts defined in OpenAPI `x-contract` extensions are automatically converted into **SCC
  Contracts**, which serve as input for Spring Cloud Contract.
- **Artifact Generation**: From a single OpenAPI Specification, the following artifacts can be generated:
    - **Code**: API code generated from the OpenAPI Specification.
    - **Test Contracts**: Test contract files generated from `x-contracts` (for the producer part).
    - **WireMock Stub Server**: Stubs generated for consumer-side testing.

## Usage

This extension allows you to centralize your contract and API definition processes, streamlining API development and
testing with [**Spring Cloud Contract**](https://spring.io/projects/spring-cloud-contract).

### Define OpenAPI Specification

Define OpenAPI with contracts embedded in the `x-contract` extensions. These extensions specify details about the API's
expected behavior for contract testing.

[Sample OpenAPI 3.0 Specification used in the tests](https://github.com/mzielinski/spring-cloud-contract-oa3/tree/master/src/test/resources/openapi)

Basic example in YAML. An example in JSON can also be found at the link above.

```yaml
openapi: 3.0.0
info:
  version: "1.0.0"
  description: "Example how to define x-contract inside OpenAPI 3.0"
  title: Spring Cloud Contract OpenAPI 3.0 Basic Example
paths:
  /v1/events:
    get:
      operationId: 'conference-events'
      summary: Retrieve all events for given day
      parameters:
        - in: query
          name: date
          schema:
            type: string
            format: date
            pattern: "\\d{4}-\\d{2}-\\d{2}"
            example: '2024-12-13'
          required: true
          x-contracts:
            - contractId: 200
              value: '2024-12-13'
      x-contracts:
        - contractId: 200
          name: Should return events for given day with HTTP status code 200
      responses:
        '200':
          description: return conference events for given day
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/event-response'
          x-contracts:
            - contractId: 200
              body:
                date: '2024-12-13'
                events: [ {
                  name: 'Consumer-Driven Contract Workshops'
                } ]
              headers:
                Content-Type: application/json

components:
  schemas:
    event-response:
      required:
        - date
        - events
      type: object
      properties:
        date:
          description: date of events
          type: string
          pattern: "\\d{4}-\\d{2}-\\d{2}"
          example: '2024-12-13'
        events:
          description: list of events
          type: array
          items:
            $ref: '#/components/schemas/event'

    event:
      required:
        - name
      type: object
      properties:
        name:
          type: string
          example: 'Consumer-Driven Contract Workshops'
```

### Define Spring Cloud Contract Configuration

- **Gradle**

```groovy
dependencies {
    testImplementation("io.github.mzielinski:spring-cloud-contract-oa3:$springCloudContractOa3Version")
}

contracts {
    // Path to the directory containing your OpenAPI specifications with x-contract extensions
    contractsDslDir = new File("__path_to_dir_with_openapi_specifications__")
    // (...) Standard Spring Cloud Configuration
}
``` 

- **Maven**:

```xml

<plugin>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-contract-maven-plugin</artifactId>
    <version>${spring-cloud-contract.version}</version>
    <executions>
        <execution>
            <goals>
                <goal>convert</goal>
                <goal>generateStubs</goal>
                <goal>generateTests</goal>
            </goals>
        </execution>
    </executions>
    <configuration>
        <packageWithBaseClasses>com.mzielinski.sccoa3.examples</packageWithBaseClasses>
        <contractsDirectory>${project.basedir}/src/main/resources/openapi</contractsDirectory>
        <testFramework>JUNIT5</testFramework>
    </configuration>
    <dependencies>
        <dependency>
            <groupId>io.github.mzielinski</groupId>
            <artifactId>spring-cloud-contract-oa3</artifactId>
            <version>${spring-cloud-contract-oa3.version}</version>
        </dependency>
    </dependencies>
</plugin>
```

## Versioning

The versioning of this project aligns with the version of **Spring Cloud Contract**. This approach ensures compatibility
and consistency between the extension and the core Spring Cloud Contract framework. When selecting a version of this
extension, choose the one that corresponds to the version of Spring Cloud Contract used in your project.

## Future Plans

- **Reverse Generation**: Support for generating OpenAPI specifications from Spring Cloud Contracts (not yet
  implemented).
- **AsyncAPI Support**: Plans to support **AsyncAPI** for asynchronous operations, allowing contract-based testing of
  event-driven architectures.

## Explore the full Example Project with Producer and Consumer side

[Spring-Cloud-Contract-OA3-Examples](https://github.com/mzielinski/spring-cloud-contract-oa3-examples) repository
contains an example project that demonstrates the complete setup using a sample OpenAPI specification.
You can explore the example to understand how to configure and use this extension with Spring Cloud Contract.

## Jitpack vs Maven Central

Since version [4.1.1.2](https://github.com/mzielinski/spring-cloud-contract-oa3/releases/tag/v4.1.4.1)
and [3.1.10.6](https://github.com/mzielinski/spring-cloud-contract-oa3/releases/tag/v3.1.10.5) [jitpack](https://jitpack.io/#mzielinski/spring-cloud-contract-oa3)
was replaced with
[Maven Central Repository](https://central.sonatype.com/artifact/io.github.mzielinski/spring-cloud-contract-oa3).
Previous releases are available only on jitpack.

## Contributing

If you're interested in contributing, or if you have any ideas or feedback, feel free to open an issue or submit a pull
request.

## License

This project is licensed under the **Apache License 2.0**. See the [LICENSE](LICENSE) file for details.
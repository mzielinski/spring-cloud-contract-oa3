# Spring Cloud Contract OpenAPI 3.0 Contract Converter

[![](https://jitpack.io/v/mzielinski/spring-cloud-contract-oa3.svg)](https://jitpack.io/#mzielinski/spring-cloud-contract-oa3)
[![CircleCI](https://circleci.com/gh/mzielinski/spring-cloud-contract-oa3.svg?style=svg)](https://circleci.com/gh/mzielinski/spring-cloud-contract-oa3)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=mzielinski_spring-cloud-contract-oa3&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=mzielinski_spring-cloud-contract-oa3)

This project is an extension for [**Spring Cloud Contract**](https://spring.io/projects/spring-cloud-contract) allowing
contracts to be defined directly within the [**OpenAPI Specification**](https://swagger.io/specification/). It supports
both **YAML** and **JSON** formats, aiming to provide a **Single Source of Truth** for both the API and Contract
definitions.

This extension is built on top of
a [springframeworkguru repository](https://github.com/springframeworkguru/spring-cloud-contract-oa3 ), which is no longer
maintained. The entire project was rewritten in **Java 17**, moving away from the previous **Groovy**-based
implementation. The rewrite also introduced new features and comprehensive tests to ensure stability.

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

1. **Define your OpenAPI Specification** with contracts embedded in the `x-contract` extensions. These extensions
   specify details about the API's expected behavior for contract testing.
2. **Add the following Dependency** to your project together with Spring Cloud
   Contract dependencies:
    - **Gradle**:
     ```groovy
     repositories {
       maven { url 'https://jitpack.io' }
     }
   
     dependencies {
       testImplementation("com.github.mzielinski:spring-cloud-contract-oa3:$springCloudContractOa3Version")
     }
      ``` 
    - **Maven**:
     ```xml
     <repositories>
       <repository>
         <id>jitpack.io</id>
         <url>https://jitpack.io</url>
       </repository>
     </repositories>
   
     <dependency>
       <groupId>com.github.mzielinski</groupId>
       <artifactId>spring-cloud-contract-oa3</artifactId>
       <version>${springCloudContractOa3Version}</version>
       <scope>test</scope>
     </dependency>
     ```
3. **Define Spring Cloud Contract Configuration**  
   Next, configure Spring Cloud Contract in the standard way. For example, using a Gradle build script:

   ```groovy
   contracts {
     // Path to the directory containing your OpenAPI specifications with x-contract extensions
     contractsDslDir = new File("__path_to_dir_with_openapi_specifications__")
     // (...) Standard Spring Cloud Configuration
   }
   ```

This extension allows you to centralize your contract and API definition processes, streamlining API development and
testing with [**Spring Cloud Contract**](https://spring.io/projects/spring-cloud-contract).

## Explore the Example Project

[Spring-Cloud-Contract-OA3-Examples](git@github.com:mzielinski/spring-cloud-contract-oa3-examples.git) repository contains an example project that demonstrates the complete setup using a sample OpenAPI specification.
You can explore the example to understand how to configure and use this extension with Spring Cloud Contract.

## Versioning

The versioning of this project aligns with the version of **Spring Cloud Contract**. This approach ensures compatibility
and consistency between the extension and the core Spring Cloud Contract framework. When selecting a version of this
extension, choose the one that corresponds to the version of Spring Cloud Contract used in your project.

## Future Plans

- **Reverse Generation**: Support for generating OpenAPI specifications from Spring Cloud Contracts (not yet
  implemented).
- **AsyncAPI Support**: Plans to support **AsyncAPI** for asynchronous operations, allowing contract-based testing of
  event-driven architectures.

## Contributing

If you're interested in contributing, or if you have any ideas or feedback, feel free to open an issue or submit a pull
request.

## License

This project is licensed under the **Apache License 2.0**. See the [LICENSE](LICENSE) file for details.
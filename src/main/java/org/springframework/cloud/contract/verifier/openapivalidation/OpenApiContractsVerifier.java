package org.springframework.cloud.contract.verifier.openapivalidation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.springframework.cloud.contract.spec.Contract;
import org.springframework.cloud.contract.spec.ContractConverter;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Locale;

/**
 * Validates Spring Cloud Contract DSL files against an OpenAPI specification.
 *
 * @since 5.0.2
 */
public class OpenApiContractsVerifier {

    /**
     * Verifies contracts from a directory against an OpenAPI specification.
     *
     * @since 5.0.2
     */
    public OpenApiVerificationReport verify(Path openApiSpec, Path contractsDir) {
        List<OpenApiContractViolation> violations = new ArrayList<>();

        if (openApiSpec == null || !Files.isRegularFile(openApiSpec)) {
            violations.add(new OpenApiContractViolation(openApiSpec, "OpenAPI",
                    "OpenAPI specification file does not exist"));
            return new OpenApiVerificationReport(List.copyOf(violations));
        }
        if (contractsDir == null || !Files.isDirectory(contractsDir)) {
            violations.add(new OpenApiContractViolation(contractsDir, "Contracts",
                    "Contracts directory does not exist"));
            return new OpenApiVerificationReport(List.copyOf(violations));
        }

        OpenAPI openAPI = parseOpenApi(openApiSpec, violations);
        if (openAPI == null) {
            return new OpenApiVerificationReport(List.copyOf(violations));
        }

        OpenApiSpecIndex specIndex = OpenApiSpecIndex.from(openAPI);
        List<ContractSource> contracts = new ContractLoader().load(contractsDir, violations);
        for (ContractSource contractSource : contracts) {
            Contract contract = contractSource.contract();
            if (contract == null || contract.getIgnored() || contract.getInProgress()) {
                continue;
            }
            validateContract(specIndex, contractSource, violations);
        }

        return new OpenApiVerificationReport(List.copyOf(violations));
    }

    private OpenAPI parseOpenApi(Path openApiSpec, List<OpenApiContractViolation> violations) {
        try {
            OpenAPI openAPI = new OpenAPIV3Parser().read(openApiSpec.toString());
            if (openAPI == null || openAPI.getPaths() == null || openAPI.getPaths().isEmpty()) {
                violations.add(new OpenApiContractViolation(openApiSpec, "OpenAPI",
                        "OpenAPI specification contains no paths"));
                return null;
            }
            return openAPI;
        } catch (Exception e) {
            violations.add(new OpenApiContractViolation(openApiSpec, "OpenAPI",
                    "Failed to read OpenAPI specification: " + e.getMessage()));
            return null;
        }
    }

    private void validateContract(OpenApiSpecIndex specIndex, ContractSource contractSource,
                                  List<OpenApiContractViolation> violations) {
        Contract contract = contractSource.contract();
        String method = ContractDetails.method(contract);
        String path = ContractDetails.path(contract);
        Integer status = ContractDetails.status(contract);

        if (method == null || path == null || status == null) {
            violations.add(new OpenApiContractViolation(contractSource.path(), contractSource.name(),
                    "Contract must define request method, request URL, and response status"));
            return;
        }

        String requestSignature = method + " " + path;
        List<String> matchingPaths = specIndex.matchingPaths(path);
        if (matchingPaths.isEmpty()) {
            violations.add(new OpenApiContractViolation(contractSource.path(), contractSource.name(),
                    "No OpenAPI path matches contract request " + requestSignature));
            return;
        }

        boolean methodExists = matchingPaths.stream()
                .anyMatch(specPath -> specIndex.hasMethod(specPath, method));
        if (!methodExists) {
            violations.add(new OpenApiContractViolation(contractSource.path(), contractSource.name(),
                    "No OpenAPI operation matches contract request " + requestSignature));
            return;
        }

        String statusCode = status.toString();
        boolean responseExists = matchingPaths.stream()
                .anyMatch(specPath -> specIndex.hasResponse(specPath, method, statusCode));
        if (!responseExists) {
            violations.add(new OpenApiContractViolation(contractSource.path(), contractSource.name(),
                    "No OpenAPI response status " + statusCode + " for contract request " + requestSignature));
        }
    }

    private record ContractSource(Path path, String name, Contract contract) {
    }

    private static final class ContractDetails {

        private ContractDetails() {
            throw new AssertionError("Utility class");
        }

        private static String method(Contract contract) {
            if (contract.getRequest() == null || contract.getRequest().getMethod() == null) {
                return null;
            }
            Object value = ContractValueExtractor.extract(contract.getRequest().getMethod());
            return value != null ? value.toString().toUpperCase(Locale.ROOT) : null;
        }

        private static String path(Contract contract) {
            if (contract.getRequest() == null) {
                return null;
            }
            Object urlPath = ContractValueExtractor.extract(contract.getRequest().getUrlPath());
            if (urlPath != null) {
                return urlPath.toString();
            }
            Object url = ContractValueExtractor.extract(contract.getRequest().getUrl());
            return url != null ? url.toString() : null;
        }

        private static Integer status(Contract contract) {
            if (contract.getResponse() == null || contract.getResponse().getStatus() == null) {
                return null;
            }
            Object value = ContractValueExtractor.extract(contract.getResponse().getStatus());
            if (value == null) {
                return null;
            }
            if (value instanceof Number number) {
                return number.intValue();
            }
            String asString = value.toString();
            String digits = asString.replaceAll("[^0-9]", "");
            return digits.isEmpty() ? null : Integer.parseInt(digits);
        }
    }

    private static final class ContractLoader {

        private static final List<ContractConverter> CONVERTERS =
                SpringFactoriesLoader.loadFactories(ContractConverter.class, null);

        List<ContractSource> load(Path contractsDir, List<OpenApiContractViolation> violations) {
            List<ContractSource> contracts = new ArrayList<>();
            try (var paths = java.nio.file.Files.walk(contractsDir)) {
                paths.filter(Files::isRegularFile)
                        .sorted()
                        .forEach(path -> loadContracts(path, contracts, violations));
            } catch (Exception e) {
                violations.add(new OpenApiContractViolation(contractsDir, "Contracts",
                        "Failed to scan contracts directory: " + e.getMessage()));
            }
            return contracts;
        }

        private void loadContracts(Path path, List<ContractSource> contracts,
                                   List<OpenApiContractViolation> violations) {
            ParseResult result = ContractParser.parse(path);
            if (!result.accepted()) {
                return;
            }
            if (result.errorMessage() != null) {
                violations.add(new OpenApiContractViolation(path, path.getFileName().toString(),
                        result.errorMessage()));
                return;
            }
            if (result.contracts().isEmpty()) {
                violations.add(new OpenApiContractViolation(path, path.getFileName().toString(),
                        "No contracts found in file"));
                return;
            }
            for (int i = 0; i < result.contracts().size(); i++) {
                Contract contract = result.contracts().get(i);
                String name = contract.getName();
                if (name == null || name.isBlank()) {
                    name = path.getFileName().toString() + "#" + (i + 1);
                }
                contracts.add(new ContractSource(path, name, contract));
            }
        }
    }

    private static final class ContractParser {

        private ContractParser() {
            throw new AssertionError("Utility class");
        }

        @SuppressWarnings("unchecked")
        static ParseResult parse(Path path) {
            boolean accepted = false;
            for (ContractConverter converter : ContractLoader.CONVERTERS) {
                if (converter.isAccepted(path.toFile())) {
                    accepted = true;
                    try {
                        return new ParseResult(true, List.copyOf((Collection<Contract>) converter.convertFrom(path.toFile())),
                                null);
                    } catch (Exception e) {
                        return new ParseResult(true, List.of(),
                                "Failed to parse contract file: " + e.getMessage());
                    }
                }
            }
            return new ParseResult(accepted, List.of(), null);
        }
    }

    private record ParseResult(boolean accepted, List<Contract> contracts, String errorMessage) {
    }

    private static final class ContractValueExtractor {

        private ContractValueExtractor() {
            throw new AssertionError("Utility class");
        }

        static Object extract(org.springframework.cloud.contract.spec.internal.DslProperty<?> property) {
            if (property == null) {
                return null;
            }
            Object client = property.getClientValue();
            return client != null ? client : property.getServerValue();
        }
    }
}

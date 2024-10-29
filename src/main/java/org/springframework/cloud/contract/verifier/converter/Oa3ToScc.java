package org.springframework.cloud.contract.verifier.converter;

import io.swagger.v3.oas.models.OpenAPI;

import java.util.Map;
import java.util.stream.Stream;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.Utils.*;

class Oa3ToScc {

    private final ServiceNameVerifier serviceNameVerifier = new ServiceNameVerifier();

    Stream<YamlContract> convert(OpenAPI openApi) {
        return openApi.getPaths().entrySet().stream()
                .flatMap(path -> path.getValue().readOperations().stream()
                        .filter(operation -> operation.getExtensions() != null && operation.getExtensions().get(X_CONTRACTS) != null)
                        .flatMap(operation -> {
                            Oa3Spec spec = new Oa3Spec(path.getKey(), path.getValue(), operation);
                            return getOrDefault(operation.getExtensions(), X_CONTRACTS, EMPTY_LIST).stream()
                                    .filter(contracts -> serviceNameVerifier.checkServiceEnabled(get(contracts, SERVICE_NAME)))
                                    .map(contracts -> getYamlContract(spec, contracts));
                        }));
    }

    private YamlContract getYamlContract(Oa3Spec spec, Map<String, Object> contract) {
        Object contractId = get(contract, CONTRACT_ID);
        YamlContract yaml = new YamlContract();
        yaml.name = get(contract, NAME);
        yaml.description = get(contract, DESCRIPTION);
        yaml.priority = get(contract, PRIORITY);
        yaml.label = get(contract, LABEL);
        yaml.ignored = getOrDefault(contract, IGNORED, false);
        yaml.request = new Oa3ToSccRequest(spec, contractId).convert(contract);
        yaml.response = new Oa3ToSccResponse(spec, contractId).convert();
        return yaml;
    }
}

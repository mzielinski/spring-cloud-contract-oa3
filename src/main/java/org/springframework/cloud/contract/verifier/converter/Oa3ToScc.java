package org.springframework.cloud.contract.verifier.converter;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.Utils.*;

class Oa3ToScc {

    private final ServiceNameVerifier serviceNameVerifier = new ServiceNameVerifier();

    Stream<YamlContract> convert(List<JsonNode> nodes) {
        return nodes.stream().flatMap(node -> toStream(node.get(PATHS).fields())
                .flatMap(pathNode -> Arrays.stream(OPENAPI_OPERATIONS)
                        .filter(operation -> pathNode.getValue().get(operation) != null)
                        .flatMap(pathMethod -> xContracts(pathNode.getValue(), pathMethod)
                                .filter(contract -> serviceNameVerifier.checkServiceEnabled(contract.get(SERVICE_NAME)))
                                .map(contract -> {
                                    JsonNode value = pathNode.getValue();
                                    Oa3Spec spec = new Oa3Spec(pathNode.getKey(), pathMethod, value.get(pathMethod), value.get(PARAMETERS));
                                    return getYamlContract(spec, contract, toText(contract.get(CONTRACT_ID)));
                                }))));
    }

    private YamlContract getYamlContract(Oa3Spec spec, JsonNode contract, String contractId) {
        YamlContract yaml = new YamlContract();
        yaml.name = toText(contract.get(NAME));
        yaml.description = toText(contract.get(DESCRIPTION));
        yaml.priority = toInteger(contract.get(PRIORITY));
        yaml.label = toText(contract.get(LABEL));
        yaml.ignored = toBoolean(contract.get(IGNORED));
        yaml.request = new Oa3ToSccRequest(spec, contractId).resolveRequest();
        yaml.response = new Oa3ToSccResponse(spec, contractId).resolveResponse();
        return yaml;
    }
}

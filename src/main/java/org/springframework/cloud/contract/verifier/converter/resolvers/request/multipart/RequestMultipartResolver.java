package org.springframework.cloud.contract.verifier.converter.resolvers.request.multipart;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.converter.YamlContract.Multipart;
import org.springframework.cloud.contract.verifier.converter.resolvers.builders.SccModelBuilder;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.AbstractResolver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.Utils.toStream;

public class RequestMultipartResolver extends AbstractResolver<Multipart> {

    public RequestMultipartResolver(Oa3Spec spec, String contractId) {
        super(spec, contractId);
    }

    @Override
    public Multipart resolve() {
        Map<String, JsonNode> multipart = traverser().requestBodyContracts(operationNode(), contractId(), MULTIPART);
        if (!multipart.isEmpty()) {
            var yamlMultipart = new Multipart();
            yamlMultipart.params.putAll(paramsToMap(multipart));
            yamlMultipart.named.addAll(namedToList(multipart));
            return yamlMultipart;
        }
        return null;
    }

    private static List<YamlContract.Named> namedToList(Map<String, JsonNode> multipart) {
        return toStream(multipart.get(NAMED).iterator())
                .map(SccModelBuilder::toNamed)
                .toList();
    }

    private static Map<String, String> paramsToMap(Map<String, JsonNode> multipart) {
        JsonNode jsonNode = multipart.get(PARAMS);
        return toStream(jsonNode.fields())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().asText()));
    }
}

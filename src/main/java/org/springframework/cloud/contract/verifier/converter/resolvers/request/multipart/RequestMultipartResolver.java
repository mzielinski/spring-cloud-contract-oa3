package org.springframework.cloud.contract.verifier.converter.resolvers.request.multipart;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.YamlContract;
import org.springframework.cloud.contract.verifier.converter.YamlContract.Multipart;
import org.springframework.cloud.contract.verifier.converter.resolvers.request.AbstractResolver;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.Utils.toStream;
import static org.springframework.cloud.contract.verifier.converter.Utils.toText;

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
                .map(named -> {
                    YamlContract.Named yamlNamed = new YamlContract.Named();
                    yamlNamed.paramName = toText(named.get(PARAM_NAME));
                    yamlNamed.fileName = toText(named.get(FILE_NAME));
                    yamlNamed.fileContent = toText(named.get(FILE_CONTENT));
                    yamlNamed.fileContentAsBytes = toText(named.get(FILE_CONTENT_AS_BYTES));
                    yamlNamed.fileContentFromFileAsBytes = toText(named.get(FILE_CONTENT_FROM_FILE_AS_BYTES));
                    yamlNamed.contentType = toText(named.get(CONTENT_TYPE));
                    yamlNamed.fileNameCommand = toText(named.get(FILE_NAME_COMMAND));
                    yamlNamed.fileContentCommand = toText(named.get(FILE_CONTENT_COMMAND));
                    yamlNamed.contentTypeCommand = toText(named.get(CONTENT_TYPE_COMMAND));
                    return yamlNamed;
                }).toList();
    }

    private static Map<String, String> paramsToMap(Map<String, JsonNode> multipart) {
        JsonNode jsonNode = multipart.get(PARAMS);
        return toStream(jsonNode.fields())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().asText()));
    }
}

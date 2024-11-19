package org.springframework.cloud.contract.verifier.converter.resolvers.request.url;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.AbstractResolver;
import org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.RequestJsonPathTraverser;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.Utils.toStream;
import static org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathConstants.JSON_PATH_CONFIGURATION;

public class UrlPathResolver extends AbstractResolver<String> {

    public UrlPathResolver(Oa3Spec spec, String contractId) {
        super(spec, contractId, new RequestJsonPathTraverser(JSON_PATH_CONFIGURATION));
    }

    @Override
    public String resolve() {
        return traverser().findContractsForField(operationNode(), contractId(), CONTRACT_PATH)
                .findFirst()
                .map(JsonNode::asText)
                .orElseGet(this::resolvePath);
    }

    private String resolvePath() {
        if (parameterNode() == null) {
            return path();
        }
        return toStream(parameterNode().iterator())
                .reduce(path(), (acc, currentNode) ->
                        traverser().findContractsForField(currentNode, contractId(), VALUE).findFirst()
                                .map(value -> acc.replace("{" + currentNode.get(NAME).asText() + "}", value.asText()))
                                .orElse(acc), (p1, p2) -> p1);
    }
}

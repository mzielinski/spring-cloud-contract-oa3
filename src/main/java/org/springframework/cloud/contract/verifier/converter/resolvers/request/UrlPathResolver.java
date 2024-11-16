package org.springframework.cloud.contract.verifier.converter.resolvers.request;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.JsonPathTraverser;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;
import static org.springframework.cloud.contract.verifier.converter.Utils.toStream;
import static org.springframework.cloud.contract.verifier.converter.resolvers.JsonPathConstants.JSON_PATH_CONFIGURATION;

public class UrlPathResolver implements Resolver<String> {

    private final JsonPathTraverser traverser = new JsonPathTraverser(JSON_PATH_CONFIGURATION);
    private final Oa3Spec spec;
    private final String contractId;

    public UrlPathResolver(Oa3Spec spec, String contractId) {
        this.spec = spec;
        this.contractId = contractId;
    }

    @Override
    public String resolve() {
        return traverser.findContractsForField(spec.operationNode(), contractId, CONTRACT_PATH)
                .findFirst()
                .map(JsonNode::asText)
                .orElseGet(this::resolvePath);
    }

    private String resolvePath() {
        if (spec.parametersNode() == null) {
            return spec.path();
        }
        return toStream(spec.parametersNode().iterator())
                .reduce(spec.path(), (acc, currentNode) ->
                        traverser.findContractsForField(currentNode, contractId, VALUE).findFirst()
                                .map(value -> acc.replace("{" + currentNode.get(NAME).asText() + "}", value.asText()))
                                .orElse(acc), (p1, p2) -> p1);
    }
}

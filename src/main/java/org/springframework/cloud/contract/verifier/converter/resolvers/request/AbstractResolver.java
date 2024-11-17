package org.springframework.cloud.contract.verifier.converter.resolvers.request;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathTraverser;

import static org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathConstants.JSON_PATH_CONFIGURATION;

public abstract class AbstractResolver<T> {

    private final JsonPathTraverser traverser = new JsonPathTraverser(JSON_PATH_CONFIGURATION);
    private final Oa3Spec spec;
    private final String contractId;

    protected AbstractResolver(Oa3Spec spec, String contractId) {
        this.spec = spec;
        this.contractId = contractId;
    }

    public abstract T resolve();

    protected JsonPathTraverser traverser() {
        return traverser;
    }

    protected String path() {
        return spec.path();
    }

    protected JsonNode parameterNode() {
        return spec.parametersNode();
    }

    protected JsonNode operationNode() {
        return spec.operationNode();
    }

    protected Oa3Spec spec() {
        return spec;
    }

    protected String contractId() {
        return contractId;
    }
}

package org.springframework.cloud.contract.verifier.converter.resolvers;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.cloud.contract.verifier.converter.Oa3Spec;
import org.springframework.cloud.contract.verifier.converter.resolvers.jsonPath.JsonPathTraverser;

public abstract class AbstractResolver<T> {

    private final JsonPathTraverser traverser;
    private final Oa3Spec spec;
    private final String contractId;

    protected AbstractResolver(Oa3Spec spec, String contractId, JsonPathTraverser traverser) {
        this.spec = spec;
        this.contractId = contractId;
        this.traverser = traverser;
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

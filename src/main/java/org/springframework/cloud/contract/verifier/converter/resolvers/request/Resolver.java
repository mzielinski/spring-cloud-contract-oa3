package org.springframework.cloud.contract.verifier.converter.resolvers.request;

public interface Resolver<T> {
    T resolve();
}

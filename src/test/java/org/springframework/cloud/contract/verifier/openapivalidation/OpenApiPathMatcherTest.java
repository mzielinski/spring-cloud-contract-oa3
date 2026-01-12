package org.springframework.cloud.contract.verifier.openapivalidation;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class OpenApiPathMatcherTest {

    @Test
    void should_match_template_path_when_contract_uses_concrete_value() {
        // given
        String contractPath = "/bar/123";
        String specPath = "/bar/{id}";

        // when
        boolean matches = OpenApiPathMatcher.matches(contractPath, specPath);

        // then
        assertThat(matches).isTrue();
    }

    @Test
    void should_reject_path_when_segments_do_not_match() {
        // given
        String contractPath = "/bar/123";
        String specPath = "/foo/{id}";

        // when
        boolean matches = OpenApiPathMatcher.matches(contractPath, specPath);

        // then
        assertThat(matches).isFalse();
    }
}

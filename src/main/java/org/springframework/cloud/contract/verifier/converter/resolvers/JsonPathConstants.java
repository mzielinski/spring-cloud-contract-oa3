package org.springframework.cloud.contract.verifier.converter.resolvers;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.CONTRACT_ID;
import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.IN;

public class JsonPathConstants {

    public static final Configuration JSON_PATH_CONFIGURATION = Configuration.builder()
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .mappingProvider(new JacksonMappingProvider())
            .options(Option.SUPPRESS_EXCEPTIONS)
            .build();

    public final static Function<Object, Filter> CONTRACT_ID_FILTER = buildFilter(CONTRACT_ID);
    public final static Function<Object, Filter> PARAM_IN_FILTER = buildFilter(IN);

    private static Function<Object, Filter> buildFilter(String name) {
        return value -> toFilters(name, value).stream().reduce((currentFilter, acc) -> acc.or(currentFilter)).orElseThrow();
    }

    private static List<Filter> toFilters(String name, Object value) {
        if (value.getClass().isArray()) {
            return Arrays.stream((Object[]) value)
                    .map(val -> Criteria.where(name).is(val))
                    .map(Filter::filter)
                    .toList();
        } else {
            return List.of(Filter.filter(Criteria.where(name).is(value)));
        }
    }
}

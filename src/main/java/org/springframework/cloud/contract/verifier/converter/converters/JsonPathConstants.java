package org.springframework.cloud.contract.verifier.converter.converters;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.Criteria;
import com.jayway.jsonpath.Filter;
import com.jayway.jsonpath.Option;
import com.jayway.jsonpath.spi.json.JacksonJsonNodeJsonProvider;
import com.jayway.jsonpath.spi.mapper.JacksonMappingProvider;

import java.util.function.Function;

import static org.springframework.cloud.contract.verifier.converter.Oa3Spec.*;

public class JsonPathConstants {

    public static final Configuration JSON_PATH_CONFIGURATION = Configuration.builder()
            .jsonProvider(new JacksonJsonNodeJsonProvider())
            .mappingProvider(new JacksonMappingProvider())
            .options(Option.SUPPRESS_EXCEPTIONS)
            .build();

    public final static Function<Object, Filter> CONTRACT_ID_FILTER = buildFilter(CONTRACT_ID);
    public final static Function<Object, Filter> PARAM_IN_FILTER = buildFilter(IN);

    private static Function<Object, Filter> buildFilter(String name) {
        return value -> Filter.filter(Criteria.where(name).is(value));
    }
}

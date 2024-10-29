package org.springframework.cloud.contract.verifier.converter;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.OpenAPIV3Parser;

import java.io.File;

class YamlOa3Parser implements Oa3Parser {

    @Override
    public OpenAPI parseOpenAPI(File file) {
        var spec = new OpenAPIV3Parser().read(file.getPath());
        if (spec == null || spec.getPaths().isEmpty()) {
            throw new IllegalArgumentException("OpenAPI specification %s not found".formatted(file.getPath()));
        }
        return spec;
    }
}

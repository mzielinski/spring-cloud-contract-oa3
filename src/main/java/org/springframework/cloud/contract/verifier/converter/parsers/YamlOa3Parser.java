package org.springframework.cloud.contract.verifier.converter.parsers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class YamlOa3Parser implements Oa3Parser {

    private final YAMLFactory yamlFactory = YAMLFactory.builder().build();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean canParse(File file) {
        return file.getPath().endsWith(".yml") || file.getPath().endsWith(".yaml");
    }

    @Override
    public List<JsonNode> parseOpenAPI(File file) {
        try {
            final var yamlParser = yamlFactory.createParser(file);
            return objectMapper.readValues(yamlParser, new TypeReference<JsonNode>() {
            }).readAll();
        } catch (IOException ex) {
            throw new IllegalArgumentException("OpenAPI specification %s not found".formatted(file.getPath()));
        }
    }
}

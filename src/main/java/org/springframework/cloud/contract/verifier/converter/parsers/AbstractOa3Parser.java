package org.springframework.cloud.contract.verifier.converter.parsers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.List;

abstract class AbstractOa3Parser implements Oa3Parser {

    private final List<String> extensions;
    private final ObjectMapper objectMapper = new ObjectMapper();

    AbstractOa3Parser(List<String> extensions) {
        this.extensions = extensions;
    }

    @Override
    public boolean validExtension(File file) {
        return extensions.stream().anyMatch(ext -> file.getName().endsWith(ext));
    }

    @Override
    public List<JsonNode> parseSpecification(File file) {
        try {
            return objectMapper.readValues(jsonParser(file), new TypeReference<JsonNode>() {
            }).readAll();
        } catch (IOException ex) {
            throw new IllegalArgumentException("OpenAPI specification %s not found".formatted(file.getPath()));
        }
    }

    abstract JsonParser jsonParser(File file) throws IOException;
}

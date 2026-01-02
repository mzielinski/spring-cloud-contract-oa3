package org.springframework.cloud.contract.verifier.converter.parsers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class YamlOa3Parser extends AbstractOa3Parser {

    private final YAMLFactory yamlFactory = YAMLFactory.builder().build();

    public YamlOa3Parser() {
        super(List.of("yaml", "yml"));
    }

    @Override
    JsonParser jsonParser(File file) throws IOException {
        return yamlFactory.createParser(file);
    }
}

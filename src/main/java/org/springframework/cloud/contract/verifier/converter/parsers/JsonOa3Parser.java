package org.springframework.cloud.contract.verifier.converter.parsers;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class JsonOa3Parser extends AbstractOa3Parser {

    private final JsonFactory yamlFactory = JsonFactory.builder().build();

    public JsonOa3Parser() {
        super(List.of("json"));
    }

    @Override
    JsonParser jsonParser(File file) throws IOException {
        return yamlFactory.createParser(file);
    }
}

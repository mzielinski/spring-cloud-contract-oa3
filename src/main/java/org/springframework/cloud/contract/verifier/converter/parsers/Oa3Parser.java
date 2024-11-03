package org.springframework.cloud.contract.verifier.converter.parsers;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.util.List;

public interface Oa3Parser {
    boolean canParse(File file);
    List<JsonNode> parseOpenAPI(File file);
}

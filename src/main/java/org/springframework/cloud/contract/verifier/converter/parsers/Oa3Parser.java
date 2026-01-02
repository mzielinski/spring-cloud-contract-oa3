package org.springframework.cloud.contract.verifier.converter.parsers;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.File;
import java.util.List;

public interface Oa3Parser {
    boolean validExtension(File file);

    List<JsonNode> parseSpecification(File file);
}

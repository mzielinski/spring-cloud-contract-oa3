package org.springframework.cloud.contract.verifier.converter.parsers;

import java.io.File;
import java.util.Optional;

public class Oa3ParserStrategy {

    private final Oa3Parser jsonOa3Parser = new JsonOa3Parser();
    private final Oa3Parser yamlOa3Parser = new YamlOa3Parser();

    public Optional<Oa3Parser> resolve(File file) {
        if (jsonOa3Parser.validExtension(file)) {
            return Optional.of(jsonOa3Parser);
        } else if (yamlOa3Parser.validExtension(file)) {
            return Optional.of(yamlOa3Parser);
        }
        return Optional.empty();
    }
}

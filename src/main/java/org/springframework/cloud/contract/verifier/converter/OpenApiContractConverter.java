package org.springframework.cloud.contract.verifier.converter;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.models.PathItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.contract.spec.Contract;
import org.springframework.cloud.contract.spec.ContractConverter;
import org.springframework.cloud.contract.verifier.converter.parsers.Oa3Parser;
import org.springframework.cloud.contract.verifier.converter.parsers.YamlOa3Parser;

import java.io.File;
import java.util.Collection;
import java.util.List;

public class OpenApiContractConverter implements ContractConverter<Collection<PathItem>> {

    private static final Logger log = LoggerFactory.getLogger(OpenApiContractConverter.class);

    private final TempYamlToContracts tempYamlToContracts = new TempYamlToContracts();
    private final Oa3ToScc oa3ToScc = new Oa3ToScc();
    private final Oa3Parser oa3Parser = new YamlOa3Parser();

    @Override
    public boolean isAccepted(File file) {
        try {
            if (oa3Parser.canParse(file)) {
                return !convertFrom(file).isEmpty();
            }
        } catch (Exception e) {
            log.error("Error reading OpenAPI specification", e);
        }
        return false;
    }

    @Override
    public Collection<Contract> convertFrom(File file) {
        try {
            List<JsonNode> nodes = oa3Parser.parseOpenAPI(file);
            return oa3ToScc.convert(nodes)
                    .map(tempYamlToContracts::convertFromYaml)
                    .flatMap(Collection::stream)
                    .toList();
        } catch (Exception e) {
            log.error("Error converting OpenAPI file {} to contracts", file.getAbsolutePath(), e);
            return List.of();
        }
    }

    @Override
    public Collection<PathItem> convertTo(Collection<Contract> pathItems) {
        throw new UnsupportedOperationException("Cannot convert contracts into oa3");
    }
}

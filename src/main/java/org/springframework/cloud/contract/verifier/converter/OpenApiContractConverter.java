package org.springframework.cloud.contract.verifier.converter;

import io.swagger.v3.oas.models.PathItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.contract.spec.Contract;
import org.springframework.cloud.contract.spec.ContractConverter;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class OpenApiContractConverter implements ContractConverter<Collection<PathItem>> {

    private static final Logger log = LoggerFactory.getLogger(OpenApiContractConverter.class);

    private final TempYamlToContracts tempYamlToContracts = new TempYamlToContracts();
    private final Oa3ToScc oa3ToScc = new Oa3ToScc();
    private final Oa3Parser oa3Parser = new Oa3Parser();

    @Override
    public boolean isAccepted(File file) {
        try {
            return oa3ToScc.convert(oa3Parser.parseOpenAPI(file))
                    .anyMatch(Objects::nonNull);
        } catch (Exception e) {
            log.error("Error reading OpenAPI specification", e);
        }
        return false;
    }

    @Override
    public Collection<Contract> convertFrom(File file) {
        try {
            return oa3ToScc.convert(oa3Parser.parseOpenAPI(file))
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

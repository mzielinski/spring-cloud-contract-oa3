package org.springframework.cloud.contract.verifier.converter;

import io.swagger.v3.oas.models.OpenAPI;

import java.io.File;

interface Oa3Parser {

    OpenAPI parseOpenAPI(File file);
}

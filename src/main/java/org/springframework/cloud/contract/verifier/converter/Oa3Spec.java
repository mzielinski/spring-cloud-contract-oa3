package org.springframework.cloud.contract.verifier.converter;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;
import java.util.stream.Stream;

import static org.springframework.cloud.contract.verifier.converter.Utils.*;

public record Oa3Spec(String path, String httpMethod, JsonNode operationNode, JsonNode parametersNode) {

    public static final String BODY = "body";
    public static final String BODY_FROM_FILE = "bodyFromFile";
    public static final String BODY_FROM_FILE_AS_BYTES = "bodyFromFileAsBytes";
    public static final String COMMAND = "command";
    public static final String CONTENT = "content";
    public static final String CONTENT_TYPE = "contentType";
    public static final String CONTENT_TYPE_COMMAND = "contentTypeCommand";
    public static final String CONTENT_TYPE_HTTP_HEADER = "Content-Type";
    public static final String CONTRACT_ID = "contractId";
    public static final String CONTRACT_PATH = "contractPath";
    public static final String COOKIE = "cookie";
    public static final String COOKIES = "cookies";
    public static final String DESCRIPTION = "description";
    public static final String FILE_CONTENT = "fileContent";
    public static final String FILE_CONTENT_AS_BYTES = "fileContentAsBytes";
    public static final String FILE_CONTENT_COMMAND = "fileContentCommand";
    public static final String FILE_CONTENT_FROM_FILE_AS_BYTES = "fileContentFromFileAsBytes";
    public static final String FILE_NAME = "fileName";
    public static final String FILE_NAME_COMMAND = "fileNameCommand";
    public static final String HEADER = "header";
    public static final String HEADERS = "headers";
    public static final String IGNORED = "ignored";
    public static final String IN = "in";
    public static final String KEY = "key";
    public static final String LABEL = "label";
    public static final String MATCHERS = "matchers";
    public static final String MAX_OCCURRENCE = "maxOccurrence";
    public static final String MIN_OCCURRENCE = "minOccurrence";
    public static final String MULTIPART = "multipart";
    public static final String NAME = "name";
    public static final String NAMED = "named";
    public static final String PARAM_NAME = "paramName";
    public static final String PARAMS = "params";
    public static final String PARAMETERS = "parameters";
    public static final String PATH = "path";
    public static final String PATHS = "paths";
    public static final String PREDEFINED = "predefined";
    public static final String PRIORITY = "priority";
    public static final String QUERY = "query";
    public static final String QUERY_PARAMETERS = "queryParameters";
    public static final String REGEX = "regex";
    public static final String REGEX_TYPE = "regexType";
    public static final String REQUEST = "request";
    public static final String REQUEST_BODY = "requestBody";
    public static final String RESPONSES = "responses";
    public static final String SERVICE_NAME = "serviceName";
    public static final String TYPE = "type";
    public static final String URL = "url";
    public static final String VALUE = "value";
    public static final String X_CONTRACTS = "x-contracts";
    public static final String[] OPENAPI_OPERATIONS = new String[]{"get", "put", "head", "post", "delete", "patch", "options", "trace"};


    @Deprecated
    public static Optional<String> getContentType(JsonNode root) {
        return Optional.ofNullable(root)
                .map(node -> node.get(CONTENT))
                .flatMap(node -> toStream(node.fieldNames()).findFirst());
    }

    @Deprecated
    public static Stream<JsonNode> xContracts(JsonNode node, String nodeName) {
        return findSubNodes(node, nodeName, X_CONTRACTS);
    }

    @Deprecated
    public static Optional<JsonNode> findContract(JsonNode parentNode, String nodeName, String contractId) {
        return xContracts(parentNode, nodeName)
                .filter(contract -> contractId.equalsIgnoreCase(toText(contract.get(CONTRACT_ID))))
                .findAny();
    }
}

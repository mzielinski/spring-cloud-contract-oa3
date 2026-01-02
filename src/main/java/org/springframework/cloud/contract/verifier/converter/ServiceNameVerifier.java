package org.springframework.cloud.contract.verifier.converter;

import com.fasterxml.jackson.databind.JsonNode;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.cloud.contract.verifier.converter.Utils.toText;

class ServiceNameVerifier {

    static final String SERVICE_NAME_KEY = "scc.enabled.service-names";

    boolean checkServiceEnabled(JsonNode jsonNode) {
        return checkServiceEnabled(toText(jsonNode));
    }

    boolean checkServiceEnabled(String serviceName) {
        if (isBlank(serviceName) || isBlank(System.getProperty(SERVICE_NAME_KEY))) {
            return true;
        }
        return Arrays.stream(StringUtils.split(System.getProperty(SERVICE_NAME_KEY), ","))
                .map(StringUtils::trim)
                .toList().contains(serviceName);
    }
}

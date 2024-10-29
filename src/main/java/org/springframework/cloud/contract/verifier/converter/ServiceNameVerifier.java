package org.springframework.cloud.contract.verifier.converter;

import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;

class ServiceNameVerifier {

    static final String SERVICE_NAME_KEY = "scc.enabled.service-names";

    boolean checkServiceEnabled(String serviceName) {
        if (StringUtils.isBlank(serviceName) || StringUtils.isBlank(System.getProperty(SERVICE_NAME_KEY))) {
            return true;
        }
        return Arrays.stream(StringUtils.split(System.getProperty(SERVICE_NAME_KEY), ","))
                .map(StringUtils::trim)
                .toList().contains(serviceName);
    }
}

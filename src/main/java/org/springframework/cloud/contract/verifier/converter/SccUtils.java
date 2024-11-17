package org.springframework.cloud.contract.verifier.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

public class SccUtils {

    private static final Logger log = LoggerFactory.getLogger(SccUtils.class);

    private SccUtils() {
        throw new AssertionError("Utility class");
    }

}

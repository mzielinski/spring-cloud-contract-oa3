package org.springframework.cloud.contract.verifier.converter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

class SccUtils {

    private static final Logger log = LoggerFactory.getLogger(SccUtils.class);

    private SccUtils() {
        throw new AssertionError("Utility class");
    }

    static YamlContract.MatchingType createMatchingType(String val) {
        return tryToParse(val, YamlContract.MatchingType::valueOf);
    }

    static YamlContract.PredefinedRegex createPredefinedRegex(String val) {
        return tryToParse(val, YamlContract.PredefinedRegex::valueOf);
    }

    static YamlContract.RegexType createRegexType(String val) {
        return tryToParse(val, YamlContract.RegexType::valueOf);
    }

    private static <T> T tryToParse(String val, Function<String, T> function) {
        if (isNotBlank(val)) {
            try {
                return function.apply(val);
            } catch (Exception e) {
                log.error("Error parsing value {}", val, e);
            }
        }
        return null;
    }
}

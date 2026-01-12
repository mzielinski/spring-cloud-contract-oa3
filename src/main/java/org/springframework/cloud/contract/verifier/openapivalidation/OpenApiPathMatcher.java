package org.springframework.cloud.contract.verifier.openapivalidation;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

class OpenApiPathMatcher {

    private OpenApiPathMatcher() {
        throw new AssertionError("Utility class");
    }

    static boolean matches(String contractPath, String specPath) {
        if (contractPath == null || specPath == null) {
            return false;
        }
        String normalizedContract = normalize(contractPath);
        String normalizedSpec = normalize(specPath);
        List<String> contractSegments = splitSegments(normalizedContract);
        List<String> specSegments = splitSegments(normalizedSpec);
        if (contractSegments.size() != specSegments.size()) {
            return false;
        }
        for (int i = 0; i < contractSegments.size(); i++) {
            String contractSegment = contractSegments.get(i);
            String specSegment = specSegments.get(i);
            if (isTemplate(specSegment) || isTemplate(contractSegment)) {
                continue;
            }
            if (!specSegment.equals(contractSegment)) {
                return false;
            }
        }
        return true;
    }

    private static String normalize(String path) {
        String normalized = path;
        int queryIndex = normalized.indexOf('?');
        if (queryIndex >= 0) {
            normalized = normalized.substring(0, queryIndex);
        }
        if (normalized.contains("://")) {
            try {
                URI uri = URI.create(normalized);
                if (uri.getPath() != null) {
                    normalized = uri.getPath();
                }
            } catch (Exception ignored) {
                // ignore invalid URI, fall back to raw path
            }
        }
        if (normalized.isBlank()) {
            return "/";
        }
        if (normalized.length() > 1 && normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private static List<String> splitSegments(String path) {
        String trimmed = path.startsWith("/") ? path.substring(1) : path;
        if (trimmed.isBlank()) {
            return List.of();
        }
        return Arrays.asList(trimmed.split("/"));
    }

    private static boolean isTemplate(String segment) {
        return segment.startsWith("{") && segment.endsWith("}");
    }
}

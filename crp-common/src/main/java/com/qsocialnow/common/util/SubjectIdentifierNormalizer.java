package com.qsocialnow.common.util;

import org.apache.commons.lang3.StringUtils;

public class SubjectIdentifierNormalizer {

    public static String normalize(String identifier) {
        if (StringUtils.isNotBlank(identifier)) {
            String[] identifierTokens = identifier.split("\\s+http");
            if (identifierTokens.length > 0) {
                return identifierTokens[0];
            }
        }
        return identifier;
    }
}

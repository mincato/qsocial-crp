package com.qsocialnow.rest.security;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class AllowedOriginsHelper {

    @Resource(name = "accessControlAllowHeaders")
    private Map<String, String> headers;

    public Map<String, String> getCustomAllowedOrigin(String clientOrigin) {

        String allowedOrigins = headers.get(SecurityConstants.ALLOW_ORIGINS_KEY);

        Map<String, String> result = new HashMap<>();

        if (isAllowed(clientOrigin, allowedOrigins)) {
            result.putAll(headers);
            result.remove(SecurityConstants.ALLOW_ORIGINS_KEY);
            result.put(SecurityConstants.ALLOW_ORIGIN_KEY, clientOrigin);
        }

        return result;
    }

    private boolean isAllowed(String clientOrigin, String allowedOrigins) {
        return StringUtils.isNotBlank(clientOrigin) && StringUtils.isNotBlank(allowedOrigins)
                && isAllowedOrigin(clientOrigin, allowedOrigins);
    }

    private boolean isAllowedOrigin(String clientOrigin, String allowedOrigins) {
        String[] allowed = allowedOrigins.split(",");
        for (String origin : allowed) {
            if (origin.trim().equals(clientOrigin)) {
                return true;
            }
        }
        return false;
    }

}

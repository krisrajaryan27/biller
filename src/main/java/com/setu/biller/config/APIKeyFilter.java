package com.setu.biller.config;

import lombok.AllArgsConstructor;
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Krishna Verma
 * @date 23/07/2020
 */
@AllArgsConstructor
public class APIKeyFilter extends AbstractPreAuthenticatedProcessingFilter {
    private final String requestHeader;

    @Override
    protected Object getPreAuthenticatedPrincipal(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader(requestHeader);
    }

    @Override
    protected Object getPreAuthenticatedCredentials(HttpServletRequest httpServletRequest) {
        return null;
    }
}

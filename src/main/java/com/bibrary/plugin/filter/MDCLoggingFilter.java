package com.bibrary.plugin.filter;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

@Component
@Order(1)
public class MDCLoggingFilter extends OncePerRequestFilter {

    public static final String REQUEST_ID = "request-id";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {
        var requestId = getRequestId(request);

        try {
            MDC.put(REQUEST_ID, requestId);
            filterChain.doFilter(request, response);
        } finally {
            MDC.remove(REQUEST_ID);
        }
    }

    private String getRequestId(final HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(REQUEST_ID))
                .orElse(UUID.randomUUID().toString());
    }
}

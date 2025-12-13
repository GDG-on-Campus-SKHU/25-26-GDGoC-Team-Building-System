package com.skhu.gdgocteambuildingproject.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtFilter extends GenericFilterBean {

    private final TokenProvider tokenProvider;

    private static final List<String> EXCLUDE_PATH_PREFIXES = List.of(
            "/auth/signup",
            "/auth/login",
            "/auth/refresh",

            "/email/",
            "/swagger-ui",
            "/v3/api-docs"
    );

    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain
    ) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String path = httpRequest.getRequestURI();

        if (isExcluded(path)) {
            chain.doFilter(request, response);
            return;
        }

        String token = tokenProvider.resolveToken(httpRequest);

        if (!StringUtils.hasText(token)) {
            chain.doFilter(request, response);
            return;
        }

        tokenProvider.validateToken(token);

        if (!tokenProvider.isAccessToken(token)) {
            throw new io.jsonwebtoken.JwtException("ACCESS_TOKEN_REQUIRED");
        }

        Authentication authentication = tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    private boolean isExcluded(String path) {
        return EXCLUDE_PATH_PREFIXES.stream()
                .anyMatch(path::startsWith);
    }
}

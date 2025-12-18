package com.skhu.gdgocteambuildingproject.global.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.List;

@Slf4j
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
        String method = httpRequest.getMethod();

        if (method.equals("GET") && path.matches("^/project-gallery/\\d+$")) {
            chain.doFilter(request, response);
            return;
        }

        if (isExcluded(path)) {
            chain.doFilter(request, response);
            return;
        }

        String token = tokenProvider.resolveToken(httpRequest);

        if (!StringUtils.hasText(token)) {
            chain.doFilter(request, response);
            return;
        }

        try {
            tokenProvider.validateToken(token);

            if (!tokenProvider.isAccessToken(token)) {
                log.warn("[JWT FILTER] Invalid token type - ACCESS_TOKEN_REQUIRED");
                chain.doFilter(request, response);
                return;
            }

            Authentication authentication = tokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (Exception e) {
            // 인증을 설정하지 않고 필터 체인을 계속 진행
            log.warn("[JWT FILTER] Invalid JWT token", e);
        }

        chain.doFilter(request, response);
    }

    private boolean isExcluded(String path) {
        return EXCLUDE_PATH_PREFIXES.stream()
                .anyMatch(path::startsWith);
    }
}

package com.skhu.gdgocteambuildingproject.global.jwt;

import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.global.jwt.service.CustomUserDetailsService;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;

@Slf4j
@Component
public class TokenProvider {
    private static final String ROLE_CLAIM = "Role";
    private static final int REFRESH_TOKEN_MULTIPLIER = 7;
    private final Key key;
    private final long accessTokenValidityTime;
    private final CustomUserDetailsService customUserDetailsService;
    private static final String TOKEN_TYPE_CLAIM = "tokenType";
    private static final String ACCESS_TOKEN = "ACCESS";
    private static final String REFRESH_TOKEN = "REFRESH";

    public TokenProvider(
            @Value("${jwt.secret}") String secretKey,
            @Value("${jwt.access.expiration}") long accessTokenValidityTime,
            CustomUserDetailsService customUserDetailsService
    ) {
        byte[] keyBytes = secretKey.getBytes(java.nio.charset.StandardCharsets.UTF_8);
        this.key = Keys.hmacShaKeyFor(keyBytes);
        this.accessTokenValidityTime = accessTokenValidityTime;
        this.customUserDetailsService = customUserDetailsService;
    }

    public String createAccessToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidityTime);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim(ROLE_CLAIM, user.getRole().name())
                .claim(TOKEN_TYPE_CLAIM, ACCESS_TOKEN)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + accessTokenValidityTime * REFRESH_TOKEN_MULTIPLIER);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim(TOKEN_TYPE_CLAIM, REFRESH_TOKEN)
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        if (!isAccessToken(claims)) {
            throw new JwtException("ACCESS_TOKEN_REQUIRED");
        }

        UserDetails userDetails =
                customUserDetailsService.loadUserByUsername(claims.getSubject());

        return new UsernamePasswordAuthenticationToken(
                userDetails,
                null,
                userDetails.getAuthorities()
        );
    }

    public String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    public void validateToken(String token) {
        try {
            parseClaims(token);
        } catch (Exception e) {
            log.error(ExceptionMessage.INVALID_JWT_TOKEN.getMessage(), e);
            throw new JwtException(ExceptionMessage.INVALID_JWT_TOKEN.getMessage());
        }
    }

    public void validateJwtFormat(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
        } catch (Exception e) {
            throw new IllegalArgumentException(
                    ExceptionMessage.INVALID_JWT_TOKEN.getMessage()
            );
        }
    }

    public boolean isAccessToken(String token) {
        return isAccessToken(parseClaims(token));
    }

    public boolean isRefreshToken(String token) {
        return isRefreshToken(parseClaims(token));
    }

    private boolean isAccessToken(Claims claims) {
        return ACCESS_TOKEN.equals(claims.get(TOKEN_TYPE_CLAIM));
    }

    private boolean isRefreshToken(Claims claims) {
        return REFRESH_TOKEN.equals(claims.get(TOKEN_TYPE_CLAIM));
    }

    private Claims parseClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public long getRefreshTokenExpirySeconds() {
        return (accessTokenValidityTime * REFRESH_TOKEN_MULTIPLIER) / 1000;
    }
}

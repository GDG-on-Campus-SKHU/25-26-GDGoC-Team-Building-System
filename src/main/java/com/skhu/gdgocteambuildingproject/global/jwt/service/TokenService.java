package com.skhu.gdgocteambuildingproject.global.jwt.service;

import com.skhu.gdgocteambuildingproject.auth.domain.RefreshToken;
import com.skhu.gdgocteambuildingproject.auth.repository.RefreshTokenRepository;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.global.jwt.TokenProvider;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenProvider tokenProvider;

    public String createAccessToken(User user) {
        return tokenProvider.createAccessToken(user);
    }

    public String createRefreshToken(User user) {
        return tokenProvider.createRefreshToken(user);
    }

    @Transactional
    public void store(User user, String token) {
        LocalDateTime expiredAt =
                LocalDateTime.now()
                        .plusSeconds(tokenProvider.getRefreshTokenExpirySeconds());

        refreshTokenRepository.save(
                RefreshToken.create(user, token, expiredAt)
        );
    }

    @Transactional(readOnly = true)
    public RefreshToken validate(String token) {
        try {
            tokenProvider.validateJwtFormat(token);

            if (!tokenProvider.isRefreshToken(token)) {
                throw new IllegalArgumentException();
            }

            return refreshTokenRepository.findByToken(token)
                    .orElseThrow(IllegalArgumentException::new);

        } catch (Exception e) {
            throw new IllegalArgumentException(
                    ExceptionMessage.REFRESH_TOKEN_INVALID.getMessage()
            );
        }
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public long getRefreshTokenExpirySeconds() {
        return tokenProvider.getRefreshTokenExpirySeconds();
    }
}

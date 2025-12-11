package com.skhu.gdgocteambuildingproject.global.jwt.service;

import com.skhu.gdgocteambuildingproject.auth.domain.RefreshToken;
import com.skhu.gdgocteambuildingproject.auth.repository.RefreshTokenRepository;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.global.jwt.TokenProvider;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public RefreshToken store(User user, String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseGet(() ->
                        refreshTokenRepository.save(RefreshToken.of(user, token))
                );
    }

    @Transactional(readOnly = true)
    public RefreshToken validate(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new IllegalArgumentException(ExceptionMessage.REFRESH_TOKEN_INVALID.getMessage())
                );
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public void deleteAllByUser(User user) {
        refreshTokenRepository.deleteAllByUser(user);
    }

    public long getRefreshTokenExpirySeconds() {
        return tokenProvider.getRefreshTokenExpirySeconds();
    }
}

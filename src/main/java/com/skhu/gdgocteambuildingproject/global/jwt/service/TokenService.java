package com.skhu.gdgocteambuildingproject.global.jwt.service;

import com.skhu.gdgocteambuildingproject.auth.domain.RefreshToken;
import com.skhu.gdgocteambuildingproject.auth.repository.RefreshTokenRepository;
import com.skhu.gdgocteambuildingproject.global.jwt.TokenProvider;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public RefreshToken store(User user, String token) {
        return refreshTokenRepository.save(RefreshToken.of(user, token));
    }

    public RefreshToken validate(String token) {
        return refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid refresh token"));
    }

    public String rotate(RefreshToken oldToken) {
        User user = oldToken.getUser();
        refreshTokenRepository.delete(oldToken);

        String newRefresh = createRefreshToken(user);
        store(user, newRefresh);

        return newRefresh;
    }

    public void deleteByToken(String token) {
        refreshTokenRepository.deleteByToken(token);
    }

    public void deleteAllByUser(User user) {
        refreshTokenRepository.deleteAllByUser(user);
    }
}

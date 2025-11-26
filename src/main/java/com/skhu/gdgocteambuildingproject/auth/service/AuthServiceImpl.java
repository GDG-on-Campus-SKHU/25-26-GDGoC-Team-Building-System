package com.skhu.gdgocteambuildingproject.auth.service;

import com.skhu.gdgocteambuildingproject.auth.domain.RefreshToken;
import com.skhu.gdgocteambuildingproject.auth.dto.request.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.RefreshTokenRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.response.LoginResponseDto;
import com.skhu.gdgocteambuildingproject.auth.repository.RefreshTokenRepository;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.global.jwt.TokenProvider;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Override
    @Transactional
    public LoginResponseDto signUp(SignUpRequestDto dto) {

        if (userRepository.existsByEmailAndDeletedFalse(dto.getEmail())) {
            throw new RuntimeException(ExceptionMessage.EMAIL_ALREADY_EXISTS.getMessage());
        }

        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new RuntimeException(ExceptionMessage.INVALID_PASSWORD.getMessage());
        }

        User user = dto.toEntity(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);

        return createLoginResponse(user);
    }

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto dto) {

        User user = userRepository.findByEmailAndDeletedFalse(dto.getEmail())
                .orElseThrow(() -> new RuntimeException(ExceptionMessage.USER_NOT_FOUND.getMessage()));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new RuntimeException(ExceptionMessage.INVALID_PASSWORD.getMessage());
        }

        validateUserStatus(user);

        return createLoginResponse(user);
    }

    @Override
    @Transactional
    public LoginResponseDto refresh(RefreshTokenRequestDto dto) {

        RefreshToken existing = refreshTokenRepository.findByToken(dto.getRefreshToken())
                .orElseThrow(() -> new RuntimeException(ExceptionMessage.REFRESH_TOKEN_INVALID.getMessage()));

        User user = existing.getUser();
        validateUserStatus(user);

        refreshTokenRepository.delete(existing);

        return createLoginResponse(user);
    }

    @Override
    @Transactional
    public void logout(RefreshTokenRequestDto dto) {
        refreshTokenRepository.deleteByToken(dto.getRefreshToken());
    }

    @Override
    @Transactional
    public void delete(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException(ExceptionMessage.USER_NOT_EXIST.getMessage()));

        refreshTokenRepository.deleteAllByUser(user);
        user.softDelete();
    }

    private void validateUserStatus(User user) {

        if (user.isDeleted()) {
            throw new RuntimeException(ExceptionMessage.USER_NOT_EXIST.getMessage());
        }

        if (user.getApprovalStatus() == ApprovalStatus.WAITING) {
            throw new RuntimeException(ExceptionMessage.USER_NOT_APPROVED.getMessage());
        }
    }

    private LoginResponseDto createLoginResponse(User user) {

        String accessToken = tokenProvider.createAccessToken(user);
        String refreshToken = tokenProvider.createRefreshToken(user);

        refreshTokenRepository.save(
                RefreshToken.builder()
                        .user(user)
                        .token(refreshToken)
                        .build()
        );

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }
}

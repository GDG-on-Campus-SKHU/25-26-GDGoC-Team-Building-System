package com.skhu.gdgocteambuildingproject.auth.service;

import com.skhu.gdgocteambuildingproject.auth.cookie.RefreshTokenCookieWriter;
import com.skhu.gdgocteambuildingproject.auth.domain.RefreshToken;
import com.skhu.gdgocteambuildingproject.auth.dto.request.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.response.LoginResponseDto;
import com.skhu.gdgocteambuildingproject.auth.repository.RefreshTokenRepository;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.global.jwt.TokenProvider;
import com.skhu.gdgocteambuildingproject.global.jwt.service.TokenService;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.UserGeneration;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserStatus;
import com.skhu.gdgocteambuildingproject.user.repository.UserGenerationRepository;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserGenerationRepository userGenerationRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final RefreshTokenCookieWriter cookieWriter;
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    @Transactional
    public LoginResponseDto signUp(SignUpRequestDto dto, HttpServletResponse response) {
        validateSignUp(dto);

        User user = createUser(dto);
        createMainGeneration(user, dto);

        return issueTokens(user, response);
    }

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto dto, HttpServletResponse response) {
        User user = getUser(dto.getEmail());

        validatePassword(dto.getPassword(), user.getPassword());
        validateUserStatus(user);

        return issueTokens(user, response);
    }

    @Override
    @Transactional
    public LoginResponseDto refresh(String refreshToken, HttpServletResponse response) {
        if (refreshToken == null) {
            throw new IllegalArgumentException(
                    ExceptionMessage.REFRESH_TOKEN_INVALID.getMessage()
            );
        }

        if (!tokenProvider.isRefreshToken(refreshToken)) {
            throw new IllegalArgumentException(
                    ExceptionMessage.REFRESH_TOKEN_INVALID.getMessage()
            );
        }

        RefreshToken storedToken = tokenService.validate(refreshToken);
        User user = storedToken.getUser();

        rotateRefreshToken(user, refreshToken, response);

        return LoginResponseDto.builder()
                .accessToken(tokenService.createAccessToken(user))
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }

    @Override
    @Transactional
    public void logout(String refreshToken, HttpServletResponse response) {
        if (StringUtils.hasText(refreshToken)) {
            tokenProvider.validateJwtFormat(refreshToken);
            if (tokenProvider.isRefreshToken(refreshToken)) {
                tokenService.deleteByToken(refreshToken);
            }
        }
        cookieWriter.clear(response);
    }


    @Override
    @Transactional
    public void delete(Long userId, HttpServletResponse response) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                ExceptionMessage.USER_NOT_EXIST.getMessage()
                        )
                );

        if (user.isDeleted()) {
            throw new IllegalStateException(
                    ExceptionMessage.USER_NOT_EXIST.getMessage()
            );
        }
        refreshTokenRepository.deleteAllByUser(user);
        user.softDelete();
        cookieWriter.clear(response);
    }

    private LoginResponseDto issueTokens(User user, HttpServletResponse response) {
        String accessToken = tokenService.createAccessToken(user);
        String refreshToken = tokenService.createRefreshToken(user);

        tokenService.store(user, refreshToken);
        cookieWriter.write(response, refreshToken);

        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }

    private void rotateRefreshToken(User user, String oldRefreshToken, HttpServletResponse response) {
        tokenService.deleteByToken(oldRefreshToken);

        String newRefreshToken = tokenService.createRefreshToken(user);
        tokenService.store(user, newRefreshToken);

        cookieWriter.write(response, newRefreshToken);
    }

    private void validateSignUp(SignUpRequestDto dto) {
        if (dto.getRole() == UserRole.ROLE_SKHU_ADMIN) {
            throw new IllegalArgumentException(
                    ExceptionMessage.INVALID_SIGNUP_ROLE.getMessage()
            );
        }

        if (userRepository.existsByEmailAndDeletedFalse(dto.getEmail())) {
            throw new IllegalArgumentException(
                    ExceptionMessage.EMAIL_ALREADY_EXISTS.getMessage()
            );
        }

        if (userRepository.existsByNumberAndDeletedFalse(dto.getNumber())) {
            throw new IllegalArgumentException(
                    ExceptionMessage.PHONE_ALREADY_EXISTS.getMessage()
            );
        }

        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new IllegalArgumentException(
                    ExceptionMessage.INVALID_PASSWORD.getMessage()
            );
        }
    }

    private User createUser(SignUpRequestDto dto) {
        return userRepository.save(
                User.builder()
                        .email(dto.getEmail())
                        .password(passwordEncoder.encode(dto.getPassword()))
                        .name(dto.getName())
                        .number(dto.getNumber())
                        .school(dto.getSchool())
                        .role(dto.getRole())
                        .part(dto.getPart())
                        .build()
        );
    }

    private void createMainGeneration(User user, SignUpRequestDto dto) {
        UserGeneration userGeneration = UserGeneration.builder()
                .user(user)
                .position(dto.getPosition())
                .generation(Generation.fromLabel(dto.getGeneration()))
                .isMain(true)
                .build();

        user.addGeneration(userGeneration);
        userGenerationRepository.save(userGeneration);
    }

    private User getUser(String email) {
        return userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() ->
                        new EntityNotFoundException(ExceptionMessage.USER_NOT_FOUND.getMessage())
                );
    }

    private void validatePassword(String raw, String encoded) {
        if (!passwordEncoder.matches(raw, encoded)) {
            throw new IllegalArgumentException(
                    ExceptionMessage.INVALID_PASSWORD.getMessage()
            );
        }
    }

    private void validateUserStatus(User user) {
        if (user.isDeleted()) {
            throw new IllegalStateException(
                    ExceptionMessage.USER_NOT_EXIST.getMessage()
            );
        }

        if (user.getUserStatus() == UserStatus.BANNED) {
            throw new LockedException(
                    ExceptionMessage.BANNED_USER.getMessage()
            );
        }

        if (user.getApprovalStatus() == ApprovalStatus.WAITING) {
            throw new IllegalStateException(
                    ExceptionMessage.USER_NOT_APPROVED.getMessage()
            );
        }

        if (user.getApprovalStatus() == ApprovalStatus.REJECTED) {
            throw new IllegalStateException(
                    ExceptionMessage.USER_REJECTED.getMessage()
            );
        }
    }
}

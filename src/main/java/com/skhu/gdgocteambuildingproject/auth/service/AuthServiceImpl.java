package com.skhu.gdgocteambuildingproject.auth.service;

import com.skhu.gdgocteambuildingproject.auth.cookie.RefreshTokenCookieWriter;
import com.skhu.gdgocteambuildingproject.auth.domain.RefreshToken;
import com.skhu.gdgocteambuildingproject.auth.dto.request.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.token.AuthTokenBundle;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.global.jwt.service.TokenService;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.UserGeneration;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
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

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserGenerationRepository userGenerationRepository;
    private final RefreshTokenCookieWriter cookieWriter;

    @Override
    @Transactional
    public AuthTokenBundle signUp(SignUpRequestDto dto, HttpServletResponse response) {
        validateSignUp(dto);
        User user = createUser(dto);
        createMainGeneration(user, dto);
        return issueTokens(user, response);
    }

    @Override
    @Transactional
    public AuthTokenBundle login(LoginRequestDto dto, HttpServletResponse response) {
        User user = getUser(dto.getEmail());
        validatePassword(dto.getPassword(), user.getPassword());
        validateUserStatus(user);
        return issueTokens(user, response);
    }

    @Override
    @Transactional
    public AuthTokenBundle refresh(String refreshToken, HttpServletResponse response) {
        RefreshToken stored = validateRefreshToken(refreshToken);
        User user = stored.getUser();
        validateUserStatus(user);

        String newAccess = tokenService.createAccessToken(user);

        return AuthTokenBundle.of(newAccess, refreshToken, user);
    }

    @Override
    @Transactional
    public void logout(String refreshToken, HttpServletResponse response) {
        if (refreshToken != null) tokenService.deleteByToken(refreshToken);
        cookieWriter.clear(response);
    }

    @Override
    @Transactional
    public void delete(Long userId, HttpServletResponse response) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.USER_NOT_EXIST.getMessage()));

        tokenService.deleteAllByUser(user);
        user.softDelete();
        cookieWriter.clear(response);
    }

    private AuthTokenBundle issueTokens(User user, HttpServletResponse response) {
        AuthTokenBundle bundle = generateTokens(user);
        cookieWriter.write(response, bundle.getRefreshToken());
        return bundle;
    }

    private AuthTokenBundle generateTokens(User user) {
        String access = tokenService.createAccessToken(user);
        String refresh = tokenService.createRefreshToken(user);
        tokenService.store(user, refresh);
        return AuthTokenBundle.of(access, refresh, user);
    }

    private void validateSignUp(SignUpRequestDto dto) {
        if (userRepository.existsByEmailAndDeletedFalse(dto.getEmail()))
            throw new IllegalArgumentException(ExceptionMessage.EMAIL_ALREADY_EXISTS.getMessage());

        if (!dto.getPassword().equals(dto.getPasswordConfirm()))
            throw new IllegalArgumentException(ExceptionMessage.INVALID_PASSWORD.getMessage());
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
        UserGeneration ug = UserGeneration.builder()
                .user(user)
                .position(dto.getPosition())
                .generation(Generation.fromLabel(dto.getGeneration()))
                .isMain(true)
                .build();

        user.addGeneration(ug);
        userGenerationRepository.save(ug);
    }

    private User getUser(String email) {
        return userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.USER_NOT_FOUND.getMessage()));
    }

    private void validatePassword(String input, String encoded) {
        if (!passwordEncoder.matches(input, encoded))
            throw new IllegalArgumentException(ExceptionMessage.INVALID_PASSWORD.getMessage());
    }

    private RefreshToken validateRefreshToken(String token) {
        if (token == null)
            throw new IllegalArgumentException(ExceptionMessage.REFRESH_TOKEN_INVALID.getMessage());

        return tokenService.validate(token);
    }

    private void validateUserStatus(User user) {
        if (user.isDeleted()) throw new IllegalStateException(ExceptionMessage.USER_NOT_EXIST.getMessage());
        if (user.getApprovalStatus() == ApprovalStatus.WAITING) throw new IllegalStateException(ExceptionMessage.USER_NOT_APPROVED.getMessage());
        if (user.getApprovalStatus() == ApprovalStatus.REJECTED) throw new IllegalStateException(ExceptionMessage.USER_REJECTED.getMessage());
        if (user.getUserStatus() == UserStatus.BANNED) throw new LockedException(ExceptionMessage.BANNED_USER.getMessage());
    }
}

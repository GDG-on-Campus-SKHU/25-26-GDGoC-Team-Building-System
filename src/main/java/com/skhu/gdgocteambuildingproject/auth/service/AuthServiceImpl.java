package com.skhu.gdgocteambuildingproject.auth.service;

import com.skhu.gdgocteambuildingproject.auth.domain.RefreshToken;
import com.skhu.gdgocteambuildingproject.auth.dto.request.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.auth.service.dto.AuthTokenBundle;
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

    @Override
    @Transactional
    public AuthTokenBundle signUp(SignUpRequestDto dto) {
        if (userRepository.existsByEmailAndDeletedFalse(dto.getEmail())) {
            throw new IllegalArgumentException(ExceptionMessage.EMAIL_ALREADY_EXISTS.getMessage());
        }

        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_PASSWORD.getMessage());
        }

        User user = User.builder()
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .name(dto.getName())
                .number(dto.getNumber())
                .school(dto.getSchool())
                .role(dto.getRole())
                .part(dto.getPart())
                .build();

        User saved = userRepository.save(user);

        UserGeneration ug = UserGeneration.builder()
                .user(saved)
                .position(dto.getPosition())
                .generation(Generation.fromLabel(dto.getGeneration()))
                .isMain(true)
                .build();

        saved.addGeneration(ug);
        userGenerationRepository.save(ug);

        String access = tokenService.createAccessToken(saved);
        String refresh = tokenService.createRefreshToken(saved);

        tokenService.store(saved, refresh);

        return AuthTokenBundle.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .user(saved)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public AuthTokenBundle login(LoginRequestDto dto) {
        User user = userRepository.findByEmailAndDeletedFalse(dto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.USER_NOT_FOUND.getMessage()));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_PASSWORD.getMessage());
        }

        validateUserStatus(user);

        String access = tokenService.createAccessToken(user);
        String refresh = tokenService.createRefreshToken(user);

        tokenService.store(user, refresh);

        return AuthTokenBundle.builder()
                .accessToken(access)
                .refreshToken(refresh)
                .user(user)
                .build();
    }

    @Override
    @Transactional
    public AuthTokenBundle refresh(String refreshToken) {
        if (refreshToken == null) {
            throw new IllegalArgumentException(ExceptionMessage.REFRESH_TOKEN_INVALID.getMessage());
        }

        RefreshToken old = tokenService.validate(refreshToken);

        if (old == null) {
            throw new IllegalArgumentException(ExceptionMessage.REFRESH_TOKEN_INVALID.getMessage());
        }

        User user = old.getUser();

        validateUserStatus(user);

        String newAccess = tokenService.createAccessToken(user);
        String newRefresh = tokenService.rotate(old);

        return AuthTokenBundle.builder()
                .accessToken(newAccess)
                .refreshToken(newRefresh)
                .user(user)
                .build();
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken != null) {
            tokenService.deleteByToken(refreshToken);
        }
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.USER_NOT_EXIST.getMessage()));

        tokenService.deleteAllByUser(user);
        user.softDelete();
    }

    private void validateUserStatus(User user) {
        if (user.isDeleted()) {
            throw new IllegalStateException(ExceptionMessage.USER_NOT_EXIST.getMessage());
        }
        if (user.getApprovalStatus() == ApprovalStatus.WAITING) {
            throw new IllegalStateException(ExceptionMessage.USER_NOT_APPROVED.getMessage());
        }
        if (user.getApprovalStatus() == ApprovalStatus.REJECTED) {
            throw new IllegalStateException(ExceptionMessage.USER_REJECTED.getMessage());
        }
        if (user.getUserStatus() == UserStatus.BANNED) {
            throw new LockedException(ExceptionMessage.BANNED_USER.getMessage());
        }
    }
}

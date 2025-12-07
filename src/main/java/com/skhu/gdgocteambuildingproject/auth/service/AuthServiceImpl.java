package com.skhu.gdgocteambuildingproject.auth.service;

import com.skhu.gdgocteambuildingproject.auth.domain.RefreshToken;
import com.skhu.gdgocteambuildingproject.auth.dto.request.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.RefreshTokenRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.response.LoginResponseDto;
import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.global.jwt.service.TokenService;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.UserGeneration;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserPosition;
import com.skhu.gdgocteambuildingproject.user.repository.UserGenerationRepository;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;
    private final UserGenerationRepository userGenerationRepository;

    @Override
    @Transactional
    public LoginResponseDto signUp(SignUpRequestDto dto) {
        if (userRepository.existsByEmailAndDeletedFalse(dto.getEmail())) {
            throw new IllegalArgumentException(ExceptionMessage.EMAIL_ALREADY_EXISTS.getMessage());
        }

        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_PASSWORD.getMessage());
        }

        User savedUser = userRepository.save(
                dto.toEntity(passwordEncoder.encode(dto.getPassword()))
        );

        Generation generation = convertGenerationLabel(dto.getGeneration());
        saveUserGeneration(savedUser, generation, dto.getPosition());

        return createLoginResponse(savedUser);
    }

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmailAndDeletedFalse(dto.getEmail())
                .orElseThrow(() -> new EntityNotFoundException(ExceptionMessage.USER_NOT_FOUND.getMessage()));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException(ExceptionMessage.INVALID_PASSWORD.getMessage());
        }

        validateUserStatus(user);

        return createLoginResponse(user);
    }

    @Override
    @Transactional
    public LoginResponseDto refresh(RefreshTokenRequestDto dto) {
        RefreshToken oldToken = tokenService.validate(dto.getRefreshToken());
        User user = oldToken.getUser();

        validateUserStatus(user);

        String newAccess = tokenService.createAccessToken(user);
        String newRefresh = tokenService.rotate(oldToken);

        return buildLoginResponse(user, newAccess, newRefresh);
    }

    @Override
    @Transactional
    public void logout(RefreshTokenRequestDto dto) {
        tokenService.deleteByToken(dto.getRefreshToken());
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
    }

    private LoginResponseDto createLoginResponse(User user) {
        String accessToken = tokenService.createAccessToken(user);
        String refreshToken = tokenService.createRefreshToken(user);

        tokenService.store(user, refreshToken);

        return buildLoginResponse(user, accessToken, refreshToken);
    }

    private LoginResponseDto buildLoginResponse(User user,
                                                String accessToken,
                                                String refreshToken) {
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .email(user.getEmail())
                .name(user.getName())
                .role(user.getRole().name())
                .build();
    }

    private void saveUserGeneration(User user, Generation generation, UserPosition position) {
        UserGeneration userGeneration = UserGeneration.builder()
                .user(user)
                .position(position)
                .generation(generation)
                .build();

        user.addGeneration(userGeneration);

        userGenerationRepository.save(userGeneration);
    }

    private Generation convertGenerationLabel(String label) {
        return Arrays.stream(Generation.values())
                .filter(g -> g.getLabel().equals(label))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 기수 값입니다: " + label));
    }
}

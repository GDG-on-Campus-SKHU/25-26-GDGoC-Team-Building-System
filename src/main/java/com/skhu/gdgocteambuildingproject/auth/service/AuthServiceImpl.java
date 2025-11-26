package com.skhu.gdgocteambuildingproject.auth.service;

import com.skhu.gdgocteambuildingproject.auth.domain.RefreshToken;
import com.skhu.gdgocteambuildingproject.auth.dto.request.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.RefreshTokenRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.request.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.response.LoginResponseDto;
import com.skhu.gdgocteambuildingproject.auth.repository.RefreshTokenRepository;
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
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        User user = dto.toEntity(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);

        return createLoginResponse(user);
    }

    @Override
    @Transactional
    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmailAndDeletedFalse(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        validateUserStatus(user);

        return createLoginResponse(user);
    }

    @Override
    @Transactional
    public LoginResponseDto refresh(RefreshTokenRequestDto dto) {
        RefreshToken existing = refreshTokenRepository.findByToken(dto.getRefreshToken())
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 리프레시 토큰입니다."));

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
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        refreshTokenRepository.deleteAllByUser(user);
        user.softDelete();
    }

    private void validateUserStatus(User user) {
        if (user.isDeleted()) {
            throw new IllegalStateException("탈퇴한 회원입니다.");
        }
        if (user.getApprovalStatus() == ApprovalStatus.WAITING) {
            throw new IllegalStateException("관리자 승인 대기 중입니다.");
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

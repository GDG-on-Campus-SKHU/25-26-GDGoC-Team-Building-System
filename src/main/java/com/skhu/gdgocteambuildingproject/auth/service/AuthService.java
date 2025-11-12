package com.skhu.gdgocteambuildingproject.auth.service;

import com.skhu.gdgocteambuildingproject.auth.dto.LoginRequestDto;
import com.skhu.gdgocteambuildingproject.auth.dto.LoginResponseDto;
import com.skhu.gdgocteambuildingproject.auth.dto.SignUpRequestDto;
import com.skhu.gdgocteambuildingproject.global.jwt.TokenProvider;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional
    public LoginResponseDto signUp(SignUpRequestDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }

        if (!dto.getPassword().equals(dto.getPasswordConfirm())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        User user = new User(
                dto.getEmail(),
                passwordEncoder.encode(dto.getPassword()),
                dto.getName(),
                dto.getNumber(),
                dto.getIntroduction(),
                dto.getSchool(),
                dto.getRole(),
                dto.getPosition(),
                dto.getPart(),
                dto.getGeneration()
        );

        userRepository.save(user);

        String accessToken = tokenProvider.createAccessToken(user);
        String refreshToken = tokenProvider.createRefreshToken(user);

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return new LoginResponseDto(
                accessToken,
                refreshToken,
                user.getEmail(),
                user.getName(),
                user.getRole().name()
        );
    }

    @Transactional(readOnly = true)
    public LoginResponseDto login(LoginRequestDto dto) {
        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        if (!passwordEncoder.matches(dto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }

        if (!user.isApproved()) {
            throw new IllegalStateException("관리자 승인 대기 중입니다.");
        }

        String accessToken = tokenProvider.createAccessToken(user);
        String refreshToken = tokenProvider.createRefreshToken(user);

        user.updateRefreshToken(refreshToken);
        userRepository.save(user);

        return new LoginResponseDto(accessToken, refreshToken, user.getEmail(), user.getName(), user.getRole().name());
    }

}
package com.skhu.gdgocteambuildingproject.global.email.service;

import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ResetPasswordService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void resetPassword(String email, String newPassword) {

        var user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                ExceptionMessage.USER_EMAIL_NOT_EXIST.getMessage()
                        ));

        user.updatePassword(passwordEncoder.encode(newPassword));
    }
}

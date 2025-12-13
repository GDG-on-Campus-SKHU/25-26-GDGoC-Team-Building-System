package com.skhu.gdgocteambuildingproject.global.email.service;

import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.user.domain.User;
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
    private final EmailVerificationService emailVerificationService;

    @Transactional
    public void resetPassword(String email, String code, String newPassword) {
        emailVerificationService.validateCode(email, code);

        User user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                ExceptionMessage.USER_EMAIL_NOT_EXIST.getMessage()
                        )
                );

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException(
                    ExceptionMessage.PASSWORD_SAME_AS_OLD.getMessage()
            );
        }
        user.updatePassword(passwordEncoder.encode(newPassword));
        emailVerificationService.consumeCode(email);
    }

    private void validateRequest(String email, String code, String newPassword) {
        if (email == null || email.isBlank()
                || code == null || code.isBlank()
                || newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException(
                    ExceptionMessage.PASSWORD_INVALID_FORMAT.getMessage()
            );
        }
    }
}

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
        validateResetPasswordRequest(email, code, newPassword);
        emailVerificationService.verifyCode(email, code);

        User user = getUserByEmail(email);
        validateNewPassword(user, newPassword);

        user.updatePassword(passwordEncoder.encode(newPassword));
    }

    private void validateResetPasswordRequest(String email, String code, String newPassword) {
        if (email == null || email.isBlank()
                || code == null || code.isBlank()
                || newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException(
                    ExceptionMessage.PASSWORD_INVALID_FORMAT.getMessage()
            );
        }

        if (newPassword.length() < 8) {
            throw new IllegalArgumentException(
                    ExceptionMessage.PASSWORD_INVALID_FORMAT.getMessage()
            );
        }
    }

    private User getUserByEmail(String email) {
        return userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                ExceptionMessage.USER_EMAIL_NOT_EXIST.getMessage()
                        )
                );
    }

    private void validateNewPassword(User user, String newPassword) {
        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException(
                    ExceptionMessage.PASSWORD_SAME_AS_OLD.getMessage()
            );
        }
    }
}

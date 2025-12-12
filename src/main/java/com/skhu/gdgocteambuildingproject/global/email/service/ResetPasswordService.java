package com.skhu.gdgocteambuildingproject.global.email.service;

import com.skhu.gdgocteambuildingproject.global.exception.ExceptionMessage;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserStatus;
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
        validateParams(email, code, newPassword);
        emailVerificationService.verifyCodeOrThrow(email, code);

        var user = userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                ExceptionMessage.USER_EMAIL_NOT_EXIST.getMessage()
                        )
                );

        if (user.getRole() != UserRole.SKHU_ADMIN) {

            if (user.getApprovalStatus() != ApprovalStatus.APPROVED) {
                throw new IllegalStateException(
                        ExceptionMessage.USER_NOT_APPROVED.getMessage()
                );
            }

            if (user.getUserStatus() == UserStatus.BANNED) {
                throw new IllegalStateException(
                        ExceptionMessage.BANNED_USER.getMessage()
                );
            }
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new IllegalArgumentException(
                    ExceptionMessage.PASSWORD_SAME_AS_OLD.getMessage()
            );
        }

        user.updatePassword(passwordEncoder.encode(newPassword));
    }

    private void validateParams(String email, String code, String newPassword) {
        if (email == null || email.isBlank() ||
                code == null || code.isBlank() ||
                newPassword == null || newPassword.isBlank()) {
            throw new IllegalArgumentException(ExceptionMessage.PASSWORD_INVALID_FORMAT.getMessage());
        }

        if (newPassword.length() < 8) {
            throw new IllegalArgumentException(ExceptionMessage.PASSWORD_INVALID_FORMAT.getMessage());
        }
    }
}

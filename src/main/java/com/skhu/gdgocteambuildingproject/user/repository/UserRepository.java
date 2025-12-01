package com.skhu.gdgocteambuildingproject.user.repository;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndDeletedFalse(String email);

    boolean existsByEmailAndDeletedFalse(String email);

    Page<User> findAllByApprovalStatus(ApprovalStatus approvalStatus, Pageable pageable);

    List<User> findByNameContaining(String query);

    Page<User> findByNameContainingAndApprovalStatus(
            String query,
            ApprovalStatus status,
            Pageable pageable
    );

    Page<User> findByPartAndApprovalStatus(
            Part part,
            ApprovalStatus status,
            Pageable pageable
    );

    Page<User> findBySchoolContainingAndApprovalStatus(
            String school,
            ApprovalStatus status,
            Pageable pageable
    );
}

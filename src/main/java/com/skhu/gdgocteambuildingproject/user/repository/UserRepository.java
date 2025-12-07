package com.skhu.gdgocteambuildingproject.user.repository;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

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

    @Query("""
            SELECT DISTINCT u
            FROM User u
                JOIN u.generation g
            WHERE u.deleted = false
                AND u.approvalStatus = :approvalStatus
                AND u.userStatus = :userStatus
                AND (:generation IS NULL OR g.generation = :generation)
                AND (:schools IS NULL OR u.school IN :schools)
            """)
    List<User> searchUsers(
            @Param("generation") Generation generation,
            @Param("schools") List<String> schools,
            @Param("approvalStatus") ApprovalStatus approvalStatus,
            @Param("userStatus") UserStatus userStatus
    );
}

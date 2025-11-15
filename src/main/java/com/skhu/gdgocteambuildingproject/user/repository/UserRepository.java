package com.skhu.gdgocteambuildingproject.user.repository;

import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    List<User> findByNameContaining(String query);
}

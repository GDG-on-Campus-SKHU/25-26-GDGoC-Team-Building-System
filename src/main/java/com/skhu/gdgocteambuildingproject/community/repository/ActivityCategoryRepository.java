package com.skhu.gdgocteambuildingproject.community.repository;

import com.skhu.gdgocteambuildingproject.community.domain.ActivityCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityCategoryRepository extends JpaRepository<ActivityCategory, Long> {
    Optional<ActivityCategory> findByName(String name);
}

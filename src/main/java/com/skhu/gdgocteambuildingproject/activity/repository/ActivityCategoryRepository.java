package com.skhu.gdgocteambuildingproject.activity.repository;

import com.skhu.gdgocteambuildingproject.activity.domain.ActivityCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ActivityCategoryRepository extends JpaRepository<ActivityCategory, Long> {
    Optional<ActivityCategory> findByName(String name);
}

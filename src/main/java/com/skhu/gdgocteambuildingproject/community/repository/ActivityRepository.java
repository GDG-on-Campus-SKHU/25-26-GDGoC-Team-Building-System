package com.skhu.gdgocteambuildingproject.community.repository;

import com.skhu.gdgocteambuildingproject.community.domain.Activity;
import com.skhu.gdgocteambuildingproject.community.domain.ActivityCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    long countByActivityCategory(ActivityCategory activityCategory);

    List<Activity> findByActivityCategory(ActivityCategory category);
}

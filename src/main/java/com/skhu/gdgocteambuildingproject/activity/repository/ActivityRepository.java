package com.skhu.gdgocteambuildingproject.activity.repository;

import com.skhu.gdgocteambuildingproject.activity.domain.Activity;
import com.skhu.gdgocteambuildingproject.activity.domain.ActivityCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {
    long countByActivityCategory(ActivityCategory activityCategory);

    List<Activity> findByActivityCategory(ActivityCategory category);

    @Modifying(clearAutomatically = true)
    @Query("DELETE FROM Activity a WHERE a.activityCategory = :category")
    void deleteAllByActivityCategory(ActivityCategory category);
}

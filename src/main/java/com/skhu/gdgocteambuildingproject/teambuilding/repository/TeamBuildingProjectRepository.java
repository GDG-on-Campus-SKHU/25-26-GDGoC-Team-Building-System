package com.skhu.gdgocteambuildingproject.teambuilding.repository;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.ScheduleType;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TeamBuildingProjectRepository extends JpaRepository<TeamBuildingProject, Long> {

    @Query("""
    SELECT DISTINCT p
    FROM TeamBuildingProject p
    LEFT JOIN FETCH p.schedules
    WHERE EXISTS (
        SELECT 1
        FROM ProjectSchedule s
        WHERE s.project = p
          AND s.type = :type
          AND s.endDate < :criteria
    )
    """)
    List<TeamBuildingProject> findProjectsWithScheduleEndedBefore(
            @Param("type") ScheduleType type,
            @Param("criteria") LocalDateTime criteria
    );

    @Query("""
    SELECT DISTINCT p
    FROM TeamBuildingProject p
    LEFT JOIN FETCH p.schedules
    WHERE EXISTS (
        SELECT 1
        FROM ProjectSchedule s
        WHERE s.project = p
          AND s.type = :type
          AND (s.startDate IS NULL OR s.startDate > :criteria)
    )
    """)
    List<TeamBuildingProject> findProjectsWithScheduleNotStartedBefore(
            @Param("type") ScheduleType type,
            @Param("criteria") LocalDateTime criteria
    );

    @Query("""
    SELECT DISTINCT p
    FROM TeamBuildingProject p
    LEFT JOIN FETCH p.schedules
    WHERE EXISTS (
        SELECT 1
        FROM ProjectSchedule s
        WHERE s.project = p
          AND s.type = :type
          AND s.startDate IS NOT NULL
          AND s.startDate < :criteria
    )
    """)
    List<TeamBuildingProject> findProjectsWithScheduleStartedBefore(
            @Param("type") ScheduleType type,
            @Param("criteria") LocalDateTime criteria
    );
}

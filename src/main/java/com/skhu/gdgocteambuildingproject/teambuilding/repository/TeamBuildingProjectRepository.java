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
    LEFT JOIN FETCH p.schedules s
    WHERE s.type = :type
      AND s.endDate < :criteria
    """)
    List<TeamBuildingProject> findProjectsWithScheduleEndedBefore(
            @Param("type") ScheduleType type,
            @Param("criteria") LocalDateTime criteria
    );

    @Query("""
    SELECT DISTINCT p
    FROM TeamBuildingProject p
    LEFT JOIN FETCH p.schedules s
    WHERE s.type = :type
      AND s.endDate > :criteria
    """)
    List<TeamBuildingProject> findProjectsWithScheduleNotEndedBefore(
            @Param("type") ScheduleType type,
            @Param("criteria") LocalDateTime criteria
    );
}

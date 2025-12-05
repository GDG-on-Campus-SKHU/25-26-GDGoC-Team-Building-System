package com.skhu.gdgocteambuildingproject.teambuilding.repository;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectSchedule;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ProjectScheduleRepository extends JpaRepository<ProjectSchedule, Long> {
    @Query("""
        SELECT s
        FROM ProjectSchedule s
        WHERE s.confirmed = false
          AND s.endDate < :now
        """)
    List<ProjectSchedule> findUnconfirmedSchedulesEndedBefore(@Param("now") LocalDateTime now);
}


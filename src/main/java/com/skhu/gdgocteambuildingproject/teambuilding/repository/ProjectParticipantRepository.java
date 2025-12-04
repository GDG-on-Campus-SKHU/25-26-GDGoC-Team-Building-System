package com.skhu.gdgocteambuildingproject.teambuilding.repository;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectParticipantRepository extends JpaRepository<ProjectParticipant, Long> {
    boolean existsByUserIdAndProjectId(long userId, long projectId);
}

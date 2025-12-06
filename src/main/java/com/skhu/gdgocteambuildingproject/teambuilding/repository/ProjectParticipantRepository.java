package com.skhu.gdgocteambuildingproject.teambuilding.repository;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectParticipant;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectParticipantRepository extends JpaRepository<ProjectParticipant, Long> {
    boolean existsByUserIdAndProjectId(long userId, long projectId);

    List<ProjectParticipant> findByProjectId(long projectId);

    void deleteByProjectId(long projectId);
}

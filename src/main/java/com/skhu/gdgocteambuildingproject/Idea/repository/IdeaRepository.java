package com.skhu.gdgocteambuildingproject.Idea.repository;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdeaRepository extends JpaRepository<Idea, Long> {
    Page<Idea> findByProjectId(long projectId, Pageable pageable);

    Optional<Idea> findByIdAndProjectId(long ideaId, long projectId);

    Optional<Idea> findByCreatorIdAndProjectId(long creatorId, long projectId);

    Optional<Idea> findByCreatorAndProject(User creator, TeamBuildingProject project);
}

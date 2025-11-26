package com.skhu.gdgocteambuildingproject.Idea.repository;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.IdeaStatus;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IdeaRepository extends JpaRepository<Idea, Long> {
    Page<Idea> findByProjectId(long projectId, Pageable pageable);

    Page<Idea> findByProjectIdAndRecruitingIsTrue(long projectId, Pageable pageable);

    @Query(
            value = "SELECT * FROM idea WHERE project_id = :project_id",
            countQuery = "SELECT count(*) FROM idea WHERE project_id = :project_id",
            nativeQuery = true
    )
    Page<Idea> findByProjectIdIncludeDeleted(
            @Param("project_id") long projectId,
            Pageable pageable
    );

    Optional<Idea> findByIdAndProjectId(long ideaId, long projectId);

    Optional<Idea> findByCreatorIdAndProjectId(long creatorId, long projectId);

    Optional<Idea> findByIdAndCreatorIdAndProjectId(long ideaId, long creatorId, long projectId);

    Optional<Idea> findByCreatorAndProject(User creator, TeamBuildingProject project);

    @Query(
            value = "SELECT DISTINCT i FROM Idea i "
                    + "LEFT JOIN FETCH i.enrollments e "
                    + "LEFT JOIN FETCH e.applicant "
                    + "WHERE i.creator.id = :creatorId "
                    + "AND i.project.id = :projectId "
                    + "AND i.registerStatus = :status"
    )
    Optional<Idea> findIdeaOf(
            @Param("creatorId") long creatorId,
            @Param("projectId") long projectId,
            @Param("status") IdeaStatus status
    );
}

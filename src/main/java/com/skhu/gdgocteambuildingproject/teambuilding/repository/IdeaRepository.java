package com.skhu.gdgocteambuildingproject.teambuilding.repository;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.enumtype.IdeaStatus;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.TeamBuildingProject;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IdeaRepository extends JpaRepository<Idea, Long> {
    Optional<Idea> findByIdAndRegisterStatus(long ideaId, IdeaStatus registerStatus);

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

    @Query(
            value = "SELECT i FROM Idea i JOIN FETCH i.project JOIN FETCH i.project.availableParts WHERE i.id = :ideaId"
    )
    Optional<Idea> findByIdIncludeDeleted(@Param("ideaId") long ideaId);

    @Query(
            value = "SELECT * FROM idea i WHERE i.id = :idea_id AND i.deleted = true",
            nativeQuery = true
    )
    Optional<Idea> findDeletedIdeaById(@Param("idea_id") long ideaId);

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

    @Query("""
        SELECT DISTINCT i
        FROM Idea i
        LEFT JOIN FETCH i.enrollments e
        WHERE EXISTS (
            SELECT 1
            FROM IdeaEnrollment enrollment
            WHERE enrollment.idea = i
              AND enrollment.schedule.id = :scheduleId
        )
          AND i.registerStatus = :status
        """)
    List<Idea> findByScheduleIdAndRegisterStatus(
            @Param("scheduleId") long scheduleId,
            @Param("status") IdeaStatus status
    );
}

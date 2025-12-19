package com.skhu.gdgocteambuildingproject.teambuilding.repository;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.IdeaMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface IdeaMemberRepository extends JpaRepository<IdeaMember, Long> {
    @Query("""
                SELECT im
                FROM IdeaMember im JOIN FETCH im.user
                WHERE im.id = :id
            """)
    Optional<IdeaMember> findByIdWithUser(Long id);

    @Modifying
    @Query("DELETE FROM IdeaMember im WHERE im.idea.id = :ideaId")
    void deleteAllByIdeaId(@Param("ideaId") long ideaId);

    @Query("""
                select count(im)
                from IdeaMember im
                where im.idea.project.id = :projectId
            """)
    int countConfirmedMembers(@Param("projectId") Long projectId);
}

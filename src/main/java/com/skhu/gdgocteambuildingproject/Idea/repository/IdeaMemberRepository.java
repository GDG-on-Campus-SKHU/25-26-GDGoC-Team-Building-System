package com.skhu.gdgocteambuildingproject.Idea.repository;

import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface IdeaMemberRepository extends JpaRepository<IdeaMember, Long> {
    @Query("""
                SELECT im
                FROM IdeaMember im JOIN FETCH im.user
                WHERE im.id = :id
            """)
    Optional<IdeaMember> findByIdWithUser(Long id);
}

package com.skhu.gdgocteambuildingproject.Idea.repository;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IdeaRepository extends JpaRepository<Idea, Long> {
    Page<Idea> findByProjectId(Long projectId, Pageable pageable);
}

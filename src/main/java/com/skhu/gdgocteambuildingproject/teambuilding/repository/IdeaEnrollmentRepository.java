package com.skhu.gdgocteambuildingproject.teambuilding.repository;

import com.skhu.gdgocteambuildingproject.Idea.domain.IdeaEnrollment;
import jakarta.persistence.LockModeType;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface IdeaEnrollmentRepository extends JpaRepository<IdeaEnrollment, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT e FROM IdeaEnrollment e JOIN FETCH e.idea WHERE e.id = :enrollmentId")
    Optional<IdeaEnrollment> findByIdWithIdeaLock(@Param("enrollmentId") Long enrollmentId);
}

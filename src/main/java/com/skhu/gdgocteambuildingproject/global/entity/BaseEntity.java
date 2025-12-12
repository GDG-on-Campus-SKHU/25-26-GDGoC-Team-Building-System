package com.skhu.gdgocteambuildingproject.global.entity;

import jakarta.persistence.*;
import java.util.Objects;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BaseEntity otherBaseEntity)) {
            return false;
        }
        return Objects.equals(getId(), otherBaseEntity.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}

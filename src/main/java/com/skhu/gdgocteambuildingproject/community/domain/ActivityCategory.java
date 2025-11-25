package com.skhu.gdgocteambuildingproject.community.domain;

import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ActivityCategory extends BaseEntity {

    @Column(length = 50, nullable = false)
    private String name;

    private boolean isPublished = false; // 기본값은 미게시

    @Builder
    public ActivityCategory(String name, boolean isPublished) {
        this.name = name;
        this.isPublished = isPublished;
    }
}

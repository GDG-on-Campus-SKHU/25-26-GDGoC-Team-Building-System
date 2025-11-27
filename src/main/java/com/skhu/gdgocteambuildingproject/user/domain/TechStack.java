package com.skhu.gdgocteambuildingproject.user.domain;

import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.TechStackType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TechStack extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TechStackType techStackType;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public TechStack(TechStackType techStackType, User user) {
        this.techStackType = techStackType;
        this.user = user;
    }
}

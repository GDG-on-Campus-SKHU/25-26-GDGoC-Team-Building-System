package com.skhu.gdgocteambuildingproject.projectgallery.domain;

import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.ServiceStatus;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GalleryProject extends BaseEntity {

    @Column(nullable = false)
    private String projectName;
    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Generation generation;
    @Column(nullable = false)
    private String shortDescription;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ServiceStatus serviceStatus;

    @Lob
    @Column(nullable = false)
    private String description;

    private String thumbnailUrl; // null일 결우 프론트에서 기본이미지 처리

    @Builder.Default
    private boolean isExhibited = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<GalleryProjectMember> members = new ArrayList<>();

    public void update(
            String projectName,
            Generation generation,
            String shortDescription,
            ServiceStatus serviceStatus,
            String description,
            String thumbnailUrl,
            User leader
    ) {
        this.projectName = projectName;
        this.generation = generation;
        this.shortDescription = shortDescription;
        this.serviceStatus = serviceStatus;
        this.description = description;
        this.thumbnailUrl = thumbnailUrl;
        this.user = leader;
    }

    public void clearMembers() {
        this.members.clear();
    }
}

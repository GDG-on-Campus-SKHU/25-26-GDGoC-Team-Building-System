package com.skhu.gdgocteambuildingproject.user.domain;

import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.LinkType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserLink extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LinkType linkType;

    @Column(nullable = false)
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Builder
    public UserLink(LinkType linkType, String url, User user) {
        this.linkType = linkType;
        this.url = url;
        this.user = user;
    }
}

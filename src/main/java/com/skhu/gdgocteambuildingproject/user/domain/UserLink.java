package com.skhu.gdgocteambuildingproject.user.domain;

import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.LinkType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.net.URI;

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

    public void validateLink() {
        if (url == null || url.isBlank() || url.isEmpty()) {
            throw new IllegalArgumentException("주소를 입력해주세요.");
        }

        URI uri;
        try {
            uri = URI.create(url);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효한 URL 형식이 아닙니다.");
        }

        String host = uri.getHost();
        if (host == null) {
            throw new IllegalArgumentException("유효한 URL 형식이 아닙니다.");
        }

        if (!linkType.matchesHost(host)) {
            throw new IllegalArgumentException(
                    "선택한 링크 종류(" + linkType.getDisplayName() + ")와 URL이 맞지 않습니다."
            );
        }
    }
}

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
        if (url == null || url.isBlank()) {
            throw new IllegalArgumentException("주소를 입력해주세요.");
        }

        url = normalizeUrl(url);

        URI uri;
        try {
            uri = URI.create(url);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("유효한 URL 형식이 아닙니다.");
        }

        String scheme = uri.getScheme();
        if (scheme == null || !(scheme.equalsIgnoreCase("http") || scheme.equalsIgnoreCase("https"))) {
            throw new IllegalArgumentException("링크는 http:// 또는 https:// 로만 시작할 수 있습니다.");
        }

        String rawAuthority = uri.getRawAuthority();
        if (rawAuthority == null || rawAuthority.isBlank() || rawAuthority.endsWith(":")) {
            throw new IllegalArgumentException("유효한 URL 형식이 아닙니다.");
        }

        String rawHost = uri.getHost();
        if (rawHost == null || rawHost.isBlank()) {
            throw new IllegalArgumentException("유효한 URL 형식이 아닙니다.");
        }

        String host = stripWww(rawHost);

        if (!linkType.matchesHost(host)) {
            throw new IllegalArgumentException(
                    "선택한 링크 종류(" + linkType.getDisplayName() + ")와 URL이 맞지 않습니다."
            );
        }
    }

    private String normalizeUrl(String raw) {
        String s = raw.trim();
        if (s.isEmpty()) return s;

        try {
            URI maybe = URI.create(s);
            if (maybe.getScheme() != null) {
                return s;
            }
        } catch (IllegalArgumentException ignored) {
        }

        if (s.startsWith("//")) {
            return "https:" + s;
        }

        return "https://" + s;
    }

    private String stripWww(String host) {
        String lower = host.toLowerCase();
        if (lower.startsWith("www.")) {
            return host.substring(4);
        }
        return host;
    }
}

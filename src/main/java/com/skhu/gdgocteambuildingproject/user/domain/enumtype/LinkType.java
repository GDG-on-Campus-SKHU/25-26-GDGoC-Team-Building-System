package com.skhu.gdgocteambuildingproject.user.domain.enumtype;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum LinkType {
    GITHUB("GitHub"),
    BLOG("Blog"),
    LINKEDIN("LinkedIn"),
    VELOG("Velog"),
    TISTORY("Tistory"),
    NOTION("Notion"),
    BAEKJOON("BaekJoon"),
    INSTAGRAM("Instagram"),
    TWITTER("Twitter"),
    X("X"),
    FACEBOOK("Facebook"),
    YOUTUBE("YouTube"),
    OTHER("기타");

    private final String displayName;

    private static final String SIMPLE_ICONS_BASE_URL = "https://cdn.simpleicons.org/";

    public String getLinkIconUrl() {
        return switch (this) {
            case BLOG -> "/icons/blog.png";
            case LINKEDIN -> "/icons/linkedin.png";
            case BAEKJOON -> "/icons/baekjoon.png";
            case TWITTER -> "/icons/twitter.png";
            case OTHER -> "/icons/other_guitar.png";
            default -> SIMPLE_ICONS_BASE_URL + getIconSlug();
        };
    }

    private String getIconSlug() {
        return displayName.toLowerCase().replace(" ", "");
    }

}

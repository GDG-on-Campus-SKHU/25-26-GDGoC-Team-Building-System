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

    public boolean matchesHost(String host) {
        return switch (this) {
            case GITHUB     -> host.endsWith("github.com");
            case VELOG      -> host.endsWith("velog.io");
            case LINKEDIN   -> host.endsWith("linkedin.com");
            case TISTORY    -> host.endsWith("tistory.com");
            case NOTION     -> host.endsWith("notion.so") || host.endsWith("notion.site");
            case BAEKJOON   -> host.endsWith("acmicpc.net");
            case INSTAGRAM  -> host.endsWith("instagram.com");
            case TWITTER, X -> host.endsWith("twitter.com") || host.endsWith("x.com");
            case FACEBOOK   -> host.endsWith("facebook.com");
            case YOUTUBE    -> host.endsWith("youtube.com") || host.endsWith("youtu.be");
            case BLOG       -> true;
            case OTHER      -> true;
        };
    }
}

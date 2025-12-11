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
        String lowerCaseHost = host.toLowerCase();

        return switch (this) {
            case GITHUB     -> lowerCaseHost.endsWith("github.com");
            case VELOG      -> lowerCaseHost.endsWith("velog.io");
            case LINKEDIN   -> lowerCaseHost.endsWith("linkedin.com");
            case TISTORY    -> lowerCaseHost.endsWith("tistory.com");
            case NOTION     -> lowerCaseHost.endsWith("notion.so") || lowerCaseHost.endsWith("notion.site");
            case BAEKJOON   -> lowerCaseHost.endsWith("acmicpc.net");
            case INSTAGRAM  -> lowerCaseHost.endsWith("instagram.com");
            case TWITTER, X -> lowerCaseHost.endsWith("twitter.com") || lowerCaseHost.endsWith("x.com");
            case FACEBOOK   -> lowerCaseHost.endsWith("facebook.com");
            case YOUTUBE    -> lowerCaseHost.endsWith("youtube.com") || lowerCaseHost.endsWith("youtu.be");
            case BLOG       -> true;
            case OTHER      -> true;
        };
    }
}

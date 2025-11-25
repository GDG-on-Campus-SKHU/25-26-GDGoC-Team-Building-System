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
    FACEBOOK("Facebook"),
    YOUTUBE("YouTube"),
    OTHER("기타");

    private final String displayName;
}

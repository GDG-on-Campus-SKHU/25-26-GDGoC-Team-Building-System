package com.skhu.gdgocteambuildingproject.mypage;

import com.skhu.gdgocteambuildingproject.user.domain.UserLink;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.LinkType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserLinkTest {

    private UserLink userLink(LinkType type, String url) {
        return UserLink.builder()
                .linkType(type)
                .url(url)
                .user(null)
                .build();
    }

    @Nested
    @DisplayName("정규화(normalize) 관련")
    class NormalizeTests {

        @ParameterizedTest(name = "[{index}] {0} 입력 -> {2} 저장")
        @CsvSource({
                // 스킴 없으면 https:// 붙음
                "GITHUB, github.com/taewoo, https://github.com/taewoo",
                // 앞뒤 공백 trim + https:// 붙음
                "GITHUB, '   github.com/taewoo   ', https://github.com/taewoo",
                // // 시작이면 https: 붙음
                "GITHUB, '//github.com/taewoo', https://github.com/taewoo",
                // 이미 http/https면 그대로 유지
                "GITHUB, http://github.com/taewoo, http://github.com/taewoo",
                "GITHUB, https://github.com/taewoo, https://github.com/taewoo",
                // www.가 있어도 validate는 통과해야 함(저장 문자열 자체는 그대로일 수 있음)
                "GITHUB, https://www.github.com/taewoo, https://www.github.com/taewoo"
        })
        @DisplayName("validateLink는 url을 정규화하고(필요 시 https 부여) 검증을 통과한다")
        void shouldNormalizeAndPass(String typeName, String input, String expectedStored) {
            // given
            LinkType type = LinkType.valueOf(typeName);
            UserLink link = userLink(type, input);

            // when
            link.validateLink();

            // then
            assertThat(link.getUrl()).isEqualTo(expectedStored);
        }
    }

    @Nested
    @DisplayName("scheme(http/https) 검증")
    class SchemeTests {

        @Test
        @DisplayName("ftp 스킴은 거부")
        void shouldRejectFtp() {
            assertThatThrownBy(() -> userLink(LinkType.GITHUB, "ftp://github.com/taewoo").validateLink())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("http:// 또는 https://");
        }

        @Test
        @DisplayName("javascript 스킴은 거부")
        void shouldRejectJavascript() {
            assertThatThrownBy(() -> userLink(LinkType.OTHER, "javascript:alert(1)").validateLink())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("http:// 또는 https://");
        }
    }

    @Nested
    @DisplayName("입력값 기본 검증")
    class BasicValidationTests {

        @Test
        @DisplayName("null/blank는 거부")
        void shouldRejectBlank() {
            assertThatThrownBy(() -> userLink(LinkType.GITHUB, "   ").validateLink())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("주소를 입력해주세요");
        }

        @Test
        @DisplayName("host 없는 URL(https://)은 거부")
        void shouldRejectMissingHost() {
            assertThatThrownBy(() -> userLink(LinkType.GITHUB, "https://").validateLink())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("유효한 URL 형식이 아닙니다");
        }
    }

    @Nested
    @DisplayName("LinkType별 host 매칭 검증")
    class LinkTypeHostMatchTests {

        @ParameterizedTest(name = "[{index}] {0} 타입은 {1} 입력을 통과해야 한다")
        @CsvSource({
                // GITHUB
                "GITHUB, https://github.com/taewoo",
                "GITHUB, https://www.github.com/taewoo",

                // VELOG
                "VELOG, https://velog.io/@taewoo",
                "VELOG, https://www.velog.io/@taewoo",

                // LINKEDIN
                "LINKEDIN, https://linkedin.com/in/taewoo",
                "LINKEDIN, https://www.linkedin.com/in/taewoo",

                // TISTORY
                "TISTORY, https://taewoo.tistory.com",

                // NOTION (2종 허용)
                "NOTION, https://notion.so/somepage",
                "NOTION, https://www.notion.so/somepage",
                "NOTION, https://notion.site/somepage",
                "NOTION, https://www.notion.site/somepage",

                // BAEKJOON
                "BAEKJOON, https://acmicpc.net/user/taewoo",
                "BAEKJOON, https://www.acmicpc.net/user/taewoo",

                // INSTAGRAM
                "INSTAGRAM, https://instagram.com/taewoo",
                "INSTAGRAM, https://www.instagram.com/taewoo",

                // TWITTER / X (둘 다 twitter.com or x.com 허용)
                "TWITTER, https://twitter.com/taewoo",
                "TWITTER, https://x.com/taewoo",
                "X, https://twitter.com/taewoo",
                "X, https://x.com/taewoo",

                // FACEBOOK
                "FACEBOOK, https://facebook.com/taewoo",
                "FACEBOOK, https://www.facebook.com/taewoo",

                // YOUTUBE (youtube.com / youtu.be)
                "YOUTUBE, https://youtube.com/@taewoo",
                "YOUTUBE, https://www.youtube.com/@taewoo",
                "YOUTUBE, https://youtu.be/abcDEF123",

                // BLOG / OTHER 는 항상 true
                "BLOG, https://example.com/taewoo",
                "OTHER, https://example.com/taewoo"
        })
        @DisplayName("각 LinkType은 허용된 host를 통과시킨다")
        void shouldPassAllowedHosts(String typeName, String url) {
            LinkType type = LinkType.valueOf(typeName);

            UserLink link = userLink(type, url);
            link.validateLink(); // 예외 없으면 통과
        }

        @ParameterizedTest(name = "[{index}] {0} 타입은 {1} 입력을 거부해야 한다")
        @CsvSource({
                // GITHUB인데 velog
                "GITHUB, https://velog.io/@taewoo",
                // NOTION인데 github
                "NOTION, https://github.com/taewoo",
                // BAEKJOON인데 youtube
                "BAEKJOON, https://youtube.com/@taewoo",
                // YOUTUBE인데 twitter
                "YOUTUBE, https://twitter.com/taewoo",
                // INSTAGRAM인데 tistory
                "INSTAGRAM, https://taewoo.tistory.com"
        })
        @DisplayName("LinkType과 host가 매칭되지 않으면 예외")
        void shouldRejectMismatchedHost(String typeName, String url) {
            LinkType type = LinkType.valueOf(typeName);

            assertThatThrownBy(() -> userLink(type, url).validateLink())
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessageContaining("URL이 맞지 않습니다");
        }
    }

    @Test
    @DisplayName("정규화된 URL이 DB에 저장될 값(url 필드)에 실제로 반영되는지 확인")
    void shouldMutateUrlField_whenNormalized() {
        UserLink link = userLink(LinkType.GITHUB, "github.com/taewoo");

        link.validateLink();

        assertThat(link.getUrl()).isEqualTo("https://github.com/taewoo");
    }
}

package com.skhu.gdgocteambuildingproject.projectgallery.model;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectMemberResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.model.mapper.GalleryProjectMemberMapper;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GalleryProjectMemberMapperTest {

    private static final String USER_NAME = "Test";
    private static final Part PART = Part.BACKEND;
    private static final MemberRole ROLE = MemberRole.MEMBER;

    private final GalleryProjectMemberMapper mapper = new GalleryProjectMemberMapper();

    @Test
    void GalleryProjectMember_엔티티를_GalleryProjectMemberResponseDto로_매핑한다() throws Exception {
        // given
        Constructor<User> constructor = User.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        User user = constructor.newInstance();

        ReflectionTestUtils.setField(user, "name", USER_NAME);

        GalleryProjectMember member = GalleryProjectMember.builder()
                .user(user)
                .part(PART)
                .role(ROLE)
                .build();

        // when
        List<GalleryProjectMemberResponseDto> result = mapper.mapMembersInfo(List.of(member));

        // then
        assertThat(result).hasSize(1);
        GalleryProjectMemberResponseDto dto = result.getFirst();

        assertThat(dto.name()).isEqualTo(USER_NAME);
        assertThat(dto.part()).isEqualTo(PART);
        assertThat(dto.memberRole()).isEqualTo(ROLE);
    }
}

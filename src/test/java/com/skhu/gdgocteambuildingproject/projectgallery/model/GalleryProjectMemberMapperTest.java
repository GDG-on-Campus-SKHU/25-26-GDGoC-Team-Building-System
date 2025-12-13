package com.skhu.gdgocteambuildingproject.projectgallery.model;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectMember;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.member.TokenUserInfoForProjectBuildingResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectMemberResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.model.mapper.GalleryProjectMemberMapper;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.UserGeneration;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserPosition;
import com.skhu.gdgocteambuildingproject.user.repository.UserGenerationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GalleryProjectMemberMapperTest {

    private static final String USER_NAME = "TestUser";
    private static final String SCHOOL = "SKHU";
    private static final Part PART = Part.BACKEND;
    private static final MemberRole ROLE = MemberRole.MEMBER;
    private static final Long USER_ID = 1L;
    private static final String GENERATION_AND_POSITION = "25-26 MEMBER";

    @Mock
    private UserGenerationRepository userGenerationRepository;

    @InjectMocks
    private GalleryProjectMemberMapper mapper;

    @Test
    void GalleryProjectMember_엔티티를_GalleryProjectMemberResponseDto로_매핑한다() throws Exception {
        // given
        Constructor<User> constructor = User.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        User user = constructor.newInstance();

        ReflectionTestUtils.setField(user, "id", USER_ID);
        ReflectionTestUtils.setField(user, "name", USER_NAME);
        ReflectionTestUtils.setField(user, "school", SCHOOL);

        GalleryProjectMember member = GalleryProjectMember.builder()
                .user(user)
                .part(PART)
                .role(ROLE)
                .build();

        UserGeneration userGeneration = mock(UserGeneration.class);

        when(userGeneration.getGeneration())
                .thenReturn(Generation.GEN_25_26);
        when(userGeneration.getPosition())
                .thenReturn(UserPosition.MEMBER);

        when(userGenerationRepository.findByUserAndIsMainTrue(user))
                .thenReturn(userGeneration);

        // when
        List<GalleryProjectMemberResponseDto> result =
                mapper.mapMembersInfo(List.of(member));

        // then
        assertThat(result).hasSize(1);

        GalleryProjectMemberResponseDto dto = result.getFirst();
        assertThat(dto.userId()).isEqualTo(USER_ID);
        assertThat(dto.name()).isEqualTo(USER_NAME);
        assertThat(dto.school()).isEqualTo(SCHOOL);
        assertThat(dto.part()).isEqualTo(PART);
        assertThat(dto.memberRole()).isEqualTo(ROLE);
        assertThat(dto.generationAndPosition())
                .isEqualTo(GENERATION_AND_POSITION);
    }

    @Test
    void User와_UserGeneration을_전시용_DTO로_매핑한다() throws Exception {
        // given
        Constructor<User> constructor = User.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        User user = constructor.newInstance();

        ReflectionTestUtils.setField(user, "id", USER_ID);
        ReflectionTestUtils.setField(user, "name", USER_NAME);
        ReflectionTestUtils.setField(user, "school", SCHOOL);

        UserGeneration userGeneration = mock(UserGeneration.class);
        when(userGeneration.getGeneration()).thenReturn(Generation.GEN_25_26);
        when(userGeneration.getPosition()).thenReturn(UserPosition.MEMBER);

        // when
        TokenUserInfoForProjectBuildingResponseDto dto =
                mapper.mapExhibitor(user, userGeneration);

        // then
        assertThat(dto.userId()).isEqualTo(USER_ID);
        assertThat(dto.name()).isEqualTo(USER_NAME);
        assertThat(dto.school()).isEqualTo(SCHOOL);
        assertThat(dto.generationAndPosition())
                .isEqualTo(GENERATION_AND_POSITION);
    }

}


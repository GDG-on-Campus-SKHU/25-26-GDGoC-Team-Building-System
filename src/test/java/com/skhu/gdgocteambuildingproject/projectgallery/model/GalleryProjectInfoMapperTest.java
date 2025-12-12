package com.skhu.gdgocteambuildingproject.projectgallery.model;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.ServiceStatus;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.*;
import com.skhu.gdgocteambuildingproject.projectgallery.model.mapper.GalleryProjectInfoMapper;
import com.skhu.gdgocteambuildingproject.projectgallery.model.mapper.GalleryProjectMemberMapper;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.util.ReflectionTestUtils;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Constructor;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GalleryProjectInfoMapperTest {

    private static final Long PROJECT_ID = 999L;
    private static final Long LEADER_ID = 123L;

    private static final String PROJECT_NAME = "TestProject";
    private static final String GENERATION = "25-26";
    private static final String SHORT_DESC = "shortDescription";
    private static final String DESCRIPTION = "Description";
    private static final String FILE_URL = "https://example.com/test.png";

    private static final String USER_NAME = "Test";

    private static final ServiceStatus STATUS = ServiceStatus.IN_SERVICE;

    @Mock
    private GalleryProjectMemberMapper memberMapper;

    @InjectMocks
    private GalleryProjectInfoMapper infoMapper;

    @Test
    void GalleryProject_엔티티를_GalleryProjectInfoResponseDto로_매핑한다() throws Exception {
        // given
        Constructor<User> constructor = User.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        User leader = constructor.newInstance();

        ReflectionTestUtils.setField(leader, "id", LEADER_ID);

        GalleryProject project = GalleryProject.builder()
                .projectName(PROJECT_NAME)
                .generation(Generation.fromLabel(GENERATION))
                .shortDescription(SHORT_DESC)
                .serviceStatus(STATUS)
                .description(DESCRIPTION)
                .thumbnailUrl(FILE_URL)
                .user(leader)
                .build();

        ReflectionTestUtils.setField(project, "id", PROJECT_ID);

        List<GalleryProjectMemberResponseDto> mockMembers = List.of(
                GalleryProjectMemberResponseDto.builder().name(USER_NAME).build()
        );

        when(memberMapper.mapMembersInfo(any())).thenReturn(mockMembers);

        // when
        GalleryProjectInfoResponseDto dto = infoMapper.mapToInfo(project);

        // then
        assertThat(dto.galleryProjectId()).isEqualTo(PROJECT_ID);
        assertThat(dto.projectName()).isEqualTo(PROJECT_NAME);
        assertThat(dto.generation()).isEqualTo(GENERATION);
        assertThat(dto.shortDescription()).isEqualTo(SHORT_DESC);
        assertThat(dto.description()).isEqualTo(DESCRIPTION);
        assertThat(dto.serviceStatus()).isEqualTo(STATUS.name());
        assertThat(dto.leaderId()).isEqualTo(LEADER_ID);
        assertThat(dto.thumbnailUrl()).isEqualTo(FILE_URL);
        assertThat(dto.members()).isEqualTo(mockMembers);
    }

    @Test
    void GalleryProject_엔티티를_GalleryProjectSummaryResponseDto로_매핑한다() {
        // given
        GalleryProject project = mock(GalleryProject.class);

        when(project.getId()).thenReturn(PROJECT_ID);
        when(project.getProjectName()).thenReturn(PROJECT_NAME);
        when(project.getGeneration()).thenReturn(Generation.fromLabel(GENERATION));
        when(project.getShortDescription()).thenReturn(SHORT_DESC);
        when(project.getServiceStatus()).thenReturn(STATUS);
        when(project.getThumbnailUrl()).thenReturn(FILE_URL);

        // when
        GalleryProjectSummaryResponseDto dto = infoMapper.mapToSummary(project);

        // then
        assertThat(dto.galleryProjectId()).isEqualTo(PROJECT_ID);
        assertThat(dto.projectName()).isEqualTo(PROJECT_NAME);
        assertThat(dto.generation()).isEqualTo(GENERATION);
        assertThat(dto.shortDescription()).isEqualTo(SHORT_DESC);
        assertThat(dto.serviceStatus()).isEqualTo(STATUS.name());
        assertThat(dto.thumbnailUrl()).isEqualTo(FILE_URL);
    }
}

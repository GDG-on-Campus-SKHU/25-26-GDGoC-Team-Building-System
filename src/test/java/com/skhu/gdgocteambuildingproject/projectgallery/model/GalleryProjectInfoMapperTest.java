package com.skhu.gdgocteambuildingproject.projectgallery.model;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.ServiceStatus;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectMemberResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectSummaryResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.model.mapper.GalleryProjectInfoMapper;
import com.skhu.gdgocteambuildingproject.projectgallery.model.mapper.GalleryProjectMemberMapper;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GalleryProjectInfoMapperTest {

    private static final Long PROJECT_ID = 999L;
    private static final String PROJECT_NAME = "TestProject";
    private static final String GENERATION = "25-26";
    private static final String SHORT_DESC = "shortDescription";
    private static final String DESCRIPTION = "Description";
    private static final String FILE_URL = "https://example.com/test.png";
    private static final ServiceStatus STATUS = ServiceStatus.IN_SERVICE;

    private static final String LEADER_NAME = "Leader";
    private static final String MEMBER_NAME = "Member";

    @Mock
    private GalleryProjectMemberMapper memberMapper;

    @InjectMocks
    private GalleryProjectInfoMapper infoMapper;

    @Test
    void GalleryProject_엔티티를_GalleryProjectInfoResponseDto로_매핑한다() {
        // given
        GalleryProject project = GalleryProject.builder()
                .projectName(PROJECT_NAME)
                .generation(Generation.fromLabel(GENERATION))
                .shortDescription(SHORT_DESC)
                .serviceStatus(STATUS)
                .description(DESCRIPTION)
                .thumbnailUrl(FILE_URL)
                .members(List.of())
                .build();

        ReflectionTestUtils.setField(project, "id", PROJECT_ID);

        GalleryProjectMemberResponseDto leaderDto =
                GalleryProjectMemberResponseDto.builder()
                        .name(LEADER_NAME)
                        .build();

        List<GalleryProjectMemberResponseDto> memberDtos = List.of(
                GalleryProjectMemberResponseDto.builder()
                        .name(MEMBER_NAME)
                        .build()
        );

        when(memberMapper.mapLeaderInfo(any())).thenReturn(leaderDto);
        when(memberMapper.mapMembersInfo(any())).thenReturn(memberDtos);

        // when
        GalleryProjectInfoResponseDto dto = infoMapper.mapToInfo(project);

        // then
        assertThat(dto.galleryProjectId()).isEqualTo(PROJECT_ID);
        assertThat(dto.projectName()).isEqualTo(PROJECT_NAME);
        assertThat(dto.generation()).isEqualTo(GENERATION);
        assertThat(dto.shortDescription()).isEqualTo(SHORT_DESC);
        assertThat(dto.description()).isEqualTo(DESCRIPTION);
        assertThat(dto.serviceStatus()).isEqualTo(STATUS.name());
        assertThat(dto.thumbnailUrl()).isEqualTo(FILE_URL);

        assertThat(dto.leader()).isEqualTo(leaderDto);
        assertThat(dto.members()).isEqualTo(memberDtos);
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

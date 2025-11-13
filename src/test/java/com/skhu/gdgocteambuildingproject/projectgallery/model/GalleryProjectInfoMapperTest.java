package com.skhu.gdgocteambuildingproject.projectgallery.model;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.ServiceStatus;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.GalleryProjectFileInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.GalleryProjectMemberResponseDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GalleryProjectInfoMapperTest {

    private static final Long PROJECT_ID = 999L;
    private static final String PROJECT_NAME = "테스트용 프로젝트";
    private static final String GENERATION = "25-26";
    private static final String SHORT_DESCRIPTION = "박박대대경경";
    private static final ServiceStatus SERVICE_STATUS = ServiceStatus.IN_SERVICE;
    private static final String DESCRIPTION = "안녕하세요 반가워요";

    private static final String MEMBER_NAME = "Alice";
    private static final String MEMBER_PART = "Backend";
    private static final String FILE_URL = "https://example.com/poster.png";

    @Mock
    private GalleryProjectMemberMapper memberMapper;
    @Mock
    private GalleryProjectFileMapper fileMapper;
    @InjectMocks
    private GalleryProjectInfoMapper infoMapper;

    @Test
    void 엔티티를_DTO로_정상_매핑한다() {
        // given
        GalleryProject project = mock(GalleryProject.class);
        when(project.getId()).thenReturn(PROJECT_ID);
        when(project.getProjectName()).thenReturn(PROJECT_NAME);
        when(project.getGeneration()).thenReturn(GENERATION);
        when(project.getShortDescription()).thenReturn(SHORT_DESCRIPTION);
        when(project.getServiceStatus()).thenReturn(SERVICE_STATUS);
        when(project.getDescription()).thenReturn(DESCRIPTION);

        List<GalleryProjectMemberResponseDto> mockMembers = List.of(
                GalleryProjectMemberResponseDto.builder()
                        .name(MEMBER_NAME)
                        .part(MEMBER_PART)
                        .memberRole(null)
                        .build()
        );

        List<GalleryProjectFileInfoResponseDto> mockFiles = List.of(
                GalleryProjectFileInfoResponseDto.builder()
                        .fileUrl(FILE_URL)
                        .build()
        );

        when(memberMapper.map(project.getMembers())).thenReturn(mockMembers);
        when(fileMapper.map(project.getFiles())).thenReturn(mockFiles);

        // when
        GalleryProjectInfoResponseDto result = infoMapper.mapToInfo(project);

        // then
        assertThat(result.galleryProjectId()).isEqualTo(PROJECT_ID);
        assertThat(result.projectName()).isEqualTo(PROJECT_NAME);
        assertThat(result.generation()).isEqualTo(GENERATION);
        assertThat(result.shortDescription()).isEqualTo(SHORT_DESCRIPTION);
        assertThat(result.serviceStatus()).isEqualTo(SERVICE_STATUS.name());
        assertThat(result.description()).isEqualTo(DESCRIPTION);
        assertThat(result.members()).isEqualTo(mockMembers);
        assertThat(result.files()).isEqualTo(mockFiles);

        verify(memberMapper).map(project.getMembers());
        verify(fileMapper).map(project.getFiles());
    }
}


package com.skhu.gdgocteambuildingproject.projectgallery.model;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.ServiceStatus;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectListResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectSummaryResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.model.mapper.GalleryProjectInfoMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GalleryProjectSummaryMapperTest {

    private static final Long PROJECT_ID = 999L;
    private static final String PROJECT_NAME = "테스트용 프로젝트";
    private static final String SHORT_DESCRIPTION = "짧은 설명";
    private static final ServiceStatus SERVICE_STATUS = ServiceStatus.IN_SERVICE;
    private static final String FILE_URL = "https://example.com/poster.png";

    @InjectMocks
    private GalleryProjectInfoMapper infoMapper;

    @Test
    void 엔티티를_Summary_DTO로_정상_매핑한다() {
        // given
        GalleryProject project = mock(GalleryProject.class);
        when(project.getId()).thenReturn(PROJECT_ID);
        when(project.getProjectName()).thenReturn(PROJECT_NAME);
        when(project.getShortDescription()).thenReturn(SHORT_DESCRIPTION);
        when(project.getServiceStatus()).thenReturn(SERVICE_STATUS);
        when(project.getThumbnailUrl()).thenReturn(FILE_URL);

        // when
        GalleryProjectSummaryResponseDto result = infoMapper.mapToSummary(project);

        // then
        assertThat(result.galleryProjectId()).isEqualTo(PROJECT_ID);
        assertThat(result.projectName()).isEqualTo(PROJECT_NAME);
        assertThat(result.shortDescription()).isEqualTo(SHORT_DESCRIPTION);
        assertThat(result.serviceStatus()).isEqualTo(SERVICE_STATUS.name());
        assertThat(result.thumbnailUrl()).isNotNull();
        assertThat(result.thumbnailUrl()).isEqualTo(FILE_URL);
    }

    @Test
    void Summary_DTO_리스트를_ListResponseDto로_정상_매핑한다() {
                GalleryProjectSummaryResponseDto summaryDto = GalleryProjectSummaryResponseDto.builder()
                .galleryProjectId(PROJECT_ID)
                .projectName(PROJECT_NAME)
                .shortDescription(SHORT_DESCRIPTION)
                .serviceStatus(SERVICE_STATUS.name())
                .thumbnailUrl(FILE_URL)
                .build();

        List<GalleryProjectSummaryResponseDto> summaryList = List.of(summaryDto);

        // when
        GalleryProjectListResponseDto result = infoMapper.mapToListDto(summaryList);

        // then
        assertThat(result.galleryProjectSummaryResponseDtoList()).hasSize(1);
        GalleryProjectSummaryResponseDto first = result.galleryProjectSummaryResponseDtoList().getFirst();
        assertThat(first.projectName()).isEqualTo(PROJECT_NAME);
        assertThat(first.thumbnailUrl()).isEqualTo(FILE_URL);
    }
}

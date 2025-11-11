package com.skhu.gdgocteambuildingproject.projectgallery.model;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.ServiceStatus;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.GalleryProjectFileInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.GalleryProjectMemberResponseDto;
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
        when(project.getId()).thenReturn(999L);
        when(project.getProjectName()).thenReturn("테스트용 프로젝트");
        when(project.getGeneration()).thenReturn("25-26");
        when(project.getShortDescription()).thenReturn("박박대대경경");
            when(project.getServiceStatus()).thenReturn(ServiceStatus.IN_SERVICE);
        when(project.getDescription()).thenReturn("안녕하세요 반가워요");

        // mock members & files
        List<GalleryProjectMemberResponseDto> mockMembers = List.of(
                GalleryProjectMemberResponseDto.builder()
                        .name("Alice")
                        .part("Backend")
                        .memberRole(null)
                        .build()
        );
        List<GalleryProjectFileInfoResponseDto> mockFiles = List.of(
                GalleryProjectFileInfoResponseDto.builder()
                        .fileUrl("https://example.com/poster.png")
                        .build()
        );

        when(memberMapper.map(project.getMembers())).thenReturn(mockMembers);
        when(fileMapper.map(project.getFiles())).thenReturn(mockFiles);

        // when
        GalleryProjectInfoResponseDto result = infoMapper.map(project);

        // then
        assertThat(result.galleryProjectId()).isEqualTo(999L);
        assertThat(result.projectName()).isEqualTo("테스트용 프로젝트");
        assertThat(result.generation()).isEqualTo("25-26");
        assertThat(result.shortDescription()).isEqualTo("박박대대경경");
        assertThat(result.serviceStatus()).isEqualTo(ServiceStatus.IN_SERVICE);
        assertThat(result.description()).isEqualTo("안녕하세요 반가워요");
        assertThat(result.members()).isEqualTo(mockMembers);
        assertThat(result.files()).isEqualTo(mockFiles);

        verify(memberMapper).map(project.getMembers());
        verify(fileMapper).map(project.getFiles());
    }
}

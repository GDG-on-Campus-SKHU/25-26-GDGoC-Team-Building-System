package com.skhu.gdgocteambuildingproject.projectgallery.model;

import com.skhu.gdgocteambuildingproject.global.aws.domain.File;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectFile;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectFileInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.model.mapper.GalleryProjectFileMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GalleryProjectFileMapperTest {

    private static final String FILE_URL = "https://example.com/test.png";

    @Mock
    private GalleryProjectFileMapper mapper;

    @Test
    void File_엔티티를_GalleryProjectFileInfoResponseDto로_매핑한다() throws Exception {
        // given
        File mockFile = mock(File.class);

        when(mockFile.getUrl()).thenReturn(FILE_URL);

        GalleryProjectFile galleryProjectFile = GalleryProjectFile.builder()
                .file(mockFile)
                .build();

        // when
        List<GalleryProjectFileInfoResponseDto> result = mapper.map(List.of(galleryProjectFile));

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().fileUrl()).isEqualTo(FILE_URL);
    }
}

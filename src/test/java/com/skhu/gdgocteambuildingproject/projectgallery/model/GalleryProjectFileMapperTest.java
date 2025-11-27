package com.skhu.gdgocteambuildingproject.projectgallery.model.mapper;

import com.skhu.gdgocteambuildingproject.global.aws.domain.File;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProjectFile;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.res.GalleryProjectFileInfoResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.lang.reflect.Constructor;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GalleryProjectFileMapperTest {

    private static final String FILE_URL = "https://example.com/test.png";

    private final GalleryProjectFileMapper mapper = new GalleryProjectFileMapper();

    @Test
    void File_엔티티를_GalleryProjectFileInfoResponseDto로_매핑한다() throws Exception {
        // given
        Constructor<File> constructor = File.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        File file = constructor.newInstance();

        ReflectionTestUtils.setField(file, "url", FILE_URL);

        GalleryProjectFile galleryProjectFile = GalleryProjectFile.builder()
                .file(file)
                .build();

        // when
        List<GalleryProjectFileInfoResponseDto> result = mapper.map(List.of(galleryProjectFile));

        // then
        assertThat(result).hasSize(1);
        assertThat(result.getFirst().fileUrl()).isEqualTo(FILE_URL);
    }
}

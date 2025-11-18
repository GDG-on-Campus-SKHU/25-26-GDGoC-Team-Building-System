package com.skhu.gdgocteambuildingproject.projectgallery.model;

import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.GalleryProject;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.ServiceStatus;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.create.GalleryProjectMemberResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.info.GalleryProjectFileInfoResponseDto;
import com.skhu.gdgocteambuildingproject.projectgallery.dto.project.info.GalleryProjectInfoResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
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
    private static final Long LEADER_ID = 123L;

    private static final String PROJECT_NAME = "테스트용 프로젝트";
    private static final String GENERATION = "25-26";
    private static final String SHORT_DESCRIPTION = "박박대대경경";
    private static final String DESCRIPTION = "안녕하세요 반가워요";

    private static final ServiceStatus SERVICE_STATUS = ServiceStatus.IN_SERVICE;

    private static final String MEMBER_NAME = "Alice";
    private static final Part MEMBER_PART = Part.BACKEND;

    private static final String FILE_URL = "https://example.com/poster.png";

    @Mock
    private GalleryProjectMemberMapper memberMapper;

    @Mock
    private GalleryProjectFileMapper fileMapper;

    @InjectMocks
    private GalleryProjectInfoMapper infoMapper;

    @Test
    void 엔티티를_DTO로_정상_매핑한다() {
        // GIVEN
        User leader = mock(User.class);
        when(leader.getId()).thenReturn(LEADER_ID);

        GalleryProject project = GalleryProject.builder()
                .projectName(PROJECT_NAME)
                .generation(GENERATION)
                .shortDescription(SHORT_DESCRIPTION)
                .serviceStatus(SERVICE_STATUS)
                .description(DESCRIPTION)
                .user(leader)
                .build();

        ReflectionTestUtils.setField(project, "id", PROJECT_ID);

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

        when(memberMapper.mapMembersInfo(project.getMembers())).thenReturn(mockMembers);
        when(fileMapper.map(project.getFiles())).thenReturn(mockFiles);

        // WHEN
        GalleryProjectInfoResponseDto result = infoMapper.mapToInfo(project);

        // THEN — 값 검증
        assertThat(result.galleryProjectId()).isEqualTo(PROJECT_ID);
        assertThat(result.projectName()).isEqualTo(PROJECT_NAME);
        assertThat(result.generation()).isEqualTo(GENERATION);
        assertThat(result.shortDescription()).isEqualTo(SHORT_DESCRIPTION);
        assertThat(result.serviceStatus()).isEqualTo(SERVICE_STATUS.name());
        assertThat(result.description()).isEqualTo(DESCRIPTION);

        assertThat(result.leaderId()).isEqualTo(LEADER_ID);

        assertThat(result.members()).isEqualTo(mockMembers);
        assertThat(result.files()).isEqualTo(mockFiles);

        // verify
        verify(memberMapper).mapMembersInfo(project.getMembers());
        verify(fileMapper).map(project.getFiles());
    }
}

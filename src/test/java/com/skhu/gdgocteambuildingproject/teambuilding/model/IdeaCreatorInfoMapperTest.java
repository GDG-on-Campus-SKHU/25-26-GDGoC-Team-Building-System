package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static org.mockito.Mockito.when;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaCreatorInfoResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdeaCreatorInfoMapperTest {

    private static final String CREATOR_NAME = "creatorName";
    private static final Part PART = Part.BACKEND;
    private static final String SCHOOL = "Hello University";

    private final IdeaCreatorInfoMapper ideaCreatorInfoMapper = new IdeaCreatorInfoMapper();

    @Mock
    private Idea idea;
    @Mock
    private User creator;

    @Test
    void 엔티티의_정보를_DTO로_매핑한다() {
        // given
        when(idea.getCreator()).thenReturn(creator);
        when(idea.getCreatorPart()).thenReturn(PART);
        when(creator.getName()).thenReturn(CREATOR_NAME);
        when(creator.getSchool()).thenReturn(SCHOOL);

        // when
        IdeaCreatorInfoResponseDto dto = ideaCreatorInfoMapper.map(idea);

        // then
        Assertions.assertThat(dto.creatorName()).isEqualTo(CREATOR_NAME);
        Assertions.assertThat(dto.part()).isEqualTo(PART);
        Assertions.assertThat(dto.school()).isEqualTo(SCHOOL);
    }
}

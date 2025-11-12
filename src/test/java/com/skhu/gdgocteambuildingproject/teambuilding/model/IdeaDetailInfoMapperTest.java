package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaCreatorInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaDetailInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaMemberCompositionResponseDto;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdeaDetailInfoMapperTest {
    private static final long IDEA_ID = 90L;
    private static final String TITLE = "title";
    private static final String INTRODUCTION = "introduction";
    private static final String DESCRIPTION = "description";
    @Mock
    private List<IdeaMemberCompositionResponseDto> COMPOSITIONS_DTOS;
    @Mock
    private IdeaCreatorInfoResponseDto CREATOR_DTO;
    @Mock
    private User CREATOR;

    @Mock
    private Idea idea;
    @Mock
    private IdeaCreatorInfoMapper creatorInfoMapper;
    @Mock
    private IdeaMemberCompositionMapper compositionMapper;

    @InjectMocks
    private IdeaDetailInfoMapper ideaDetailInfoMapper;

    @Test
    void 엔티티의_정보를_DTO로_매핑한다() {
        // when
        when(idea.getId()).thenReturn(IDEA_ID);
        when(idea.getTitle()).thenReturn(TITLE);
        when(idea.getIntroduction()).thenReturn(INTRODUCTION);
        when(idea.getDescription()).thenReturn(DESCRIPTION);
        when(idea.getCreator()).thenReturn(CREATOR);
        when(creatorInfoMapper.map(CREATOR)).thenReturn(CREATOR_DTO);
        when(compositionMapper.map(idea)).thenReturn(COMPOSITIONS_DTOS);

        // when
        IdeaDetailInfoResponseDto dto = ideaDetailInfoMapper.map(idea);

        // then
        assertThat(dto.ideaId()).isEqualTo(IDEA_ID);
        assertThat(dto.title()).isEqualTo(TITLE);
        assertThat(dto.introduction()).isEqualTo(INTRODUCTION);
        assertThat(dto.description()).isEqualTo(DESCRIPTION);
        assertThat(dto.creator()).isEqualTo(CREATOR_DTO);
        assertThat(dto.compositions()).isEqualTo(COMPOSITIONS_DTOS);
    }
}

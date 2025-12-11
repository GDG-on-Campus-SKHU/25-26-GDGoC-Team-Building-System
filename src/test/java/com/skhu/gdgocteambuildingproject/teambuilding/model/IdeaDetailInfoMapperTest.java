package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.skhu.gdgocteambuildingproject.teambuilding.domain.Idea;
import com.skhu.gdgocteambuildingproject.admin.dto.idea.AdminIdeaDetailResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.domain.ProjectTopic;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaCreatorInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaDetailInfoResponseDto;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaMemberCompositionResponseDto;
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
    private static final long TOPIC_ID = 1L;
    private static final String TOPIC = "topic";
    @Mock
    private List<IdeaMemberCompositionResponseDto> COMPOSITIONS_DTOS;
    @Mock
    private IdeaCreatorInfoResponseDto CREATOR_DTO;

    @Mock
    private Idea idea;
    @Mock
    private ProjectTopic topic;
    @Mock
    private IdeaCreatorInfoMapper creatorInfoMapper;
    @Mock
    private IdeaMemberCompositionMapper compositionMapper;

    @InjectMocks
    private IdeaDetailInfoMapper ideaDetailInfoMapper;

    @Test
    void 엔티티의_정보를_DTO로_매핑한다() {
        // given
        when(idea.getId()).thenReturn(IDEA_ID);
        when(idea.getTitle()).thenReturn(TITLE);
        when(idea.getIntroduction()).thenReturn(INTRODUCTION);
        when(idea.getDescription()).thenReturn(DESCRIPTION);
        when(idea.getTopic()).thenReturn(topic);
        when(topic.getId()).thenReturn(TOPIC_ID);
        when(topic.getTopic()).thenReturn(TOPIC);
        when(creatorInfoMapper.map(idea)).thenReturn(CREATOR_DTO);
        when(compositionMapper.map(idea)).thenReturn(COMPOSITIONS_DTOS);

        // when
        IdeaDetailInfoResponseDto dto = ideaDetailInfoMapper.map(idea);

        // then
        assertThat(dto.ideaId()).isEqualTo(IDEA_ID);
        assertThat(dto.title()).isEqualTo(TITLE);
        assertThat(dto.introduction()).isEqualTo(INTRODUCTION);
        assertThat(dto.description()).isEqualTo(DESCRIPTION);
        assertThat(dto.topicId()).isEqualTo(TOPIC_ID);
        assertThat(dto.topic()).isEqualTo(TOPIC);
        assertThat(dto.creator()).isEqualTo(CREATOR_DTO);
        assertThat(dto.compositions()).isEqualTo(COMPOSITIONS_DTOS);
    }

    @Test
    void 관리자용_엔티티의_정보를_DTO로_매핑한다() {
        // given
        when(idea.getId()).thenReturn(IDEA_ID);
        when(idea.getTitle()).thenReturn(TITLE);
        when(idea.getIntroduction()).thenReturn(INTRODUCTION);
        when(idea.getDescription()).thenReturn(DESCRIPTION);
        when(idea.getTopic()).thenReturn(topic);
        when(topic.getId()).thenReturn(TOPIC_ID);
        when(topic.getTopic()).thenReturn(TOPIC);
        when(idea.isDeleted()).thenReturn(false);
        when(creatorInfoMapper.map(idea)).thenReturn(CREATOR_DTO);
        when(compositionMapper.map(idea)).thenReturn(COMPOSITIONS_DTOS);

        // when
        AdminIdeaDetailResponseDto dto = ideaDetailInfoMapper.mapForAdmin(idea);

        // then
        assertThat(dto.ideaId()).isEqualTo(IDEA_ID);
        assertThat(dto.title()).isEqualTo(TITLE);
        assertThat(dto.introduction()).isEqualTo(INTRODUCTION);
        assertThat(dto.description()).isEqualTo(DESCRIPTION);
        assertThat(dto.topicId()).isEqualTo(TOPIC_ID);
        assertThat(dto.topic()).isEqualTo(TOPIC);
        assertThat(dto.creator()).isEqualTo(CREATOR_DTO);
        assertThat(dto.compositions()).isEqualTo(COMPOSITIONS_DTOS);
        assertThat(dto.deleted()).isFalse();
    }
}

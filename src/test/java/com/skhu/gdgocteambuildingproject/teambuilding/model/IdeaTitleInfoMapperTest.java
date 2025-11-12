package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaTitleInfoResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdeaTitleInfoMapperTest {
    private static final long EXPECTED_ID = 99L;
    private static final String EXPECTED_TITLE = "title";
    private static final String EXPECTED_INTRODUCTION = "introduction";
    private static final int EXPECTED_CURRENT_MEMBER_COUNT = 5;
    private static final int EXPECTED_MAX_MEMBER_COUNT = 10;

    private final IdeaTitleInfoMapper ideaTitleInfoMapper = new IdeaTitleInfoMapper();

    @Mock
    private Idea idea;

    @BeforeEach
    void mock() {
        when(idea.getId()).thenReturn(EXPECTED_ID);
        when(idea.getTitle()).thenReturn(EXPECTED_TITLE);
        when(idea.getIntroduction()).thenReturn(EXPECTED_INTRODUCTION);
        when(idea.getCurrentMemberCount()).thenReturn(EXPECTED_CURRENT_MEMBER_COUNT);
        when(idea.getMaxMemberCount()).thenReturn(EXPECTED_MAX_MEMBER_COUNT);
    }

    @Test
    void 엔티티의_정보를_DTO로_매핑한다() {
        IdeaTitleInfoResponseDto dto = ideaTitleInfoMapper.map(idea);

        assertThat(dto.ideaId()).isEqualTo(EXPECTED_ID);
        assertThat(dto.title()).isEqualTo(EXPECTED_TITLE);
        assertThat(dto.introduction()).isEqualTo(EXPECTED_INTRODUCTION);
        assertThat(dto.currentMemberCount()).isEqualTo(EXPECTED_CURRENT_MEMBER_COUNT);
        assertThat(dto.maxMemberCount()).isEqualTo(EXPECTED_MAX_MEMBER_COUNT);
    }
}

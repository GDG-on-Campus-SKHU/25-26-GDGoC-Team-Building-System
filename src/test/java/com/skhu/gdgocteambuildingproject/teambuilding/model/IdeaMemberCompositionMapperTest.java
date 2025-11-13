package com.skhu.gdgocteambuildingproject.teambuilding.model;

import static com.skhu.gdgocteambuildingproject.global.enumtype.Part.AI;
import static com.skhu.gdgocteambuildingproject.global.enumtype.Part.BACKEND;
import static com.skhu.gdgocteambuildingproject.global.enumtype.Part.DESIGN;
import static com.skhu.gdgocteambuildingproject.global.enumtype.Part.MOBILE;
import static com.skhu.gdgocteambuildingproject.global.enumtype.Part.PM;
import static com.skhu.gdgocteambuildingproject.global.enumtype.Part.WEB;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import com.skhu.gdgocteambuildingproject.Idea.domain.Idea;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.teambuilding.dto.response.IdeaMemberCompositionResponseDto;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class IdeaMemberCompositionMapperTest {
    private static final List<Part> PARTS = List.of(PM, DESIGN, WEB, MOBILE, BACKEND, AI);

    private static final Map<Part, Integer> EXPECTED_MAX_COUNTS = Map.of(
            PM, 10,
            DESIGN, 20,
            WEB, 30,
            MOBILE, 40,
            BACKEND, 50,
            AI, 60
    );
    private static final Map<Part, Integer> EXPECTED_CURRENT_COUNTS = Map.of(
            PM, 1,
            DESIGN, 2,
            WEB, 3,
            MOBILE, 4,
            BACKEND, 5,
            AI, 6
    );

    private final IdeaMemberCompositionMapper ideaMemberCompositionMapper = new IdeaMemberCompositionMapper();

    @Mock
    private Idea idea;

    @BeforeEach
    void mock() {
        when(idea.getMaxMemberCountsByPart()).thenReturn(EXPECTED_MAX_COUNTS);
        when(idea.getCurrentMemberCountsByPart()).thenReturn(EXPECTED_CURRENT_COUNTS);
    }

    @Test
    void 아이디어의_파트별_최대_인원수와_현재_인원수를_DTO로_매핑한다() {
        List<IdeaMemberCompositionResponseDto> dtos = ideaMemberCompositionMapper.map(idea);

        for (Part part : PARTS) {
            IdeaMemberCompositionResponseDto result = findDtoByPart(dtos, part);
            assertThat(result.maxCount()).isEqualTo(EXPECTED_MAX_COUNTS.get(part));
            assertThat(result.currentCount()).isEqualTo(EXPECTED_CURRENT_COUNTS.get(part));
        }
    }

    private IdeaMemberCompositionResponseDto findDtoByPart(
            List<IdeaMemberCompositionResponseDto> dtos,
            Part part
    ) {
        return dtos.stream()
                .filter(dto -> dto.part().equals(part))
                .findFirst()
                .orElseThrow();
    }
}

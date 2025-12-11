package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.ApprovedUserGenerationResponseDto;
import com.skhu.gdgocteambuildingproject.admin.dto.UserResponseDto;
import com.skhu.gdgocteambuildingproject.admin.util.GenerationMapper;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class UserInfoMapperTest {

    @InjectMocks
    private UserInfoMapper userInfoMapper;

    @Mock
    private GenerationMapper generationMapper;

    private static final String TEST_USER_NAME = "테스트유저";
    private static final Part TEST_USER_PART = Part.BACKEND;
    private static final String TEST_USER_GENERATION = "25-26";
    private static final String TEST_USER_SCHOOL = "성공회대학교";
    private static final String TEST_USER_EMAIL = "test@example.com";
    private static final String TEST_USER_NUMBER = "010-1234-5678";
    private static final String PASSWORD = "123456";
    private static final ApprovalStatus TEST_USER_STATUS = ApprovalStatus.WAITING;
    private static final String positionEnum = "CORE";


    @Test
    void User_엔티티를_UserResponseDto로_성공적으로_변환한다() {

        //given
        User testUser = User.builder()
                .name(TEST_USER_NAME)
                .part(TEST_USER_PART)
                .school(TEST_USER_SCHOOL)
                .email(TEST_USER_EMAIL)
                .number(TEST_USER_NUMBER)
                .role(UserRole.SKHU_MEMBER)
                .password(PASSWORD)
                .build();

        ApprovedUserGenerationResponseDto dto = new ApprovedUserGenerationResponseDto(
                1L,
                TEST_USER_GENERATION,
                positionEnum,
                true
        );

        List<ApprovedUserGenerationResponseDto> expectedGenerations = List.of(dto);
        given(generationMapper.toMainGenerationDtos(testUser)).willReturn(expectedGenerations);

        // when
        UserResponseDto responseDto = userInfoMapper.toUserResponseDto(testUser);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.userName()).isEqualTo(TEST_USER_NAME);
        assertThat(responseDto.part()).isEqualTo(TEST_USER_PART);
        assertThat(responseDto.school()).isEqualTo(TEST_USER_SCHOOL);
        assertThat(responseDto.email()).isEqualTo(TEST_USER_EMAIL);
        assertThat(responseDto.number()).isEqualTo(TEST_USER_NUMBER);
        assertThat(responseDto.approvalStatus()).isEqualTo(TEST_USER_STATUS);

        assertThat(responseDto.generations()).containsExactlyElementsOf(expectedGenerations);
    }
}

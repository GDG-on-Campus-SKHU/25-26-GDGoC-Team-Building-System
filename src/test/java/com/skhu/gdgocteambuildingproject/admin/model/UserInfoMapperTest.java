package com.skhu.gdgocteambuildingproject.admin.model;

import com.skhu.gdgocteambuildingproject.admin.dto.UserResponseDto;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.ApprovalStatus;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserPosition;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import com.skhu.gdgocteambuildingproject.user.domain.User;

class UserInfoMapperTest {

    private static final String TEST_USER_NAME = "테스트유저";
    private static final Part TEST_USER_PART = Part.BACKEND;
    private static final String TEST_USER_GENERATION = "25-26";
    private static final String TEST_USER_SCHOOL = "성공회대학교";
    private static final String TEST_USER_EMAIL = "test@example.com";
    private static final String TEST_USER_NUMBER = "010-1234-5678";
    private static final String INTRODUCTION = "방가방가합니다";
    private static final String PASSWORD = "123456";
    private static final ApprovalStatus TEST_USER_STATUS = ApprovalStatus.WAITING;

    private UserInfoMapper userInfoMapper;

    @BeforeEach
    void setUp() {
        userInfoMapper = new UserInfoMapper();
    }

    @Test
    void User_엔티티를_UserResponseDto로_성공적으로_변환한다() {

        // given
        User testUser = User.builder()
                .name(TEST_USER_NAME)
                .part(TEST_USER_PART)
                .generation(TEST_USER_GENERATION)
                .school(TEST_USER_SCHOOL)
                .email(TEST_USER_EMAIL)
                .number(TEST_USER_NUMBER)
                .position(UserPosition.CORE)
                .role(UserRole.SKHU_ADMIN)
                .introduction(INTRODUCTION)
                .password(PASSWORD)
                .build();

        // when
        UserResponseDto responseDto = userInfoMapper.toUserResponseDto(testUser);

        // then
        assertThat(responseDto).isNotNull();
        assertThat(responseDto.userName()).isEqualTo(TEST_USER_NAME);
        assertThat(responseDto.part()).isEqualTo(TEST_USER_PART);
        assertThat(responseDto.generation()).isEqualTo(TEST_USER_GENERATION);
        assertThat(responseDto.school()).isEqualTo(TEST_USER_SCHOOL);
        assertThat(responseDto.approvalStatus()).isEqualTo(TEST_USER_STATUS);
        assertThat(responseDto.email()).isEqualTo(TEST_USER_EMAIL);
        assertThat(responseDto.number()).isEqualTo(TEST_USER_NUMBER);
    }
}

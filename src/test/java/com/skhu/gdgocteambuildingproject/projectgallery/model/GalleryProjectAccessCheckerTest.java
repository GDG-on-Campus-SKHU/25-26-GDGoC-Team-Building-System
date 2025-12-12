package com.skhu.gdgocteambuildingproject.projectgallery.model;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import com.skhu.gdgocteambuildingproject.projectgallery.model.security.GalleryProjectAccessChecker;
import com.skhu.gdgocteambuildingproject.projectgallery.repository.GalleryProjectMemberRepository;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GalleryProjectAccessCheckerTest {

    private static final Long PROJECT_ID = 999L;
    private static final Long USER_ID = 123L;

    private static final UserRole ADMIN_ROLE = UserRole.ROLE_SKHU_ADMIN;
    private static final UserRole MEMBER_ROLE = UserRole.ROLE_SKHU_MEMBER;
    private static final MemberRole LEADER_ROLE = MemberRole.LEADER;

    @Mock
    private GalleryProjectMemberRepository repository;

    @InjectMocks
    private GalleryProjectAccessChecker checker;

    @Test
    void User_엔티티의_Admin_권한을_체크한다() throws Exception {
        // given
        User mockUser = mock(User.class);

        when(mockUser.getRole()).thenReturn(ADMIN_ROLE);

        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUser, null, null);

        // when
        boolean result = checker.checkLeaderOrAdminPermission(PROJECT_ID, authentication);

        // then
        assertThat(result).isTrue();
        verifyNoInteractions(repository);
    }

    @Test
    void User_엔티티가_프로젝트_리더인지_체크한다() throws Exception {
        // given
        User mockUser = mock(User.class);

        when(mockUser.getId()).thenReturn(USER_ID);
        when(mockUser.getRole()).thenReturn(MEMBER_ROLE);

        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUser, null, null);

        when(repository.existsByProjectIdAndUserIdAndRole(PROJECT_ID, USER_ID, LEADER_ROLE))
                .thenReturn(true);

        // when
        boolean result = checker.checkLeaderOrAdminPermission(PROJECT_ID, authentication);

        // then
        assertThat(result).isTrue();
    }

    @Test
    void User_엔티티가_프로젝트의_리더가_아니면_거부한다() throws Exception {
        // given
        User mockUser = mock(User.class);

        when(mockUser.getId()).thenReturn(USER_ID);
        when(mockUser.getRole()).thenReturn(MEMBER_ROLE);

        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUser, null, null);

        when(repository.existsByProjectIdAndUserIdAndRole(PROJECT_ID, USER_ID, LEADER_ROLE))
                .thenReturn(false);

        // when
        boolean result = checker.checkLeaderOrAdminPermission(PROJECT_ID, authentication);

        // then
        assertThat(result).isFalse();
    }

    @ParameterizedTest
    @EnumSource(value = UserRole.class, names = "OTHERS")
    void User_엔티티의_권한이_OTHERS_인지_검증한다(UserRole role) throws Exception {
        // given
        User mockUser = mock(User.class);

        when(mockUser.getRole()).thenReturn(role);

        Authentication authentication = new UsernamePasswordAuthenticationToken(mockUser, null, null);

        // when
        boolean result = checker.checkLeaderOrAdminPermission(PROJECT_ID, authentication);

        // then
        assertThat(result).isFalse();
        verifyNoInteractions(repository);
    }
}

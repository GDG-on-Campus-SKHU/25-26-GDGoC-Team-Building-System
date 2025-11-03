package com.skhu.gdgocteambuildingproject.projectgallery.domain;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.MemberRole;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GalleryProjectMember {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String part;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    private GalleryProject project;
}
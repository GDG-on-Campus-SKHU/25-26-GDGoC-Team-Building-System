package com.skhu.gdgocteambuildingproject.projectgallery.domain;

import com.skhu.gdgocteambuildingproject.projectgallery.domain.enumtype.ServiceStatus;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GalleryProject {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String generation;
    private String shortDescription;

    @Enumerated(EnumType.STRING)
    private ServiceStatus serviceStatus;

    @Lob
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GalleryProjectMember> members = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GalleryProjectFile> files = new ArrayList<>();
}
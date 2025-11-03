package com.skhu.gdgocteambuildingproject.community.domain;

import com.skhu.gdgocteambuildingproject.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Recruitment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private RecruitmentProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
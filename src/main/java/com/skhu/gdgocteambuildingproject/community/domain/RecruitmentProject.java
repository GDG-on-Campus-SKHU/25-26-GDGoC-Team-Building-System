package com.skhu.gdgocteambuildingproject.community.domain;


import com.skhu.gdgocteambuildingproject.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentProject {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    @Lob
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
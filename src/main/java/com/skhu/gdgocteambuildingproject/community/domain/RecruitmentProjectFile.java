package com.skhu.gdgocteambuildingproject.community.domain;

import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.projectgallery.domain.File;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RecruitmentProjectFile extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    private File file;

    @ManyToOne(fetch = FetchType.LAZY)
    private RecruitmentProject project;
}
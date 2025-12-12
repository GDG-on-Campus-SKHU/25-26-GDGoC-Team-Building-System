package com.skhu.gdgocteambuildingproject.activity.domain;

import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Activity extends BaseEntity {

    @Column(length = 20)
    private String title;

    @Column(length = 5)
    private String speaker;
    private Generation generation;
    private String videoUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "activity_category_id")
    private ActivityCategory activityCategory;

    @Builder
    public Activity(String title, String speaker, Generation generation,
                    String videoUrl, ActivityCategory activityCategory) {
        this.title = title;
        this.speaker = speaker;
        this.generation = generation;
        this.videoUrl = videoUrl;
        this.activityCategory = activityCategory;
    }

    public void update(String title, String speaker,
                       Generation generation, String videoUrl) {
        this.title = title;
        this.speaker = speaker;
        this.generation = generation;
        this.videoUrl = videoUrl;
    }
}

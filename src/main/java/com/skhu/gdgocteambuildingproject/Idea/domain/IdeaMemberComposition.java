package com.skhu.gdgocteambuildingproject.Idea.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdeaMemberComposition {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String part;
    private int count;

    @ManyToOne(fetch = FetchType.LAZY)
    private Idea idea;
}
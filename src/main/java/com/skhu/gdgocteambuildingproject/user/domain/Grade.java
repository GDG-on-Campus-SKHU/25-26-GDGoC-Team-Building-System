package com.skhu.gdgocteambuildingproject.user.domain;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Grade {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
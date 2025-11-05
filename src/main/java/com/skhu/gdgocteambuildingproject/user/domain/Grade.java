package com.skhu.gdgocteambuildingproject.user.domain;


import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Grade extends BaseEntity{

    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;
}
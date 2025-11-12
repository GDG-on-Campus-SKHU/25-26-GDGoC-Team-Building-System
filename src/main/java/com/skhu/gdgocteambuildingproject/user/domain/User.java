package com.skhu.gdgocteambuildingproject.user.domain;

import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserPosition;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserRole;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user")
public class User extends BaseEntity {

    private String email;
    private String password;

    private String name;
    private String number;
    private String introduction;
    private String school;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private UserPosition position;

    @Enumerated(EnumType.STRING)
    private Part part;
    private String generation;

    private boolean deleted;
    private String refreshToken;

    // 승인 여부
    @Column(nullable = false)
    private boolean approved = false;

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TechStack> techStacks = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Grade> grades = new ArrayList<>();

    public User(String email, String password, String name, String number,
                String introduction, String school, UserRole role, UserPosition position,
                String part, String generation) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.number = number;
        this.introduction = introduction;
        this.school = school;
        this.role = role;
        this.position = position;
        this.part = Part.valueOf(part);
        this.generation = generation;
        this.approved = true;
    }

    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

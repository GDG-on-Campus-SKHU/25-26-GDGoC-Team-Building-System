package com.skhu.gdgocteambuildingproject.global.email.domain;

import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class EmailVerification extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String code;

    @Column(nullable = false)
    private boolean verified = false;

    public EmailVerification(String email, String code) {
        this.email = email;
        this.code = code;
        this.verified = false;
    }

    public void markVerified() {
        this.verified = true;
    }
}
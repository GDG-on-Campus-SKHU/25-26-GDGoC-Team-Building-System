package com.skhu.gdgocteambuildingproject.auth.domain;

import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 300)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiredAt;

    private RefreshToken(User user, String token, LocalDateTime expiredAt) {
        this.user = user;
        this.token = token;
        this.expiredAt = expiredAt;
    }

    public static RefreshToken of(User user, String token, LocalDateTime expiredAt) {
        return new RefreshToken(user, token, expiredAt);
    }
}

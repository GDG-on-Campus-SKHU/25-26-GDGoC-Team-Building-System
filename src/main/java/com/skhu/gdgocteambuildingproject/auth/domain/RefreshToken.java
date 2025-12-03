package com.skhu.gdgocteambuildingproject.auth.domain;

import com.skhu.gdgocteambuildingproject.user.domain.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false, unique = true, length = 300)
    private String token;

    private RefreshToken(User user, String token) {
        this.user = user;
        this.token = token;
    }

    public static RefreshToken of(User user, String token) {
        return new RefreshToken(user, token);
    }
}

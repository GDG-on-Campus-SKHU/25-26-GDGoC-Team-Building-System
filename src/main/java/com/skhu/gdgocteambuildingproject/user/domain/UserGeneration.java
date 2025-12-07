package com.skhu.gdgocteambuildingproject.user.domain;

import com.skhu.gdgocteambuildingproject.user.domain.enumtype.Generation;
import com.skhu.gdgocteambuildingproject.user.domain.enumtype.UserPosition;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGeneration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Generation generation;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserPosition position;

    @Column(nullable = false)
    private boolean isMain;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Builder
    public UserGeneration(Generation generation, UserPosition position,
                          User user, boolean isMain) {
        this.generation = generation;
        this.position = position;
        this.isMain = isMain;
        this.user = user;
    }

    public void updateGeneration(Generation generation) {
        this.generation = generation;
    }

    public void updatePosition(UserPosition position) {
        this.position = position;
    }

    public void updateMain(boolean main) {
        this.isMain = main;
    }
}

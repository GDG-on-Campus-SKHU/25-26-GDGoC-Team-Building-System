package com.skhu.gdgocteambuildingproject.Idea.domain;

import com.skhu.gdgocteambuildingproject.Idea.domain.enumtype.IdeaMemberRole;
import com.skhu.gdgocteambuildingproject.global.enumtype.Part;
import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import com.skhu.gdgocteambuildingproject.user.domain.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "UK_IDEA_USER",
                        columnNames = {"idea_id", "user_id"}
                )
        }
)
public class IdeaMember extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Idea idea;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @Setter
    private Part part;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private IdeaMemberRole role;

    public boolean isCreator() {
        return role == IdeaMemberRole.CREATOR;
    }

    public boolean isMember() {
        return role == IdeaMemberRole.MEMBER;
    }
}

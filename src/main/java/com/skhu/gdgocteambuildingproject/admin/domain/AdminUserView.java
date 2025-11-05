package com.skhu.gdgocteambuildingproject.admin.domain;

import com.skhu.gdgocteambuildingproject.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdminUserView extends BaseEntity {

    private String email;
    private String name;
    private String part;
    private String role;
}
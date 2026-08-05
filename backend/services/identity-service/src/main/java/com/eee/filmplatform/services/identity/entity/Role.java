package com.eee.filmplatform.services.identity.entity;

import java.util.HashSet;

import com.eee.filmplatform.services.identity.enums.RoleName;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Getter
@Builder
@Entity
@Table(name = "roles")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Role extends BaseEntity {

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private RoleName name;

    @Column(length = 255)
    private String description;

    @Builder.Default
    @Column(nullable = false)
    private boolean system = true;

    @OneToMany(
        mappedBy = "role",
        fetch = FetchType.LAZY)
    private Set<UserRole> userRoles = new HashSet<>();
}
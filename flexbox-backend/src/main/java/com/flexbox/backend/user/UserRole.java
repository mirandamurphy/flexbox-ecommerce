package com.flexbox.backend.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_role", schema = "public", uniqueConstraints = {@UniqueConstraint(name = "user_role_user_id_key",
        columnNames = {"user_id"})})
public class UserRole {
    @EmbeddedId
    private UserRoleId id;

    @MapsId("userId")
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @MapsId("roleId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;


}
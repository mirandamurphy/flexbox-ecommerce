package com.flexbox.backend.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Getter
@Setter
@Entity
@Table(name = "role", schema = "public")
public class Role {
    @Id
    @Column(name = "role_id", nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(columnDefinition = "role_name")
    private RoleName name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;


}
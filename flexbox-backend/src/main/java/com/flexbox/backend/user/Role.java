package com.flexbox.backend.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "role", schema = "public", uniqueConstraints = {@UniqueConstraint(name = "role_name_key",
        columnNames = {"name"})})
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id", nullable = false)
    private Long id;

    @Column(name = "name", columnDefinition = "role_name not null")
    private Object name;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;


}
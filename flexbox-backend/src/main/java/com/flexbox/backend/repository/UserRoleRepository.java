package com.flexbox.backend.repository;

import com.flexbox.backend.user.UserRole;
import com.flexbox.backend.user.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, UserRoleId> {

}

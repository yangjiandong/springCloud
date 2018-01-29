package org.ssh.cloud.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssh.cloud.auth.entity.Role;

public interface RoleDao extends JpaRepository<Role, Long> {
}

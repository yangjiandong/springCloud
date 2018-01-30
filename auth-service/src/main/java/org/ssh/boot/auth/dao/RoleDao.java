package org.ssh.boot.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssh.boot.auth.entity.Role;

public interface RoleDao extends JpaRepository<Role, Long> {
}

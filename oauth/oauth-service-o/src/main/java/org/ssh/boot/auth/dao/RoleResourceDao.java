package org.ssh.boot.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssh.boot.auth.entity.RoleResource;

public interface RoleResourceDao extends JpaRepository<RoleResource, Long> {

}

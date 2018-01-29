package org.ssh.cloud.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssh.cloud.auth.entity.RoleResource;

public interface RoleResourceDao extends JpaRepository<RoleResource, Long> {

}

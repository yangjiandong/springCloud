package org.ssh.cloud.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssh.cloud.auth.entity.Resource;

public interface ResourceDao extends JpaRepository<Resource, Long> {

    Resource findByName(String name);
}

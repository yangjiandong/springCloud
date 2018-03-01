package org.ssh.boot.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssh.boot.auth.entity.Resource;

public interface ResourceDao extends JpaRepository<Resource, Long> {

    Resource findByName(String name);
}

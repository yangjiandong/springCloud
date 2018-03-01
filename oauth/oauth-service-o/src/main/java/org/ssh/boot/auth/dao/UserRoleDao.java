package org.ssh.boot.auth.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.ssh.boot.auth.entity.Role;
import org.ssh.boot.auth.entity.UserRole;

public interface UserRoleDao extends JpaRepository<UserRole, Long> {

    @Query("select c.roleName from UserRole a, User b, Role c where b.username=?1 and a.uid=b.id and a.rid=c.id")
    List<String> findRoleNameByUserName(String username);

    @Query("select c from UserRole a, User b, Role c where b.username=?1 and a.uid=b.id and a.rid=c.id")
    List<Role> findRolesByUserName(String username);
}

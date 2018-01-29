package org.ssh.cloud.auth.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.ssh.boot.security.dto.AuthoritiesDto;

@Mapper
public interface RoleResourceMapper {

    @Select("select a.path as url, r.roleName from T_Role r, T_RoleResource ra, T_Resource a where r.id=ra.rid and ra.raid=a.id")
    List<AuthoritiesDto> findAllAuthorities();
}

package org.ssh.boot.auth.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.ssh.boot.auth.entity.User;

@Mapper
public interface UserMapper {

  @Select("select id, username from T_User")
  List<User> getAll();

  //@Select("select id, username, name, intros from T_User")
  //User getInfo();
}

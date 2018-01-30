package org.ssh.boot.auth.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.ssh.boot.auth.entity.User;

public interface UserDao extends JpaRepository<User, Long> {

    User findByUsername(String userName);

    // @Modifying
    // @Query("UPDATE User SET attempts = 0, firstAttempt = 0, lockedAt = 0,  accountNonLocked = true WHERE username = :userName")
    // int resetFailAttempts(@Param("userName") String userName);

    // @Modifying
    // @Query("UPDATE User SET attempts = 1, firstAttempt = :firstAttempt WHERE username = :userName")
    // int updateFirstAttempts(@Param("firstAttempt") Long firstAttempt, @Param("userName") String userName);

    // @Modifying
    // @Query("UPDATE User SET attempts = attempts + 1 WHERE username = :userName")
    // int updateAttempts(@Param("userName") String userName);

    // @Modifying
    // @Query("UPDATE User SET accountNonLocked = :accountNonLocked, lockedAt = :lockedAt WHERE username = :userName")
    // int updateLocked(@Param("accountNonLocked") boolean accountNonLocked, @Param("lockedAt") Long lockedAt, @Param("userName") String userName);

    // @Modifying
    // @Query("UPDATE User u SET u.version=u.version+1, u.active = false WHERE u.id IN :ids")
    // void deactivateAll(@Param("ids") Long... ids);
}

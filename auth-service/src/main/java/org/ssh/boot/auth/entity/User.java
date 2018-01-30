package org.ssh.boot.auth.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import lombok.Data;
import lombok.ToString;
import org.ssh.boot.security.entity.SUser;

//https://stackoverflow.com/questions/6164123/org-hibernate-mappingexception-could-not-determine-type-for-java-util-set
@Entity
@Table(name = "T_User")
@ToString(callSuper = true)
@Data
public class User extends SUser {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Id_Generator")
    @TableGenerator(name = "Id_Generator", table = "ID_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "T_User", initialValue = 1, allocationSize = 100)
    private Long id;
    private String email;
    private String telephone;
    private String mobileNo;
    private String intros;
    private String avatar;
}

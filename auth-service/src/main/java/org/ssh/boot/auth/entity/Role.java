package org.ssh.boot.auth.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import lombok.Data;
import lombok.ToString;
import org.ssh.boot.security.entity.SRole;

@Entity
@Table(name = "T_Role")
@ToString(callSuper=true)
@Data
public class Role extends SRole {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Id_Generator")
    @TableGenerator(name = "Id_Generator", table = "ID_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "T_Role", initialValue = 1, allocationSize = 10)
    private Long id;

}

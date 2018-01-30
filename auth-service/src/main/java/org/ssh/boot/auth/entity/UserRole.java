package org.ssh.boot.auth.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import javax.persistence.Transient;
import lombok.Data;

@Entity
@Table(name = "T_UserRole")
@Data
public class UserRole implements Serializable {

    private static final long serialVersionUID = -1;

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Id_Generator")
    @TableGenerator(name = "Id_Generator", table = "ID_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "T_UserRole", initialValue = 1, allocationSize = 100)
    private Long id;
    private Long uid;
    private Long rid;

    @Transient
    private String roleName;
}

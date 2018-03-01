package org.ssh.boot.auth.entity;

import java.io.Serializable;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;
import lombok.Data;

@Entity
@Table(name = "T_Resource")
@Data
public class Resource implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "Id_Generator")
    @TableGenerator(name = "Id_Generator", table = "ID_GENERATOR", pkColumnName = "GEN_NAME", valueColumnName = "GEN_VAL", pkColumnValue = "T_Resource", initialValue = 1, allocationSize = 100)
    private Long id;
    private String name;
    private String path;
    private Boolean requireAuth = false;
}

package yx.rbac.easy.configuration.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "role_permission")
public class RolePermission {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "permission_id")
    private Long permissionId;
    @Column(name = "annotion_id")
    private String annotionId;
    @Column(name = "annotion_name")
    private String annotionNote;

    @Column(name = "role_id")
    private Long roleId;
    @Column(name = "role_name")
    private String roleName;

    public RolePermission(){}
    public RolePermission(Role role,Permission pem){
        this.roleId = role.getId();
        this.roleName = role.getName();
        this.permissionId = pem.getId();
        this.annotionId = pem.getAnnotionId();
        this.annotionNote = pem.getAnnotionNote();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getAnnotionId() {
        return annotionId;
    }

    public void setAnnotionId(String annotionId) {
        this.annotionId = annotionId;
    }

    public String getAnnotionNote() {
        return annotionNote;
    }

    public void setAnnotionNote(String annotionNote) {
        this.annotionNote = annotionNote;
    }
}

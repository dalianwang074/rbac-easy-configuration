package yx.rbac.easy.configuration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import yx.rbac.easy.configuration.entity.RolePermission;

import java.util.List;

@Repository
public interface RolePermissionDao extends JpaRepository<RolePermission, Long> {

    @Query(value = "select * from role_permission ", nativeQuery = true)
    List<RolePermission> getRolePermissions();

    @Query(value = "select * from role_permission where role_id = ?1 ", nativeQuery = true)
    List<RolePermission> listByRoleId(long roleId);

    @Query(value = "select * from role_permission where role_id in (?1) ", nativeQuery = true)
    List<RolePermission> listByRoles(String roles);

}

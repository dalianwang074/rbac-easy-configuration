package yx.rbac.easy.configuration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yx.rbac.easy.configuration.entity.UserRole;

import java.util.List;

@Repository
public interface UserRoleDao extends JpaRepository<UserRole, Long> {

    List<UserRole> findByUserId(long userId);
    List<UserRole> findByRoleId(long roleId);

}

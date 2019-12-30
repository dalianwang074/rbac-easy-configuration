package yx.rbac.easy.configuration.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import yx.rbac.easy.configuration.entity.Role;

@Repository
public interface RoleDao extends JpaRepository<Role, Long> {

}

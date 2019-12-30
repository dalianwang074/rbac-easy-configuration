package yx.rbac.easy.configuration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yx.rbac.easy.configuration.dao.RoleDao;
import yx.rbac.easy.configuration.entity.Role;

import java.util.List;

@Service
public class RoleService {

    @Autowired
    RoleDao roleDao;

    public List<Role> listRole(){
        return roleDao.findAll();
    }

    public void addRole(Role role){
        roleDao.save(role);
    }

}

package yx.rbac.easy.configuration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yx.rbac.easy.configuration.annotion.Authentication;
import yx.rbac.easy.configuration.annotion.UserLoginToken;
import yx.rbac.easy.configuration.entity.Role;
import yx.rbac.easy.configuration.service.RolePermissionService;
import yx.rbac.easy.configuration.service.RoleService;
import yx.rbac.easy.configuration.service.UserRoleService;

@RestController
public class RoleController {

    @Autowired
    RolePermissionService rolePermissionService;

    @Autowired
    UserRoleService userRoleService;

    @Autowired
    RoleService roleService;

    @Authentication(id="add_role",note="添加角色")
    @GetMapping("/addRole")
    public String addRole(@RequestBody Role role){
        roleService.addRole(role);
        return "success";
    }

    @Authentication(id="add_role_permision",note="添加角色权限数据")
    @GetMapping("/addRolePermision")
    public String addRolePermision(long roleId,long permissionId){
        rolePermissionService.addRolePermission(roleId,permissionId);
        return "success";
    }

    @UserLoginToken
    @Authentication(id="add_user_role",note="添加用户角色数据")
    @GetMapping("/addUserRole")
    public String addUserRole(long userId,long roleId){
        userRoleService.addUserRole(userId,roleId);
        return "success";
    }

}

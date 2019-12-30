package yx.rbac.easy.configuration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;
import yx.rbac.easy.configuration.constant.RDSConstant;
import yx.rbac.easy.configuration.dao.PermissionDao;
import yx.rbac.easy.configuration.dao.RoleDao;
import yx.rbac.easy.configuration.dao.RolePermissionDao;
import yx.rbac.easy.configuration.dao.UserRoleDao;
import yx.rbac.easy.configuration.entity.Permission;
import yx.rbac.easy.configuration.entity.Role;
import yx.rbac.easy.configuration.entity.RolePermission;
import yx.rbac.easy.configuration.entity.UserRole;

import java.util.List;

@Service
public class RolePermissionService {

    @Autowired
    RolePermissionDao rolePermissionDao;
    @Autowired
    RoleDao roleDao;
    @Autowired
    PermissionDao permissionDao;

    @Autowired
    UserRoleDao userRoleDao;

    @Autowired
    JedisCluster jedisCluster;

    @Transactional
    public void addRolePermission(long roleId,long permissionId){
        Role role = roleDao.findOne(roleId);
        Permission perm = permissionDao.findOne(permissionId);
        RolePermission rp = new RolePermission(role,perm);
        rolePermissionDao.save(rp);

        //插入用户角色数据之后，开始处理缓存数据
        List<RolePermission> rpList = listByRoleId(roleId);
        String annotionIds[] = new String[rpList.size()];
        for(int i=0;i<rpList.size();i++){
            annotionIds[i] = rpList.get(i).getAnnotionId();
        }

        List<UserRole> urList = userRoleDao.findByRoleId(roleId);
        urList.stream().forEach( userRole -> {
            jedisCluster.sadd(RDSConstant.USER_PERMISSION_HEADER + ":" + userRole.getUserId() , annotionIds);
        });
    }

    /**
     * 查询单条角色权限
     * @param roleId
     * @return
     */
    public List<RolePermission> listByRoleId(long roleId){
        return rolePermissionDao.listByRoleId(roleId);
    }

    /**
     * 查询多条角色权限
     * @param roles
     * @return
     */
    public List<RolePermission> listByUserRoles(List<UserRole> roles){
        if(roles.size() > 0){
            StringBuilder sb = new StringBuilder();
            sb.append(roles.get(0).getRoleId());

            for(int i=1;i<roles.size();i++){
                sb.append("," + roles.get(i).getRoleId());
            }
            return rolePermissionDao.listByRoles(sb.toString());
        }

        return null;
    }

}

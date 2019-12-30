package yx.rbac.easy.configuration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisCluster;
import yx.rbac.easy.configuration.constant.RDSConstant;
import yx.rbac.easy.configuration.dao.RoleDao;
import yx.rbac.easy.configuration.dao.UserDao;
import yx.rbac.easy.configuration.dao.UserRoleDao;
import yx.rbac.easy.configuration.entity.Role;
import yx.rbac.easy.configuration.entity.RolePermission;
import yx.rbac.easy.configuration.entity.User;
import yx.rbac.easy.configuration.entity.UserRole;

import java.util.List;

@Service
public class UserRoleService {

    @Autowired
    RoleDao roleDao;
    @Autowired
    UserDao userDao;
    @Autowired
    UserRoleDao userRoleDao;

    @Autowired
    RolePermissionService rolePermissionService;

    @Autowired
    JedisCluster jedisCluster;

    @Transactional
    public void addUserRole(long userId,long roleId){
        User user = userDao.getOne(userId);
        Role role = roleDao.findOne(roleId);
        UserRole ur = new UserRole(user,role);
        userRoleDao.save(ur);

        //插入用户角色数据之后，开始处理缓存数据
        List<UserRole> roles = userRoleDao.findByUserId(userId);
        List<RolePermission> rpList = rolePermissionService.listByUserRoles(roles);
        if(rpList.size()>0){
            String annotionIds[] = new String[rpList.size()];
            for(int i=0;i<rpList.size();i++){
                annotionIds[i] = rpList.get(i).getAnnotionId();
            }
            jedisCluster.del(RDSConstant.USER_PERMISSION_HEADER + ":" + userId);
            jedisCluster.sadd(RDSConstant.USER_PERMISSION_HEADER + ":" + userId , annotionIds);
        }
    }
}

package yx.rbac.easy.configuration.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import yx.rbac.easy.configuration.dao.PermissionDao;
import yx.rbac.easy.configuration.entity.Permission;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PermissionService {

    @Autowired
    PermissionDao permissionDao;

    public List<Permission> listPermission(){
        return permissionDao.findAll();
    }
    public List<Permission> listRolePermission(long roleId){
        return permissionDao.findAll();
    }

    /**
     * 项目启动时，初始化权限数据
     * 没有则新增，多余则删除
     * @param list
     */
    @Transactional
    public void initPermissionList(List<Permission> list){
        List<Permission> originList = permissionDao.findAll();

        Map<String,Permission> originPermissionMap = new HashMap<>();
        originList.stream().forEach(permission -> {
            originPermissionMap.put(permission.getAnnotionId(),permission);
        });

        List<Permission> insertList = new ArrayList<>();        //新增的接口权限数据
        list.stream().forEach(permission -> {
            String annotionId = permission.getAnnotionId();
            String annotionNote = permission.getAnnotionNote();

            Permission originPermission = originPermissionMap.get(annotionId);
            if(originPermission != null){
                if(!annotionNote.equals(originPermission.getAnnotionNote()) || !annotionId.equals(originPermission.getAnnotionId())){   //如果annotionId 或 如果annotionNote与数据库原有note不至于 与数据库原有数据不符合，则更新此条数据
                    originPermission.setAnnotionNote(annotionNote);
                    originPermission.setAnnotionId(annotionId);
                    insertList.add(originPermission);
                }

                originPermissionMap.remove(annotionId);     //查到数据之后，则将此条数据从map中删除
            }else{
                insertList.add(permission);
            }

        });

        List<Permission> removeList = new ArrayList<>();        //需要删掉的接口权限数据
        for(Permission permission:originPermissionMap.values()){
            removeList.add(permission);
        }

        if(insertList.size() > 0) {
            System.out.println("init annotion insert start:" + LocalTime.now().toString());
            for(Permission p : insertList){
                permissionDao.save(p);
            }
            System.out.println("init annotion insert over:" + LocalTime.now().toString());
        }
        if(removeList.size() > 0)
            permissionDao.delete(removeList);
    }

}

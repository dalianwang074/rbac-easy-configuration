package yx.rbac.easy.configuration.controller;

import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import yx.rbac.easy.configuration.annotion.Authentication;
import yx.rbac.easy.configuration.annotion.UserLoginToken;
import yx.rbac.easy.configuration.entity.Permission;
import yx.rbac.easy.configuration.service.PermissionService;

import java.util.List;

@RestController
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    @UserLoginToken
    @Authentication(id="list_permission",note="列出全部权限数据")
    @GetMapping("/listPermission")
    public String listPermission() {
        List<Permission> list = permissionService.listPermission();
        JSONArray array = new JSONArray();
        for(Permission p : list){
            array.add(p);
        }

        return array.toString();
    }


}

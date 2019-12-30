package yx.rbac.easy.configuration.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import yx.rbac.easy.configuration.annotion.Authentication;
import yx.rbac.easy.configuration.entity.User;
import yx.rbac.easy.configuration.service.UserService;

@RestController
public class UserController {

    @Autowired
    UserService userService;


    @PostMapping("/login")
    public String login(@RequestBody User user) {
        return userService.login(user);
    }

    @Authentication(id="user_permissions",note="用户权限列表")
    @PostMapping("/userPermissions")
    public String userPermissions(long userId) {
        return userService.listUserPermissions(userId);
    }

    @GetMapping("/testAddUsers")
    public String testAddUsers() {
        userService.testAddUsers();
        return "1111";
    }


}

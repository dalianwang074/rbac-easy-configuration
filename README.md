# rbac-easy-configuration
易构RBAC：基于java语言，关于rbac（用户、角色、权限管理）与分布式token快速解决方案。
使用注解拦截模式，适用于中小型项目，具有开箱即用、快速构建的特点。

@UserLoginToken  注解，用于登录认证拦截

@Authentication  注解，用于权限管理拦截

项目使用示例

- step1
新增用户
zhangsan 123456

- step2
访问权限接口


```
@UserLoginToken
    @Authentication(id="list_permission",note="列出全部权限数据")
    @GetMapping("/listPermission")
    public String listPermission()
    ```
    返回异常：
    {
    "timestamp": 1577694727366,
    "status": 500,
    "error": "Internal Server Error",
    "exception": "java.lang.RuntimeException",
    "message": "无token，请重新登录",
    "path": "/listPermission"
}

理想结果，因为我们使用了UserLoginToken 注解，需要登录，获取token之后，再进行访问。

- step3 登录，获取token
```
@PostMapping("/login")
```

获取token 
{
    "user": {
        "id": 504,
        "password": "E10ADC3949BA59ABBE56E057F20F883E",
        "username": "zhangsan"
    },
    "token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1MDQ6MTU3NzY5NDk5OTI3OCJ9.vj9fcTJiWvHOMTHXu61oicwcA-s5MwW-t5Mo34lkgNc"
}

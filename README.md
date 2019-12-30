# rbac-easy-configuration
易构RBAC：基于java语言，关于rbac（用户、角色、权限管理）与分布式token快速解决方案。
使用注解拦截模式，适用于中小型项目，具有开箱即用、快速构建的特点。

@UserLoginToken  注解，用于登录认证拦截

@Authentication  注解，用于权限管理拦截

项目使用示例

## step1
新增用户
zhangsan 123456

## step2
访问权限接口  http://localhost:60203/listPermission


```java
@UserLoginToken
    @Authentication(id=,note=)
    @GetMapping()
    public String listPermission()
```

    返回异常：

    "message": "无token，请重新登录",

理想结果，因为我们使用了UserLoginToken 注解，需要登录，获取token之后，再进行访问。


## step3 登录，获取token  http://localhost:60203/login

```java
@PostMapping()
public String login(@RequestBody User user)
```

获取token 

"token": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1MDQ6MTU3NzY5NDk5OTI3OCJ9.vj9fcTJiWvHOMTHXu61oicwcA-s5MwW-t5Mo34lkgNc"



## step4 再次访问权限接口 ，在header中放入返回的token

token=eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI1MDQ6MTU3NzY5NDk5OTI3OCJ9.vj9fcTJiWvHOMTHXu61oicwcA-s5MwW-t5Mo34lkgNc

报如下异常


"message": "用户不具备访问当前接口权限！",


OK，携带token之后，异常发生了变化，用户不具备访问当前接口权限！ 这正式我们想要返回的结果。 我们需要为用户zhangsan赋予相应的角色。


## step5 为用户赋予相应角色。 

http://localhost:60203/addUserRole?roleId=2&userId=504


此时，我们的数据库里user_role表中，就会有这条用户角色相关联数据。
    
    
    
## step6 插入角色权限数据
 
 讲道理，这步也应该通过接口模拟的，但是简化了，诸君可自行模拟，也可通过sql手动插入数据。
 
 此时，yongh张三应该具备了访问 “权限接口”的权限。
 
 
## step7 最后访问权限接口
 
 返回
 
```json
 [
    {
        "annotionId": "list_permission",
        "annotionNote": "列出全部权限数据",
        "id": 6
    },
    {
        "annotionId": "add_role",
        "annotionNote": "添加角色",
        "id": 9
    },
    {
        "annotionId": "user_permissions",
        "annotionNote": "用户权限列表",
        "id": 10
    },
    {
        "annotionId": "add_role_permision",
        "annotionNote": "添加角色权限数据",
        "id": 11
    }
]
```

success!!!

    
    
    

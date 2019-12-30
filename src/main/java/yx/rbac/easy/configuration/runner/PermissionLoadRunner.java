package yx.rbac.easy.configuration.runner;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;
import yx.rbac.easy.configuration.annotion.Authentication;
import yx.rbac.easy.configuration.entity.Permission;
import yx.rbac.easy.configuration.service.PermissionService;

import java.io.File;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionLoadRunner implements ApplicationRunner {

    @Autowired
    PermissionService permissionService;

    /**
     * 启动后扫描controller下类的权限注解
     * @param applicationArguments
     */
    @Override
    public void run(ApplicationArguments applicationArguments)  {
        List<String> controllerList = new ArrayList<>();

        try {
            //如有包路径改动，此部分代码需要定制
            String packPath = this.toString().substring(0,this.toString().lastIndexOf(".")).replaceFirst("runner","controller");
            File file = new File(this.getClass().getResource("../controller/").getPath());
            if(file.isDirectory()){
                File files[] = file.listFiles();
                for(File f:files){
                    String fileName = f.getName().replaceFirst(".class","");
                    controllerList.add(packPath + "." + fileName);
                }
            }

            List<Permission> permissionList = new ArrayList<>();
            controllerList.stream().forEach(controllerName -> {
                Class clas = null;
                try {
                    clas = Class.forName(controllerName);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }

                Method mothods[] = clas.getMethods();
                for(Method method:mothods){
                    if(method.isAnnotationPresent(Authentication.class)){
                        Authentication perm = method.getAnnotation(Authentication.class);
                        Permission pms = new Permission(perm.id(),perm.note());
                        permissionList.add(pms);
                    }
                }

            });

            permissionService.initPermissionList(permissionList);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Test
    public void testF(){
        File file = new File(this.getClass().getResource("../controller/").getPath());
        if(file.isDirectory()){
            File files[] = file.listFiles();
            for(File f:files){
                System.out.println(f.getName().replaceFirst(".class",""));
            }
        }

        System.out.println(this.getClass().getResource("../controller/").getPath());
        System.out.println(this.getClass().getPackage().toString());
    }


}

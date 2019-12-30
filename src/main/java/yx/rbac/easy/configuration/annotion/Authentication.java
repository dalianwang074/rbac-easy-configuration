package yx.rbac.easy.configuration.annotion;

import java.lang.annotation.*;

/**
 * 权限校验注解：使用此注解方法，需要验证rbac权限
 * id：注解id，需要唯一，建议class_method
 * note：注解说明,显示用
 * 系统正常运作时，切勿更改 id 与 note;
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Authentication {
    /**
     * must be unique
     * @return
     */
    String id();

    /**
     * annotion note,for read on html
     * @return
     */
    String note();
}

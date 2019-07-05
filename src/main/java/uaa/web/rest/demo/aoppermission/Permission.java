package uaa.web.rest.demo.aoppermission;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
/**
 * Created by qcl on 2019/2/18
 * desc:自定义权限管理注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Permission {
    String authorities() default "我是默认值";
}

package uaa.web.rest.demo.aoppermission;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.security.auth.login.LoginException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

@Aspect
//@Component
public class ControllerAspect {
    private final static Logger logger = LoggerFactory.getLogger(ControllerAspect.class);
    @Autowired
    private AOPUserService userService;
    /**
     * 定义切点
     */
    @Pointcut("execution(public * com.demo.permission.controller.*.*(..))")
    public void privilege() {
    }
    /**
     * 权限环绕通知
     *
     * @param joinPoint
     * @throws Throwable
     */
    @ResponseBody
    @Around("privilege()")
    public Object isAccessMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取访问目标方法
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method targetMethod = methodSignature.getMethod();
        //得到方法的访问权限
//        Permission annotation = targetMethod.getAnnotation(Permission.class);
//        Annotation[] annotations = targetMethod.getAnnotations();
//        if(annotations[0].getClass() instanceof  Permission){
//
//        }
        final String methodAccess = "";//AnnotationParse.privilegeParse(targetMethod);
        //如果该方法上没有权限注解，直接调用目标方法
        if (StringUtils.isEmpty(methodAccess)) {
            return joinPoint.proceed();
        } else {
            //获取当前用户
            Object[] args = joinPoint.getArgs();
            if (args == null) {
                throw new LoginException("参数错误");
            }
            String currentUser = args[0].toString();
            logger.info("访问用户，{}", currentUser);
            if (!userService.isAdmin(currentUser)) {
                throw new LoginException("您不是管理员");
            } else {
                logger.info("您是管理员");
                //是管理员时，才返回所需要的信息
                return joinPoint.proceed();
            }
        }
    }
}

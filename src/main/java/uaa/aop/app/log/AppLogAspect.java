package uaa.aop.app.log;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * 20180824，统计AppLogResource方法调用的切面
 */
@Aspect
@Component
public class AppLogAspect {
//    表达式中拦截了com.cn.spring.aspectj.NotVeryUsefulAspectService该类中所有方法。当执行行该类中方法时 执行相应的拦截方法，pointcut只负责 切入方法，并未执行方法体。这点要注意！
//
//    此后 讲解下Aspect  几个通知注解(advice)
//
//    @Pointcut 拦截的切入点方法，注解的在方法级别之上，但是不执行方法体，只表示切入点的入口。
    /*
    @Before 顾名思义 是在 切入点 之前执行 方法。

@AfterReturning 返回拦截方法的返回值

@AfterThrowing 拦截的方法 如果抛出异常 加执行此方法 throwing="ex" 将异常返回到参数列表

@After 在之上方法执行后执行结束操作


@Around  判断是否执行 以上的拦截 ，第一个参数必须ProceedingJoinPoint. 如要拦截：

     */
    //TODO Test
    @Pointcut(value="execution(* com.cn.spring.aspectj.NotVeryUsefulAspectService.*(..)) && args(param)")
    private void pointcut(String param){
        System.out.println("切入点pointcut()"+param);
    }
    //方法体将不执行
    @Pointcut("within(com.cn.spring.aspectj.*)")
    public String inWebLayer() {
        System.out.println("切入点inWebLayer()");
        return "返回值加载";
    }
    @Before(value="inWebLayer()")
    private void beforeinWebLayer(){
        System.out.println("beforeinWebLayer~~");
    }

    @AfterReturning(pointcut="inWebLayer()",returning="retVal")
    public void doAccessCheck(Object retVal) {
        System.out.println("doAccessCheck:"+retVal);
    }
    //TODO logEachInfo
    @Before("logEachInfo")
    public void beforeEachInfo(JoinPoint joinPoint){
        System.out.println("AppLogAspect BeforeTest:"+joinPoint.getSignature().getName());
    }
    @Pointcut("execution(* *EachInfo(..))")
    public void logEachInfo(){

    }
    @Pointcut("execution(* *DetailInfo(..))")
    public void logDetailInfo(){

    }
}

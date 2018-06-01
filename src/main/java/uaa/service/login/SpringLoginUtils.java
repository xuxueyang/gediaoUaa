package uaa.service.login;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class SpringLoginUtils implements ApplicationContextAware {
    public static ApplicationContext applicationContext = null;

    public SpringLoginUtils() {
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (applicationContext == null) {
            applicationContext = applicationContext;
        }

    }

    public static <T> T getBean(Class<T> clazz) {
        return applicationContext.getBean(clazz);
    }
}

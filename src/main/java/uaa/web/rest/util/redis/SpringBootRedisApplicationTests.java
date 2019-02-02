package uaa.web.rest.util.redis;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringRunner;
import uaa.domain.app.log.AppLogEach;
import util.UUIDGenerator;

//@RunWith(SpringRunner.class)
//@SpringBootTest
public class SpringBootRedisApplicationTests {
//    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private RedisService service;
//
//    @Test
//    public void contextLoads() {
//        AppLogEach user=new AppLogEach();
//        user.setId(UUIDGenerator.getUUID());
//        service.addUser(user);
//
//        System.out.println("RedisTest执行完成，return {}"+service.getStudent(user.getId()).getId());
//    }

}
//---------------------
//    作者：abombhz
//    来源：CSDN
//    原文：https://blog.csdn.net/abombhz/article/details/78123253
//    版权声明：本文为博主原创文章，转载请附上博文链接！

package uaa.web.rest.demo.hystrix;

import org.springframework.stereotype.Component;

@Component
public class SchedualServiceHiHystric implements SchedualServiceHi {
    @Override
    public String sayHiFromClientOne(String name) {
        return "sorry "+name;
    }
}
//---------------------
//    作者：方志朋
//    来源：CSDN
//    原文：https://blog.csdn.net/forezp/article/details/81040990
//    版权声明：本文为博主原创文章，转载请附上博文链接！

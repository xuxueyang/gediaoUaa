package uaa.web.rest.demo.hystrix;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "service-hi",fallback = SchedualServiceHiHystric.class)
public interface SchedualServiceHi {
    @RequestMapping(value = "/hi",method = RequestMethod.GET)
    String sayHiFromClientOne(@RequestParam(value = "name") String name);
}
//---------------------
//    作者：方志朋
//    来源：CSDN
//    原文：https://blog.csdn.net/forezp/article/details/81040990
//    版权声明：本文为博主原创文章，转载请附上博文链接！

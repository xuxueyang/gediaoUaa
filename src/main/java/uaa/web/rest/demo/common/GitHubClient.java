package uaa.web.rest.demo.common;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "github-client",
    url = "https://api.github.com",
    configuration = GitHubExampleConfig.class,
    fallback = GitHubClient.DefaultFallback.class)
public interface GitHubClient {
    @RequestMapping(value = "/search/repositories", method = RequestMethod.GET)
    String searchRepo(@RequestParam("q") String queryStr);

    /**
     * 容错处理类，当调用失败时，简单返回空字符串
     */
    @Component
    public class DefaultFallback implements GitHubClient {
        @Override
        public String searchRepo(@RequestParam("q") String queryStr) {
            return "";
        }
    }
}

//　声明接口之后，在代码中通过@Resource注入之后即可使用。@FeignClient标签的常用属性如下：
//
//    name：指定FeignClient的名称，如果项目使用了Ribbon，name属性会作为微服务的名称，用于服务发现
//    url: url一般用于调试，可以手动指定@FeignClient调用的地址
//decode404:当发生http 404错误时，如果该字段位true，会调用decoder进行解码，否则抛出FeignException
//    configuration: Feign配置类，可以自定义Feign的Encoder、Decoder、LogLevel、Contract
//    fallback: 定义容错的处理类，当调用远程接口失败或超时时，会调用对应接口的容错逻辑，fallback指定的类必须实现@FeignClient标记的接口
//fallbackFactory: 工厂类，用于生成fallback类示例，通过这个属性我们可以实现每个接口通用的容错逻辑，减少重复的代码
//    path: 定义当前FeignClient的统一前缀

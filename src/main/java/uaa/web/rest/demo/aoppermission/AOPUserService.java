package uaa.web.rest.demo.aoppermission;

import org.springframework.stereotype.Service;
import java.util.Arrays;
/**
 * Created by qcl on 2019/2/18
 * desc:
 */
//@Service
public class AOPUserService {
    private String[] admins = {"qiushi", "weixin", "xiaoshitou"};
    //是否是管理员
    boolean isAdmin(String name) {
        return Arrays.asList(admins).contains(name);
    }
}

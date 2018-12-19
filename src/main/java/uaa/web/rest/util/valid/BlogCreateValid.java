package uaa.web.rest.util.valid;


import org.apache.commons.lang.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uaa.service.dto.app.blog.AppBlogCreateDto;

public class BlogCreateValid implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return AppBlogCreateDto.class.equals(aClass);
    }

    @Override
    public void validate(Object obj, Errors errors) {
        //把校验信息注册到Error的实现类里
//        ValidationUtils.rejectIfEmpty(errors,"name",null,"姓名不能为空!");
        AppBlogCreateDto personScope = (AppBlogCreateDto) obj;
        if(StringUtils.isEmpty(personScope.getTitle())){
            errors.rejectValue("title",null,"标题不能为空");
        }
//        ---------------------
//            作者：木叶之荣
//        来源：CSDN
//        原文：https://blog.csdn.net/zknxx/article/details/52426771
//        版权声明：本文为博主原创文章，转载请附上博文链接！
    }
}

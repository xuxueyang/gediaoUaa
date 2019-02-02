package uaa.web.rest.app.blog;

import com.alibaba.fastjson.JSON;
import core.ReturnCode;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import uaa.config.Constants;
import uaa.domain.app.blog.AppBlogBlog;
import uaa.domain.app.blog.AppBlogCategory;
import uaa.domain.uaa.UaaToken;
import uaa.service.app.blog.AppBlogService;
import uaa.service.dto.app.blog.*;
import uaa.service.login.UaaLoginService;
import uaa.web.rest.BaseResource;
import uaa.web.rest.util.CommonUtil;
import uaa.web.rest.util.redis.Redis;
import uaa.web.rest.util.valid.BlogCreateValid;
import util.Validators;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by UKi_Hi on 2018/12/9.
 */
@RestController
@RequestMapping("/api/app/blog")
public class AppBlogResource extends BaseResource {
    @Autowired
    private AppBlogService appBlogService;
    @Autowired
    private UaaLoginService uaaLoginService;

//    @InitBinder
//    public void initBinder(WebDataBinder webDataBinder){
//        webDataBinder.addValidators(new BlogCreateValid());
//    }
    @PutMapping("/category")
    public ResponseEntity createBlogCategory(@RequestBody AppBlogCategoryDto dto){
        try{
            //博客的名字不允许重复！
            if(StringUtils.isBlank(dto.getName()))
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            AppBlogCategory categoryByName = appBlogService.findCategoryByName(dto.getName());
            if(categoryByName!=null)
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EXIST_CODE,null);
            appBlogService.createCategory(dto);
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }
    @GetMapping("/categorys")
    public ResponseEntity getBlogCategory(){
        try{
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,appBlogService.findAllBlogCategorys());
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }
    @PutMapping("/blog")
    @ApiOperation(value = "创建博客", httpMethod = "PUT", response = ResponseEntity.class, notes = "创建博客")
    public ResponseEntity createBlog( @RequestBody AppBlogCreateDto dto, BindingResult bindingResult){
        try{
            if(bindingResult.hasErrors()){
                return prepareReturnResult(ReturnCode.HAS_ERROR,bindingResult.getAllErrors().get(0));
            }
            if(Validators.fieldBlank(dto.getPermissionType())
                ||Validators.fieldBlank(dto.getSourceType())||Validators.fieldBlank(dto.getTitle())||Validators.fieldBlank(dto.getToken())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            //判断类型和来源是不是在范围里
            if(!Validators.fieldRangeValue(dto.getPermissionType(),
                Constants.PERMISSION_TYPE.KeyCan.name(),
                Constants.PERMISSION_TYPE.NoLimit.name(),
                Constants.PERMISSION_TYPE.OnlyOne.name())
                ||!Validators.fieldRangeValue(dto.getSourceType(),
                    Constants.sourceType.Owner.name(),
                    Constants.sourceType.Transfer.name())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_RANGE,null);
            }
            UaaToken userByToken = uaaLoginService.getUserByToken(dto.getToken());
            if(userByToken==null)
                return prepareReturnResult(ReturnCode.ERROR_NO_PERMISSIONS_UPDATE,null);
            String id = appBlogService.createAppBlogDTO(dto,userByToken.getCreatedid());
            Map<String,Object> map = new HashMap<>();
            map.put("id",id);
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,map);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }
    @PostMapping("/blog/updatePermission")
    @ApiOperation(value = "专门用于更新博客权限的接口",httpMethod = "POST",response = ResponseEntity.class,notes = "专门用于更新博客权限的接口")
    public ResponseEntity updateBlogPermission(@RequestBody AppBlogUpdatePermissionDto dto){
        try {
            if(!Validators.fieldRangeValue(dto.getPermissionType(),
                Constants.PERMISSION_TYPE.KeyCan.name(),
                Constants.PERMISSION_TYPE.NoLimit.name(),
                Constants.PERMISSION_TYPE.OnlyOne.name())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_RANGE,null);
            }
            if(Constants.PERMISSION_TYPE.KeyCan.equals(dto.getPermissionType())&&Validators.fieldBlank(dto.getPermissionVerify())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            UaaToken userByToken = uaaLoginService.getUserByToken(dto.getToken());
            if(userByToken==null)
                return prepareReturnResult(ReturnCode.ERROR_NO_PERMISSIONS_UPDATE,null);
            AppBlogBlog blog = appBlogService.getBlogById(dto.getId());
            if(blog==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            //判断是不是创建者
            if(!blog.getCreateId().equals(userByToken.getCreatedid())){
                return prepareReturnResult(ReturnCode.ERROR_HAVE_NO_PERMISSION_OPERATION,null);
            }
            appBlogService.updateBlogPermission(blog,dto);

            return prepareReturnResult(ReturnCode.UPDATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_UPDATE,null);
        }
    }
    @PostMapping("/blog")
    @ApiOperation(value = "更新博客", httpMethod = "POST",response = ResponseEntity.class,notes = "更新博客")
    public ResponseEntity updateBlog(@RequestBody AppBlogSaveDto dto){
        try {
            //验证token权限和自断非空
            if(Validators.fieldBlank(dto.getId())||Validators.fieldBlank(dto.getTitle())||Validators.fieldBlank(dto.getToken())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            UaaToken userByToken = uaaLoginService.getUserByToken(dto.getToken());
            if(userByToken==null)
                return prepareReturnResult(ReturnCode.ERROR_NO_PERMISSIONS_UPDATE,null);
            AppBlogBlog blog = appBlogService.getBlogById(dto.getId());
            if(blog==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            //判断是不是创建者
            if(!blog.getCreateId().equals(userByToken.getCreatedid())){
                return prepareReturnResult(ReturnCode.ERROR_HAVE_NO_PERMISSION_OPERATION,null);
            }
            //更新
            //TODO 权限和标签的更新调整暂时没有添加
            if(!Validators.fieldRangeValue(dto.getPermissionType(),
                Constants.PERMISSION_TYPE.KeyCan.name(),
                Constants.PERMISSION_TYPE.NoLimit.name(),
                Constants.PERMISSION_TYPE.OnlyOne.name())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_RANGE,null);
            }
//            if(Constants.PERMISSION_TYPE.KeyCan.equals(dto.getPermissionType())&&Validators.fieldBlank(dto.getPermissionVerify())){
//                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
//            }
            appBlogService.updateBlog(blog,dto);
            //TODO 删除redis缓存
//            Redis.getJedis().del("blog_"+dto.getId());
            return prepareReturnResult(ReturnCode.UPDATE_SUCCESS,null);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_UPDATE,null);
        }
    }
    @GetMapping("/blog/{id}")
    @ApiOperation(value = "获取博客", httpMethod = "GET", response = ResponseEntity.class, notes = "获取博客")
    public ResponseEntity getBlog(@PathVariable("id") String id,
                                  @RequestParam(value = "token",required = false) String token,
                                  @RequestParam(value = "verify",required = false) String verify){
        try {
            UaaToken userByToken = null;
            if(StringUtils.isNotBlank(token)){
                userByToken = uaaLoginService.getUserByToken(token);
            }
            AppBlogDto blog = appBlogService.getBlog(id,userByToken==null?null:userByToken.getCreatedid(),verify);
            //判断权限
            if(blog==null){
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }else {
                logApi("/api/app/blog/blog/{id}","获取博客",
                    Constants.HttpType.GET.name(),
                    id,
                    Constants.ProjectType.GEDIAO.name(),
                    CommonUtil.getTodayBelongDate(),null);
                return prepareReturnResult(ReturnCode.GET_SUCCESS,blog);
            }

        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
    @GetMapping("/blogs")
    @ApiOperation(value = "获取到全部的博客如果传token就获取到token下的，如果没有，那么就获取到所有可以看到的列表(包含密码验证的，但是密码验证的需要加个锁标记",
        httpMethod = "GET",response = ResponseEntity.class,
        notes = "获取到全部的博客如果传token就获取到token下的，如果没有，那么就获取到所有可以看到的列表(包含密码验证的，但是密码验证的需要加个锁标记")
    public ResponseEntity getAllBlogs(@RequestParam(value = "token",required = false) String token,
                                      @RequestParam(value = "categoryName",required = false) String categoryName,
                                      @RequestParam(value = "page", defaultValue = "0") Integer page,
                                      @RequestParam(value = "size", defaultValue = "10") Integer size){
        try {
//            String s = Redis.getJedis().get(""+token + "_blogs");
//            if(s!=null){
//                return  prepareReturnResult(ReturnCode.GET_SUCCESS,s );
//            }
            UaaToken userByToken = uaaLoginService.getUserByToken(token);
            List<AppBlogDto> allBlogs = appBlogService.getAllBlogs(userByToken==null?"":userByToken.getCreatedid(),page,size,categoryName);
//            Redis.getJedis().set(token + "_blogs", JSON.toJSONString(allBlogs));
            return prepareReturnResult(ReturnCode.GET_SUCCESS,allBlogs);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_QUERY,null);
        }
    }
}

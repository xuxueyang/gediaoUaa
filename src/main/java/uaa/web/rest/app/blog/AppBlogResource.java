package uaa.web.rest.app.blog;

import core.ReturnCode;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uaa.config.Constants;
import uaa.domain.uaa.UaaToken;
import uaa.service.app.blog.AppBlogService;
import uaa.service.dto.app.blog.AppBlogCreateDto;
import uaa.service.dto.app.blog.AppBlogDto;
import uaa.service.login.UaaLoginService;
import uaa.web.rest.BaseResource;
import uaa.web.rest.util.CommonUtil;
import util.Validators;

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

    @PostMapping("/blog/create")
    @ApiOperation(value = "创建博客", httpMethod = "GET", response = ResponseEntity.class, notes = "创建博客")
    public ResponseEntity createBlog(AppBlogCreateDto dto){
        try{
            if(Validators.fieldBlank(dto.getPermissionType())
                ||Validators.fieldBlank(dto.getSourceType())||Validators.fieldBlank(dto.getTitle())||Validators.fieldBlank(dto.getToken())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY,null);
            }
            //判断类型和来源是不是在范围里
            if(!Validators.fieldRangeValue(dto.getPermissionType(), Constants.PERMISSION_TYPE.values())
                ||!Validators.fieldRangeValue(dto.getSourceType(),Constants.sourceType.values())){
                return prepareReturnResult(ReturnCode.ERROR_FIELD_RANGE,null);
            }
            UaaToken userByToken = uaaLoginService.getUserByToken(dto.getToken());
            if(userByToken==null)
                return prepareReturnResult(ReturnCode.ERROR_NO_PERMISSIONS_UPDATE,null);
            String id = appBlogService.createAppBlogDTO(dto,userByToken.getId());
            return prepareReturnResult(ReturnCode.CREATE_SUCCESS,id);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE,null);
        }
    }

    @GetMapping("/blog/{id}")
    @ApiOperation(value = "获取博客", httpMethod = "GET", response = ResponseEntity.class, notes = "获取博客")
    public ResponseEntity getBlog(@PathVariable("id") String id,
                                  @RequestParam(value = "token",required = true) String token,
                                  @RequestParam(value = "verify",required = false) String verify){
        try {
            UaaToken userByToken = uaaLoginService.getUserByToken(token);
            AppBlogDto blog = appBlogService.getBlog(id,userByToken==null?null:userByToken.getId(),verify);
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

        }
    }
}

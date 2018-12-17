package uaa.service.app.blog;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.config.Constants.PERMISSION_TYPE;
import uaa.domain.app.blog.AppBlogBlog;
import uaa.domain.uaa.UaaUser;
import uaa.repository.app.blog.AppBlogBlogRepository;
import uaa.service.UaaUserService;
import uaa.service.dto.app.blog.AppBlogCreateDto;
import uaa.service.dto.app.blog.AppBlogDto;
import uaa.service.dto.app.blog.AppBlogSaveDto;
import util.UUIDGenerator;
import util.Validators;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by UKi_Hi on 2018/12/8.
 */
@Service
@Transactional
public class AppBlogService {
    @Autowired
    private AppBlogBlogRepository blogRepository;

    @Autowired
    private UaaUserService uaaUserService;


    public String createAppBlogDTO(AppBlogCreateDto appBlogCreateDto,String userId){
        AppBlogBlog blog = new AppBlogBlog();
        blog.setContent(appBlogCreateDto.getContent());
        blog.setPermissionType(appBlogCreateDto.getPermissionType());
        blog.setPermissionVerify(appBlogCreateDto.getPermissionType());
        blog.setCreateId(userId);
        blog.setUpdateId(userId);
        blog.setCreatedDate(ZonedDateTime.now());
        blog.setUpdatedDate(ZonedDateTime.now());
        blog.setId(UUIDGenerator.getUUID());
        blog.setReadCount(0);
        blog.setSourceType(appBlogCreateDto.getSourceType());
        blog.setStatus(Constants.SAVE);
        blogRepository.save(blog);
        return blog.getId();
    }
    public List<AppBlogDto> getAllBlogs(String userId){
        //如果userId为空，那么就搜索出，所以不是自己可见的
        //如果userId不为空，那么就搜索该用户下的全部
        List<AppBlogDto> returnList = new ArrayList<>();
        List<AppBlogBlog> allByCreateIdAndStatus=null;
        if(StringUtils.isNotBlank(userId)){
            allByCreateIdAndStatus = blogRepository.findAllByCreateIdAndStatus(userId, Constants.SAVE);
        }else{
            allByCreateIdAndStatus = blogRepository.findAllByStatusAndPermissionTypeIsNot(Constants.SAVE, "OnlyOne");
        }
        if(allByCreateIdAndStatus!=null&&allByCreateIdAndStatus.size()>0){
            for(AppBlogBlog one:allByCreateIdAndStatus){
                AppBlogDto dto=new AppBlogDto();
                dto.setContent(one.getContent());
                dto.setId(one.getId());
                dto.setReadCount(one.getReadCount());
                dto.setCreatedDate(one.getCreatedDate());
                dto.setTitle(one.getTitle());
                dto.setUpdatedDate(one.getUpdatedDate());
                String createId = one.getCreateId();
                UaaUser userById = uaaUserService.findUserById(createId);
                dto.setAuthorId(userById==null?"":createId);
                dto.setAuthorName(userById==null?"匿名":userById.getName());
                returnList.add(dto);
            }
        }
        return returnList;
    }
    public AppBlogDto getBlog(String id,String userId,String verify){
        AppBlogDto dto = null;
        AppBlogBlog one = blogRepository.getOne(id);
        if(one!=null){
            //验证权限
            if(Validators.fieldNotBlank(one.getPermissionType())){
                switch (one.getPermissionType()){
                    case "KeyCan":
                        // 验证秘尺对不对
                        if(Validators.fieldNotBlank(one.getPermissionVerify())&&one.getPermissionType().equals(verify)){
                            break;
                        }else{
                            return null;
                        }
                    case "NoLimit":
                        break;
                    case "OnlyOne":
                        //验证是不是创建者
                        if(!one.getCreateId().equals(userId)){
                            return null;
                        }
                        break;
                    default:
                        return null;
                }
            }
            dto=new AppBlogDto();
            dto.setContent(one.getContent());
            dto.setReadCount(one.getReadCount());
            dto.setTitle(one.getTitle());
            dto.setId(one.getId());
            dto.setCreatedDate(one.getCreatedDate());
            dto.setUpdatedDate(one.getUpdatedDate());
            String createId = one.getCreateId();
            UaaUser userById = uaaUserService.findUserById(createId);
            dto.setAuthorId(userById==null?"":createId);
            dto.setAuthorName(userById==null?"匿名":userById.getName());
            //查询评论（构建为树的形式）
            //设置阅读数目
        }
        return  dto;
    }

    public AppBlogBlog getBlogById(String id) {
        return blogRepository.getOne(id);
    }

    public void updateBlog(AppBlogBlog blog, AppBlogSaveDto dto) {
        blog.setTitle(dto.getTitle());
        blog.setContent(dto.getContent());
        blog.setUpdatedDate(ZonedDateTime.now());
        blogRepository.save(blog);
    }
}

package uaa.service.app.blog;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.config.Constants.PERMISSION_TYPE;
import uaa.domain.app.blog.AppBlogBlog;
import uaa.domain.app.blog.AppBlogTag;
import uaa.domain.app.log.AppLogTag;
import uaa.domain.uaa.UaaUser;
import uaa.repository.app.blog.AppBlogBlogRepository;
import uaa.repository.app.blog.AppBlogTagRepository;
import uaa.repository.app.log.AppLogTagRepository;
import uaa.service.UaaUserService;
import uaa.service.dto.app.blog.AppBlogCreateDto;
import uaa.service.dto.app.blog.AppBlogDto;
import uaa.service.dto.app.blog.AppBlogSaveDto;
import uaa.service.dto.app.blog.AppBlogUpdatePermissionDto;
import uaa.service.dto.app.log.AppLogTagDTO;
import util.UUIDGenerator;
import util.Validators;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Iterator;
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
    @Autowired
    private AppLogTagRepository tagRepository;
    @Autowired
    private AppBlogTagRepository blogTagRepository;

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
    //要加分页
//    Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "id");
//    Page<Book> bookPage = bookRepository.findAll(new Specification<Book>(){
//        @Override
//        public Predicate toPredicate(Root<Book> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
//            List<Predicate> list = new ArrayList<Predicate>();
//            if(null!=bookQuery.getName()&&!"".equals(bookQuery.getName())){
//                list.add(criteriaBuilder.equal(root.get("name").as(String.class), bookQuery.getName()));
//            }
//            if(null!=bookQuery.getIsbn()&&!"".equals(bookQuery.getIsbn())){
//                list.add(criteriaBuilder.equal(root.get("isbn").as(String.class), bookQuery.getIsbn()));
//            }
//            if(null!=bookQuery.getAuthor()&&!"".equals(bookQuery.getAuthor())){
//                list.add(criteriaBuilder.equal(root.get("author").as(String.class), bookQuery.getAuthor()));
//            }
//            Predicate[] p = new Predicate[list.size()];
//            return criteriaBuilder.and(list.toArray(p));
//        }

        @Transactional(readOnly = true)
    public List<AppBlogDto> getAllBlogs(String userId, int page,int size){
        //如果userId为空，那么就搜索出，所以不是自己可见的
        //如果userId不为空，那么就搜索该用户下的全部
        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "createdDate");
        List<AppBlogDto> returnList = new ArrayList<>();
        Page<AppBlogBlog> allByCreateIdAndStatus=null;
        if(StringUtils.isNotBlank(userId)){
            allByCreateIdAndStatus = blogRepository.findAllByCreateIdAndStatusOrderByCreatedDate(pageable,userId, Constants.SAVE);
        }else{
            allByCreateIdAndStatus = blogRepository.findAllByStatusAndPermissionTypeIsNotOrderByCreatedDate(pageable,Constants.SAVE, "OnlyOne");
        }
        if(allByCreateIdAndStatus!=null&&allByCreateIdAndStatus.hasContent()){
            Iterator<AppBlogBlog> iterator = allByCreateIdAndStatus.iterator();
            while (iterator.hasNext()){
                AppBlogBlog one =  iterator.next();
                AppBlogDto dto=new AppBlogDto();
                dto.setContent(one.getContent());
                dto.setId(one.getId());
                dto.setReadCount(one.getReadCount());
                dto.setCreatedDate(one.getCreatedDate());
                dto.setTitle(one.getTitle());
                dto.setPermissionType(one.getPermissionType());
                dto.setUpdatedDate(one.getUpdatedDate());
                String createId = one.getCreateId();
                UaaUser userById = uaaUserService.findUserById(createId);
                dto.setAuthorId(userById==null?"":createId);
                dto.setAuthorName(userById==null?"匿名":userById.getName());
                //查看标签
                List<AppBlogTag> allByBaseIdAndStatus = blogTagRepository.findAllByBaseIdAndStatus(one.getId(), Constants.SAVE);
                List<AppLogTagDTO> tagDTOList = new ArrayList<>();
                if(allByBaseIdAndStatus!=null&&allByBaseIdAndStatus.size()>0){
                    for(AppBlogTag blogTag:allByBaseIdAndStatus){
                        AppLogTag tag = tagRepository.findOne(blogTag.getTagId());
                        if(tag!=null){
                            AppLogTagDTO tagDTO = new AppLogTagDTO();
                            tagDTO.setId(tag.getId());
                            tagDTO.setName(tag.getName());
                            tagDTO.setGroup(tag.getGroup());
                            tagDTO.setType(tag.getType());
                            tagDTOList.add(tagDTO);
                        }
                    }
                }
                dto.setTagList(tagDTOList);
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
            //设置阅读数目，如果userId为空或者不等于创建者
            if(StringUtils.isBlank(userId)||!userId.equals(one.getCreateId())){
                one.setReadCount(1+one.getReadCount());
                blogRepository.save(one);
            }
            dto=new AppBlogDto();
            dto.setContent(one.getContent());
            dto.setReadCount(one.getReadCount());
            dto.setTitle(one.getTitle());
            dto.setId(one.getId());
            dto.setCreatedDate(one.getCreatedDate());
            dto.setPermissionType(one.getPermissionType());
            dto.setUpdatedDate(one.getUpdatedDate());
            String createId = one.getCreateId();
            UaaUser userById = uaaUserService.findUserById(createId);
            dto.setAuthorId(userById==null?"":createId);
            dto.setAuthorName(userById==null?"匿名":userById.getName());
            //查询评论（构建为树的形式）
            //查看标签
            List<AppBlogTag> allByBaseIdAndStatus = blogTagRepository.findAllByBaseIdAndStatus(one.getId(), Constants.SAVE);
            List<AppLogTagDTO> tagDTOList = new ArrayList<>();
            if(allByBaseIdAndStatus!=null&&allByBaseIdAndStatus.size()>0){
                for(AppBlogTag blogTag:allByBaseIdAndStatus){
                    AppLogTag tag = tagRepository.findOne(blogTag.getTagId());
                    if(tag!=null){
                        AppLogTagDTO tagDTO = new AppLogTagDTO();
                        tagDTO.setId(tag.getId());
                        tagDTO.setName(tag.getName());
                        tagDTO.setGroup(tag.getGroup());
                        tagDTO.setType(tag.getType());
                        tagDTOList.add(tagDTO);
                    }
                }
            }
            dto.setTagList(tagDTOList);

        }
        return  dto;
    }

    public AppBlogBlog getBlogById(String id) {
        return blogRepository.getOne(id);
    }

    public void updateBlog(AppBlogBlog blog, AppBlogSaveDto dto) {
        blog.setTitle(dto.getTitle());
        blog.setContent(dto.getContent());
        blog.setPermissionType(dto.getPermissionType());
        if(PERMISSION_TYPE.KeyCan.name().equals(dto.getPermissionType())){
            dto.setPermissionVerify(dto.getPermissionVerify());
        }
        blog.setUpdatedDate(ZonedDateTime.now());
        blogRepository.save(blog);
    }

    public void updateBlogPermission(AppBlogBlog blog, AppBlogUpdatePermissionDto dto) {
        blog.setPermissionType(dto.getPermissionType());
        if(PERMISSION_TYPE.KeyCan.name().equals(dto.getPermissionType())){
            dto.setPermissionVerify(dto.getPermissionVerify());
        }
        blog.setUpdatedDate(ZonedDateTime.now());
        blogRepository.save(blog);
    }
}

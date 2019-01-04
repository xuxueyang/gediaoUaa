package uaa.service.app.blog;

import org.apache.commons.lang.StringUtils;
import org.neo4j.unsafe.impl.batchimport.cache.idmapping.string.Radix;
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
import uaa.domain.uaa.UaaFile;
import uaa.domain.uaa.UaaUser;
import uaa.repository.app.blog.AppBlogBlogRepository;
import uaa.repository.app.blog.AppBlogTagRepository;
import uaa.repository.app.log.AppLogTagRepository;
import uaa.repository.uaa.UaaFileRepository;
import uaa.service.UaaUserService;
import uaa.service.dto.app.blog.AppBlogCreateDto;
import uaa.service.dto.app.blog.AppBlogDto;
import uaa.service.dto.app.blog.AppBlogSaveDto;
import uaa.service.dto.app.blog.AppBlogUpdatePermissionDto;
import uaa.service.dto.app.log.AppLogTagDTO;
import uaa.service.dto.upload.UploadResultDTO;
import util.UUIDGenerator;
import util.Validators;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by UKi_Hi on 2018/12/8.
 */
@Service
@Transactional
public class AppBlogService {
    @Autowired
    private UaaFileRepository fileRepository;

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
        blog.setTitleImageFileId(appBlogCreateDto.getImgUrlId());
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
                //获取全部的时候内容 提取
//                dto.setContent(one.getContent());
                dto.setPreviewContent(getPreviewContent(one.getContent()));
//                dto.setPreviewContent(one.getContent());
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
                //得到封面图片文件
                if(StringUtils.isNotBlank(one.getTitleImageFileId())){
                    UaaFile titleImage = fileRepository.findOne(one.getTitleImageFileId());
                    if(titleImage!=null){
                        UploadResultDTO uploadResult = new UploadResultDTO();
                        uploadResult.setId(titleImage.getId());
                        uploadResult.setUploadFileName(titleImage.getName());
                        uploadResult.setName(titleImage.getRelFilePath().substring(1,titleImage.getRelFilePath().length()));
                        uploadResult.setPath(titleImage.getRootFilePath()+titleImage.getRelFilePath());
                        dto.setTitleImg(uploadResult);
                    }
                }


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

    /**
     * 得到富文本的预览内容
     * @param content
     * @return
     */
    private static String getPreviewContent(String content) {

//        StringBuffer returnStr = new StringBuffer();
//        String[] split = content.split("</h2>");
//        if(split[0].startsWith("<h2>")){
//            split[0] = split[0].substring("<h2>".length(), split[0].length() - 1);
//        }
//        if(split.length>1){
//            String[] splitP = split[1].split("</p>");
//            int k = splitP.length;
//            while (returnStr.length()<=Constants.PREVIEW_LENGTH||k>0){
//                String tmp = splitP[splitP.length-k];
//                if(tmp.startsWith("<p>")){
//                    tmp = tmp.substring("<p>".length(), tmp.length() - 1);
//                }
//                if(tmp.contains("<img")){
//                    String[] split1 = tmp.split("<img.*>");
//                    StringBuffer tmpS = new StringBuffer();
//                    for(int i=0;i<split1.length;i++){
//                        tmpS.append(split1[i]);
//                    }
//                    tmp = tmpS.toString();
//                }
//                returnStr.append("\n"+tmp);
//                k--;
//            }
//        }else{
//            returnStr.append(split[0]);
//        }
//        return returnStr.toString();
        return "";
    }

//    public static void main(String[] args){
//        String tmp = "<h2>这是博客的内容</h2><p>“为什么你要这样？”某处旅馆，一个人声音有点颤抖。</p><p>“...”另一个人背对着他，低头吸着烟，沉默不语。</p><p>“为什么你要这样？”</p>";
//        getPreviewContent(tmp);
//    }
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
            //得到封面图片文件
            if(StringUtils.isNotBlank(one.getTitleImageFileId())){
                UaaFile titleImage = fileRepository.findOne(one.getTitleImageFileId());
                if(titleImage!=null){
                    UploadResultDTO uploadResult = new UploadResultDTO();
                    uploadResult.setId(titleImage.getId());
                    uploadResult.setUploadFileName(titleImage.getName());
                    uploadResult.setName(titleImage.getRelFilePath().substring(1,titleImage.getRelFilePath().length()));
                    uploadResult.setPath(titleImage.getRootFilePath()+titleImage.getRelFilePath());
                    dto.setTitleImg(uploadResult);
                }
            }

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
        blog.setTitleImageFileId(dto.getImgUrlId());
        blog.setPermissionType(dto.getPermissionType());
        if(PERMISSION_TYPE.KeyCan.name().equals(dto.getPermissionType())){
            dto.setPermissionVerify(dto.getPermissionVerify());
        }
        blog.setUpdatedDate(ZonedDateTime.now());
        blogRepository.save(blog);
    }
    private String changeContentImg(String content) {
        // 提取其中的图片部分
        Pattern pattern =Pattern.compile("<img.*>");//匹配的模式
        Matcher matcher=pattern.matcher(content);
        while(matcher.find()){
            //TODO 提取base64的格式
            String[] split = "".split("base64,");
            String substring = split[1].substring(0, split[1].lastIndexOf("\""));
            substring = substring.substring(0, substring.lastIndexOf("'"));
            decryptByBase64(substring,"");

            //base64转为文件图片。写入上传
            boolean b = decryptByBase64(substring, "");
            if(b){
                //将提取的标签位置，删除，写入<p><firgure></firgure></p>的格式
                // 存储uaafile
            }else{

            }


        }
        return content;
    }

    public boolean decryptByBase64(String base64, String filePath) {
        if (base64 == null && filePath == null) {
            return false;
        }
        try {
            Files.write(Paths.get(filePath), Base64.getDecoder().decode(base64), StandardOpenOption.CREATE);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
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

package uaa.service.app.blog;

import org.apache.commons.lang.StringUtils;
import org.neo4j.unsafe.impl.batchimport.cache.idmapping.string.Radix;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.config.Constants.PERMISSION_TYPE;
import uaa.domain.app.blog.AppBlogBlog;
import uaa.domain.app.blog.AppBlogCategory;
import uaa.domain.app.blog.AppBlogTag;
import uaa.domain.app.log.AppLogTag;
import uaa.domain.uaa.UaaFile;
import uaa.domain.uaa.UaaUser;
import uaa.repository.app.blog.AppBlogBlogRepository;
import uaa.repository.app.blog.AppBlogCategoryRepository;
import uaa.repository.app.blog.AppBlogTagRepository;
import uaa.repository.app.log.AppLogTagRepository;
import uaa.repository.uaa.UaaFileRepository;
import uaa.service.UaaUserService;
import uaa.service.dto.app.blog.*;
import uaa.service.dto.app.log.AppLogTagDTO;
import uaa.service.dto.upload.UploadResultDTO;
import util.UUIDGenerator;
import util.Validators;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
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
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UaaFileRepository fileRepository;

    @Autowired
    private AppBlogBlogRepository blogRepository;

    @Autowired
    private AppBlogCategoryRepository appBlogCategoryRepository;

    @Autowired
    private UaaUserService uaaUserService;
    @Autowired
    private AppLogTagRepository tagRepository;
    @Autowired
    private AppBlogTagRepository blogTagRepository;


    public List<AppBlogCategoryDto> findAllBlogCategorys(){
        List<AppBlogCategory> all = appBlogCategoryRepository.findAll();
        //状态为delete的不返回
        List<AppBlogCategoryDto> categories = new ArrayList<>();
        if(all!=null){
            for ( AppBlogCategory category:all ) {
                if(Constants.SAVE.equals(category.getStatus())){
                    AppBlogCategoryDto target = new AppBlogCategoryDto();
                    BeanUtils.copyProperties(category, target);
                    target.setId(""+category.getId());
                    categories.add(target);
                }
            }
        }
        return categories;
    }
    public AppBlogCategory findCategoryByName(String name){
        AppBlogCategory category = appBlogCategoryRepository.findOneByName(name);
        return category;
    }

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
    public List<AppBlogDto> getAllBlogs(String userId, int page,int size,String categoryName){
        //如果userId为空，那么就搜索出，所以不是自己可见的
        //如果userId不为空，那么就搜索该用户下的全部
        Pageable pageable = new PageRequest(page, size, Sort.Direction.ASC, "createdDate");
        List<AppBlogDto> returnList = new ArrayList<>();
//        Page<AppBlogBlog> allByCreateIdAndStatus=null;
//        List<AppBlogBlog> allByCreateIdAndStatus=null;

        // categoryName获取到分类的ID，从分类ID，删选blog集
        String categoryId= "";
        if(StringUtils.isNotBlank(categoryName)&&!"默认".equals(categoryName)){
            AppBlogCategory category = appBlogCategoryRepository.findOneByName(categoryName);
            if(category!=null){
                categoryId = ""+category.getId();
            }
        }
        String finalCategoryId = categoryId;
//        allByCreateIdAndStatus = blogRepository.findAll(new Specification<AppBlogBlog>() {
//            @Override
//            public Predicate toPredicate(Root<AppBlogBlog> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> predicates = new ArrayList<>();
//                predicates.add(criteriaBuilder.equal(root.get("status").as(String.class), Constants.SAVE));
//                if(StringUtils.isNotBlank(finalCategoryId)){
//                    predicates.add(criteriaBuilder.equal(root.get("categoryId").as(String.class), finalCategoryId));
//
//                }
//                if(StringUtils.isBlank(userId)){
//                    predicates.add(criteriaBuilder.notEqual(root.get("permissionType").as(String.class), PERMISSION_TYPE.OnlyOne.name()));
//                }else{
//                    // 只会查询创建者为userId的
//                    predicates.add(criteriaBuilder.equal(root.get("createId").as(String.class), userId));
//                }
//
//                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
//            }
//        },pageable);

//        blogRepository.findAllByCreateIdAndStatusOrderByCreatedDate(pageable,userId,Constants.SAVE)

        StringBuilder sql = new StringBuilder("select y.id, y.CATEGORY_ID,y.CREATED_DATE,y.CREATE_ID,y.PERMISSION_TYPE,y.PERMISSION_VERIFY,"
            +"y.READ_COUNT,y.SOURCE_TYPE,y.`STATUS`,y.TITLE,y.TITLE_IMAGE_FILE_ID,y.UPDATED_DATE from app_blog_blog y "
            + "where y.`STATUS` = '"+Constants.SAVE+"' ");
        if(StringUtils.isNotBlank(finalCategoryId)) {
            sql.append(" and y.CATEGORY_ID='" + finalCategoryId+ "' ");
        }
        if(StringUtils.isBlank(userId)) {
            sql.append(" and y.PERMISSION_TYPE!='" + PERMISSION_TYPE.OnlyOne.name()+ "' ");
        }else {
            sql.append(" and y.CREATE_ID!='" + userId+ "' ");
        }
        List<Map<String, Object>> result = jdbcTemplate.queryForList(sql.toString());


//        if(allByCreateIdAndStatus!=null&&allByCreateIdAndStatus.hasContent()){
//                Iterator<AppBlogBlog> iterator = allByCreateIdAndStatus.iterator();
        if(result!=null&&result.size()>0)
            for(Map<String,Object> one: result){
//            while (iterator.hasNext()){
//                AppBlogBlog one =  iterator.next();
                AppBlogDto dto=new AppBlogDto();
                //获取全部的时候内容 提取
//                dto.setContent(one.getContent());
//                dto.setPreviewContent(getPreviewContent(one.getContent()));
//                dto.setPreviewContent(one.getContent());
                dto.setId(one.get("id").toString());
                dto.setReadCount(one.get("readCount")==null?0:Integer.parseInt(one.get("readCount").toString()));
                dto.setCreatedDate(one.get("createdDate")==null?null:(ZonedDateTime)(one.get("createdDate")));
                dto.setTitle(one.get("title")==null?"默认标题":(one.get("title").toString()));
                dto.setPermissionType(one.get("permissionType")==null?"默认权限":(one.get("permissionType").toString()));
                dto.setUpdatedDate(one.get("updatedDate")==null?null:(ZonedDateTime)(one.get("updatedDate")));

                dto.setCategoryId(one.get("categoryId")==null?"":one.get("categoryId").toString());

//                TODO 为了优化速度，忽视
                if(one.get("createId")!=null) {
                    String createId = one.get("createId").toString();
                    UaaUser userById = uaaUserService.findUserById(createId);
                    dto.setAuthorId(userById==null?"":createId);
                    dto.setAuthorName(userById==null?"匿名":userById.getName());
                }

                //得到封面图片文件 TODO 为了优化速度，忽视
                if(one.get("titleImageFileId")!=null&&StringUtils.isNotBlank(one.get("titleImageFileId").toString())){
                    UaaFile titleImage = fileRepository.findOne(one.get("titleImageFileId").toString());
                    if(titleImage!=null){
                        UploadResultDTO uploadResult = new UploadResultDTO();
                        uploadResult.setId(titleImage.getId());
                        uploadResult.setUploadFileName(titleImage.getName());
                        uploadResult.setName(titleImage.getRelFilePath().substring(1,titleImage.getRelFilePath().length()));
                        uploadResult.setPath(titleImage.getRootFilePath()+titleImage.getRelFilePath());
                        dto.setTitleImg(uploadResult);
                    }
                }


                //查看标签 TODO 为了优化速度，忽视
//                List<AppBlogTag> allByBaseIdAndStatus = blogTagRepository.findAllByBaseIdAndStatus(one.getId(), Constants.SAVE);
//                List<AppLogTagDTO> tagDTOList = new ArrayList<>();
//                if(allByBaseIdAndStatus!=null&&allByBaseIdAndStatus.size()>0){
//                    for(AppBlogTag blogTag:allByBaseIdAndStatus){
//                        AppLogTag tag = tagRepository.findOne(blogTag.getTagId());
//                        if(tag!=null){
//                            AppLogTagDTO tagDTO = new AppLogTagDTO();
//                            tagDTO.setId(tag.getId());
//                            tagDTO.setName(tag.getName());
//                            tagDTO.setGroup(tag.getGroup());
//                            tagDTO.setType(tag.getType());
//                            tagDTOList.add(tagDTO);
//                        }
//                    }
//                }
//                dto.setTagList(tagDTOList);
                returnList.add(dto);
//            }
        }
        return returnList;
    }

    /**
     * 得到富文本的预览内容
     * @param content
     * @return
     */
    private static String getPreviewContent(String content) {

        StringBuffer returnStr = new StringBuffer();
        String[] split = content.split("</h2>");
        if(split[0].startsWith("<h2>")){
            split[0] = split[0].substring("<h2>".length(), split[0].length() - 1);
        }
        if(split.length>1){
            String[] splitP = split[1].split("</p>");
            int k = splitP.length;
            while (returnStr.length()<=Constants.PREVIEW_LENGTH||k>0){
                String tmp = splitP[splitP.length-k];
                if(tmp.startsWith("<p>")){
                    tmp = tmp.substring("<p>".length(), tmp.length() - 1);
                }
                if(tmp.contains("<img")){
                    String[] split1 = tmp.split("<img.*>");
                    StringBuffer tmpS = new StringBuffer();
                    for(int i=0;i<split1.length;i++){
                        tmpS.append(split1[i]);
                    }
                    tmp = tmpS.toString();
                }
                returnStr.append("\n"+tmp);
                k--;
            }
        }else{
            returnStr.append(split[0]);
        }
        return returnStr.toString();
//        return "";
    }

//    public static void main(String[] args){
//        String tmp = "<h2>这是博客的内容</h2><p>“为什么你要这样？”某处旅馆，一个人声音有点颤抖。</p><p>“...”另一个人背对着他，低头吸着烟，沉默不语。</p><p>“为什么你要这样？”</p>";
//        getPreviewContent(tmp);
//    }
    public AppBlogDto getBlog(String id,String userId,String verify,String source){
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
            if("WX".equals(source)){
                // TODO 如果是wx的，那么去掉图片的（做正则替换)
                // <img src=
                // >
                String regex = "<img src=[^>]*>";
                String content = one.getContent();
                dto.setContent(content.replaceAll(regex,"<div><b>***很抱歉,这张图片在微信无法显示!***</b></div>"));

            }else{
                dto.setContent(one.getContent());
            }
            dto.setReadCount(one.getReadCount());
            dto.setTitle(one.getTitle());
            dto.setId(one.getId());
            dto.setCreatedDate(one.getCreatedDate());
            dto.setPermissionType(one.getPermissionType());
            dto.setUpdatedDate(one.getUpdatedDate());
            String createId = one.getCreateId();
            UaaUser userById = uaaUserService.findUserById(createId);
            dto.setAuthorId(userById==null?"":createId);
            dto.setCategoryId(one.getCategoryId());
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
        blog.setCategoryId(dto.getCategoryId());
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

    public void updateBlogCategory(AppBlogBlog blog, AppBlogUpdateCategoryDto dto) {
        blog.setCategoryId(dto.getCategoryId());
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

    public void createCategory(AppBlogCategoryDto dto,String userId) {
        AppBlogCategory category = new AppBlogCategory();
//        category.setId(UUIDGenerator.getUUID());
        category.setName(dto.getName());
        category.setCreatedNickName("创建者昵称");
        category.setCreatedDate(ZonedDateTime.now());
        category.setUpdatedDate(ZonedDateTime.now());
        category.setStatus(Constants.SAVE);
        category.setCreatedId(userId);
        String nickName = uaaUserService.findUserById(userId).getNickName();
        if(nickName==null)
            nickName = uaaUserService.findUserById(userId).getName();
        category.setCreatedNickName(nickName);
        appBlogCategoryRepository.save(category);

    }

    public AppBlogCategory getCategory(String id) {
        AppBlogCategory one = appBlogCategoryRepository.findOne(id);
        if(one!=null&&Constants.SAVE.equals(one.getStatus()))
            return one;
        return null;
    }

    public void deleteCategory(AppBlogCategory category) {
//        appBlogCategoryRepository.delete(category);
        category.setStatus(Constants.DELETE);
        appBlogCategoryRepository.save(category);
    }
    //TODO 1。找出全部分类 2。按分类找博客 3。博客设置分类
}

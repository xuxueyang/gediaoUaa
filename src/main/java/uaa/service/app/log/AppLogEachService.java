package uaa.service.app.log;


import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.domain.app.log.AppLogDetail;
import uaa.domain.app.log.AppLogEach;
import uaa.domain.app.log.AppLogEachTag;
import uaa.domain.app.log.AppLogTag;
import uaa.repository.app.log.AppLogDetailRepository;
import uaa.repository.app.log.AppLogEachRepository;
import uaa.repository.app.log.AppLogEachTagRepository;
import uaa.repository.app.log.AppLogTagRepository;
import uaa.service.dto.app.log.*;
import util.UUIDGenerator;
import util.Validators;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.time.ZonedDateTime;
import java.util.*;

@Transactional
@Service
public class AppLogEachService {

    @Autowired
    private AppLogEachRepository appLogEachRepository;

    @Autowired
    private AppLogDetailService appLogDetailService;

    @Autowired
    private AppLogEachTagRepository appLogEachTagRepository;

    @Autowired
    private AppLogTagRepository appLogTagRepository;

    public AppLogEachDTO getEachInfoById(String id){
        if(id==null||"".equals(id)){
            return null;
        }
        AppLogEach appLogEach = appLogEachRepository.findOne(id);
        if(appLogEach==null||Constants.APP_LOG_STATUS_DELETE.equals(appLogEach.getStatus())){
            return null;
        }
        return prepareEachEntityToDTO(appLogEach);
    }
    private AppLogEachDTO prepareEachEntityToDTO(AppLogEach appLogEach){
        AppLogEachDTO appLogEachDTO = new AppLogEachDTO();
        appLogEachDTO.setUpdatedDate(appLogEach.getUpdatedDate());
        appLogEachDTO.setCreatedDate(appLogEach.getCreatedDate());
        List<AppLogDetailDTO> detailDTOList = new ArrayList<>();
        //从关联表里找出数据，插入
        for(AppLogDetail appLogDetail:appLogEach.getDetails()){
            //遍历塞入数据
            detailDTOList.add(appLogDetailService.prepareDetailEntityToDTO(appLogDetail,false));
        }
        appLogEachDTO.setAppLogDetailDTOList(detailDTOList);

        //塞入分类数据
        Map<String,AppLogTagDTO> tagMap = new HashMap<>();
        Set<AppLogEachTag> eachTags = appLogEach.getTags();
        for(AppLogEachTag tag:eachTags){
            if(Validators.fieldBlank(tag.getTagId())){
                continue;
            }
            AppLogTag one = appLogTagRepository.findOne(tag.getTagId());
            if(one!=null){
                AppLogTagDTO appLogTagDTO = new AppLogTagDTO();
                appLogTagDTO.setType(one.getType());
                appLogTagDTO.setGroup(one.getGroup());
                appLogTagDTO.setName(one.getName());
                tagMap.put(one.getId(),appLogTagDTO);
            }
        }
        appLogEachDTO.setTagMap(tagMap);

        //塞入自己的数据
        appLogEachDTO.setId(appLogEach.getId());
        appLogEachDTO.setBelongDate(appLogEach.getBelongDate());
        appLogEachDTO.setMessage(appLogEach.getMessage());
        appLogEachDTO.setTitle(appLogEach.getTitle());
        appLogEachDTO.setStatus(appLogEach.getType());
//        appLogEachDTO.setAppLogDetailDTOList();
        return appLogEachDTO;
    }

    public AppLogEach findEachById(String id){
        AppLogEach one = appLogEachRepository.findOne(id);
        if(one==null||
            Constants.APP_LOG_STATUS_DELETE.equals(one.getStatus())){
            return null;
        }
        return one;
    }
    public List<AppLogEach> findAllEachByUserId(String userId){
        return appLogEachRepository.findAllByCreatedIdAndStatusNotOrderByBelongDate(userId,Constants.APP_LOG_STATUS_DELETE);
    }
    public void updateEach(AppLogEach appLogEach,UpdateLogEachDTO updateLogEachDTO,String updateId) {
        appLogEach.setBelongDate(updateLogEachDTO.getBelongDate());
        appLogEach.setTitle(updateLogEachDTO.getTitle());
        appLogEach.setUpdatedID(updateId);
        if(StringUtils.isNotBlank(updateLogEachDTO.getTomatoType()))
            appLogEach.setTomatoType(updateLogEachDTO.getTomatoType());
        appLogEach.setType(updateLogEachDTO.getStatus());
        appLogEach.setMessage(updateLogEachDTO.getMessage());
        appLogEach.setUpdatedDate(ZonedDateTime.now());
        //对于分类进行修改
        if(updateLogEachDTO.getTags()==null||updateLogEachDTO.getTags().size()==0){
            //说明全部删除
            Set<AppLogEachTag> tags = appLogEach.getTags();
            for(AppLogEachTag tag:tags){
                tag.setStatus(Constants.APP_LOG_STATUS_DELETE);
                appLogEachTagRepository.save(tag);
            }
        }else {
            Set<AppLogEachTag> tags = appLogEach.getTags();
            for(AppLogEachTag tag:tags){
                if(!updateLogEachDTO.getTags().contains(tag.getId())){
                    tag.setStatus(Constants.APP_LOG_STATUS_DELETE);
                    appLogEachTagRepository.save(tag);
                }else {
                    updateLogEachDTO.getTags().remove(tag.getId());
                }
            }
            //对于未存在的，需要创建
            for(String tagId:updateLogEachDTO.getTags()){
                AppLogEachTag appLogEachTag = new AppLogEachTag();
                appLogEachTag.setStatus(Constants.APP_LOG_STATUS_SAVE);
                appLogEachTag.setTagId(tagId);
                appLogEachTag.setId(UUIDGenerator.getUUID());
                appLogEachTag.setLogEachId(appLogEach.getId());
                appLogEachTagRepository.save(appLogEachTag);
            }
        }
        appLogEachRepository.save(appLogEach);
    }

    public AppLogEachDTO createEach(CreateLogEachDTO createLogEachDTO, String createdid) {
        AppLogEach appLogEach = new AppLogEach();
        appLogEach.setCreatedId(createdid);
        appLogEach.setUpdatedID(createdid);
        appLogEach.setType(createLogEachDTO.getStatus());
        appLogEach.setMessage(createLogEachDTO.getMessage());
        appLogEach.setTitle(createLogEachDTO.getTitle());
        appLogEach.setBelongDate(createLogEachDTO.getBelongDate());
        appLogEach.setTomatoType(createLogEachDTO.getTomatoType());
        appLogEach.setStatus(Constants.APP_LOG_STATUS_SAVE);
        appLogEach.setId(UUIDGenerator.getUUID());
        appLogEachRepository.save(appLogEach);
        //插入到分类表中（传的是名字——id有详细的附加信息好处，但是名字可以在没有的时候添加，也不错)
        Set<AppLogEachTag> tagSet = new HashSet<>();
        if(createLogEachDTO.getTags()!=null||createLogEachDTO.getTags().size()>0){
            //也可能是ID的（或者说--现在塞入的都是ID）
            for(String tagName:createLogEachDTO.getTags()){
                AppLogTag one = appLogTagRepository.findOneByNameAndCreatedId(tagName,createdid);
                if(one==null){
                    //可能因为传入的是ID，再搜素一下
                    one = appLogTagRepository.findOneByIdAndCreatedId(tagName,createdid);
                }
                if(one==null){
                    //创建标签，并且设置到关联中
                    one = new AppLogTag();
                    one.setId(UUIDGenerator.getUUID());
                    one.setName(tagName);
                    //组合type设置为默认
                    one.setUpdatedId(createdid);
                    one.setStatus(Constants.APP_LOG_STATUS_SAVE);
                    one.setVersion("0");
                    one.setCreatedId(createdid);
                    appLogTagRepository.save(one);
                }else{
                    if(Constants.APP_LOG_STATUS_DELETE.equals(one.getStatus())){
                        one.setStatus(Constants.APP_LOG_STATUS_SAVE);
                        appLogTagRepository.save(one);
                    }
                }
                {
                    AppLogEachTag appLogEachTag = new AppLogEachTag();
                    appLogEachTag.setStatus(Constants.APP_LOG_STATUS_SAVE);
                    appLogEachTag.setLogEachId(appLogEach.getId());
                    appLogEachTag.setTagId(one.getId());
                    appLogEachTag.setId(UUIDGenerator.getUUID());
                    appLogEachTagRepository.save(appLogEachTag);
                    tagSet.add(appLogEachTag);
                }
            }
        }
        appLogEach.setTags(tagSet);
        return prepareEachEntityToDTO(appLogEach);
    }

    public void deleteEach(AppLogEach appLogEach) {
        //删除logeach以及附带的logdetail
        Set<AppLogDetail> details = appLogEach.getDetails();
        for(AppLogDetail appLogDetail:details){
            appLogDetail.setStatus(Constants.APP_LOG_STATUS_DELETE);
            appLogDetailService.saveDetail(appLogDetail);
        }
//        Set<AppLogEachTag> tags = appLogEach.getTags();
//        for(AppLogEachTag tag:tags){
//            tag.setStatus(Constants.APP_LOG_STATUS_DELETE);
//            appLogEachTagRepository.save(tag);
//        }
        appLogEach.setUpdatedDate(ZonedDateTime.now());
        appLogEach.setStatus(Constants.APP_LOG_STATUS_DELETE);
        appLogEachRepository.save(appLogEach);
    }

    public Map<String,Object> getAllEach(String userId, String startDate,
                                          String endDate, String searchContext, String type, String tagId,
                                          Pageable pageable,String tomatoType) {

        Specification<AppLogEach> specification = new Specification<AppLogEach>() {
            @Override
            public Predicate toPredicate(Root<AppLogEach> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.notEqual(root.get("status").as(String.class), Constants.APP_LOG_STATUS_DELETE));
                predicates.add(criteriaBuilder.equal(root.get("createdId").as(String.class), userId));

                if(StringUtils.isNotBlank(tomatoType)){
                    predicates.add(criteriaBuilder.equal(root.get("tomatoType").as(String.class), tomatoType));
                }

                if (Validators.fieldNotBlank(searchContext)) {
                    //模糊查找
                    List<Predicate> content = new ArrayList<>();
                    content.add(criteriaBuilder.like(root.get("message").as(String.class), "%" + searchContext + "%"));
                    content.add(criteriaBuilder.like(root.get("title").as(String.class), "%" + searchContext + "%"));
                    Predicate andContent = criteriaBuilder.or(content.toArray(new Predicate[content.size()]));
                    predicates.add(andContent);
                }
                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("updatedDate").as(ZonedDateTime.class)));
                //type不为空，且不为默认
                if (Validators.fieldNotBlank(type) && !"0".equals(type)) {
                    //状态
                    predicates.add(criteriaBuilder.equal(root.get("type").as(String.class), type));
                    if(!Constants.LogEach_Type.UNFinished.equals(type)&&!Constants.LogEach_Type.Mem.equals(type)){
                        if (Validators.fieldNotBlank(startDate) && Validators.verifyBelongDate(startDate)) {
                            //对于备忘录和未完成的type，是不会考虑时间的
                            //大于或等于传入时间
                            predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("belongDate").as(String.class), startDate));
                        }
                        if (Validators.fieldNotBlank(endDate) && Validators.verifyBelongDate(endDate)) {
                            //小于或等于传入时间
                            predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("belongDate").as(String.class), endDate));
                        }
                    }
                    return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                }else{
                    //如果没有type的话，还是(因为发现未完成数目可能很多，所以，这里，仅仅做备忘录的修改）
                    if (Validators.fieldNotBlank(startDate) && Validators.verifyBelongDate(startDate)) {
                        //对于备忘录和未完成的type，是不会考虑时间的
                        //大于或等于传入时间
                        predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("belongDate").as(String.class), startDate));
                    }
                    if (Validators.fieldNotBlank(endDate) && Validators.verifyBelongDate(endDate)) {
                        //小于或等于传入时间
                        predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("belongDate").as(String.class), endDate));
                    }
                    Predicate noType = criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
                    //未完成的内容+type搜索即可
                    List<Predicate> unfinished = new ArrayList<>();
                    unfinished.add(criteriaBuilder.equal(root.get("type").as(String.class), Constants.LogEach_Type.UNFinished));
                    unfinished.add(criteriaBuilder.equal(root.get("type").as(String.class), Constants.LogEach_Type.Mem));
                    Predicate unfinisedType = criteriaBuilder.or(unfinished.toArray(new Predicate[unfinished.size()]));

                    //
                    if (Validators.fieldNotBlank(searchContext)) {
                        List<Predicate> content = new ArrayList<>();
                        //模糊查找
                        content.add(criteriaBuilder.like(root.get("message").as(String.class), "%" + searchContext + "%"));
                        content.add(criteriaBuilder.like(root.get("title").as(String.class), "%" + searchContext + "%"));
                        Predicate andContent = criteriaBuilder.or(content.toArray(new Predicate[content.size()]));
                        unfinisedType = criteriaBuilder.and(unfinisedType,andContent);
                    }
                    //附加基本的查找
                    List<Predicate> basePList = new ArrayList<>();
                    basePList.add(criteriaBuilder.notEqual(root.get("status").as(String.class), Constants.APP_LOG_STATUS_DELETE));
                    basePList.add(criteriaBuilder.equal(root.get("createdId").as(String.class), userId));
                    Predicate base = criteriaBuilder.and(basePList.toArray(new Predicate[basePList.size()]));

                    unfinisedType = criteriaBuilder.and(unfinisedType,base);

                    return criteriaBuilder.or(noType,unfinisedType);
                }

            }
        };
        Page<AppLogEach> allPage = appLogEachRepository.findAll(specification,pageable);
        List<AppLogEach> all = allPage.getContent();
        List<AppLogEachDTO> dtoList = new ArrayList<>();
        //根据日期排序
        if(all!=null&&all.size()>0){
            for(AppLogEach appLogEach: all){
                if(Validators.fieldNotBlank(tagId)){
                    Set<AppLogEachTag> tags = appLogEach.getTags();
                    if(tags!=null&&tags.size()>0){
                        for(AppLogEachTag tag:tags){
                            if(tagId.equals(tag.getTagId())){
                                dtoList.add(prepareEachEntityToDTO(appLogEach));
                                break;
                            }
                        }
                    }
                }else{
                    dtoList.add(prepareEachEntityToDTO(appLogEach));
                }

            }
        }
        Map<String,Object> map = new HashMap();
        map.put("data",dtoList);
        map.put("totalPages",allPage.getTotalPages());
        map.put("totalElements",allPage.getTotalElements());
        map.put("number",allPage.getNumber());
        map.put("size",allPage.getSize());
        map.put("numberOfElements",allPage.getNumberOfElements());
        return map;

    }

//    public List<AppLogEachDTO> getAllEach(String userId) {
//        List<AppLogEach> allByCreatedId = appLogEachRepository.findAllByCreatedIdAndStatusNotOrderByUpdatedDateDesc(userId,Constants.APP_LOG_STATUS_DELETE);
//        List<AppLogEachDTO> dtoList = new ArrayList<>();
//        //根据日期排序
//        if(allByCreatedId!=null&&allByCreatedId.size()>0){
//            for(AppLogEach appLogEach: allByCreatedId){
//                dtoList.add(prepareEachEntityToDTO(appLogEach));
//            }
//        }
//        return dtoList;
//    }

}

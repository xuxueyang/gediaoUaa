package uaa.service.app.log;


import org.springframework.beans.factory.annotation.Autowired;
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
            detailDTOList.add(appLogDetailService.prepareDetailEntityToDTO(appLogDetail));
        }
        appLogEachDTO.setAppLogDetailDTOList(detailDTOList);

        //塞入分类数据
        Map<String,AppLogTagDTO> tagMap = new HashMap<>();
        Set<AppLogEachTag> eachTags = appLogEach.getTags();
        for(AppLogEachTag tag:eachTags){
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
        appLogEach.setStatus(Constants.APP_LOG_STATUS_N);
        appLogEach.setId(UUIDGenerator.getUUID());
        appLogEachRepository.save(appLogEach);
        //插入到分类表中（传的是名字——id有详细的附加信息好处，但是名字可以在没有的时候添加，也不错)
        Set<AppLogEachTag> tagSet = new HashSet<>();
        if(createLogEachDTO.getTags()!=null||createLogEachDTO.getTags().size()>0){
            for(String tagName:createLogEachDTO.getTags()){
                AppLogTag one = appLogTagRepository.findOneByNameAndCreatedId(tagName,createdid);
                if(one==null){
                    //创建标签，并且设置到关联中
                    one = new AppLogTag();
                    one.setId(UUIDGenerator.getUUID());
                    one.setName(tagName);
                    //组合type设置为默认
                    one.setUpdatedId(createdid);
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

    public List<AppLogEachDTO> getAllEach(String userId,String startDate,String endDate,String searchContext,String type) {
        List<AppLogEach> all = appLogEachRepository.findAll(new Specification<AppLogEach>() {
            @Override
            public Predicate toPredicate(Root<AppLogEach> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(criteriaBuilder.notEqual(root.get("status").as(String.class),Constants.APP_LOG_STATUS_DELETE));
                predicates.add(criteriaBuilder.equal(root.get("createdId").as(String.class),userId));

                if (Validators.fieldNotBlank(startDate)&&Validators.verifyBelongDate(startDate)) {
                    //大于或等于传入时间
                    predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("belongDate").as(String.class), startDate));
                }
                if (Validators.fieldNotBlank(endDate)&&Validators.verifyBelongDate(endDate)) {
                    //小于或等于传入时间
                    predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("belongDate").as(String.class), endDate));
                }
                if (Validators.fieldNotBlank(searchContext)) {
                    //模糊查找
                    predicates.add(criteriaBuilder.like(root.get("message").as(String.class), "%" + searchContext + "%"));
                    predicates.add(criteriaBuilder.like(root.get("title").as(String.class), "%" + searchContext + "%"));
//                    //TODO 标签也在模糊搜索中 以后加吧--
//                    String tagSearch = "select * from app_log_each where id in"+
//                                "("+
//                                " select eachTag.BASE_ID from app_log_each_tag  eachTag where TAG_ID in "
//                                    + "("+"select tag.id from app_log_tag tag where name like" +"%"+searchContext+"%" +")"
//                                +")"
//                        ;

                }
                if (Validators.fieldNotBlank(type)) {
                    //状态
                    predicates.add(criteriaBuilder.equal(root.get("type").as(String.class), type));

                }
                    // and到一起的话所有条件就是且关系，or就是或关系
                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
        List<AppLogEachDTO> dtoList = new ArrayList<>();
        //根据日期排序
        if(all!=null&&all.size()>0){
            for(AppLogEach appLogEach: all){
                dtoList.add(prepareEachEntityToDTO(appLogEach));
            }
        }
        return dtoList;

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

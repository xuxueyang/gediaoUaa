package uaa.service.app.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.domain.app.log.AppLogDay;
import uaa.domain.app.log.AppLogDayTag;
import uaa.domain.app.log.AppLogTag;
import uaa.repository.app.log.AppLogDayRepository;
import uaa.repository.app.log.AppLogDayTagRepository;
import uaa.repository.app.log.AppLogTagRepository;
import uaa.service.dto.app.log.*;
import util.UUIDGenerator;
import util.Validators;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;

/**
 * Created by UKi_Hi on 2018/6/4.
 */
@Service
@Transactional
/**
 * 含有day、标签这种的添加
 */
public class AppLogSingleService {
    @Autowired
    private AppLogDayRepository appLogDayRepository;

    @Autowired
    private AppLogTagRepository appLogTagRepository;

    @Autowired
    private AppLogDayTagRepository appLogDayTagRepository;

    public AppLogDay findDayBy(String id) {
        return appLogDayRepository.findOne(id);
    }

    public void deleteDay(AppLogDay appLogDay) {
        appLogDay.setStatus(Constants.APP_LOG_STATUS_DELETE);
        appLogDayRepository.save(appLogDay);
    }

    public AppLogDayDTO createDay(CreateLogDayDTO createLogDayDTO, String createdid) {
        AppLogDay appLogDay = new AppLogDay();
        appLogDay.setId(UUIDGenerator.getUUID());
        appLogDay.setStatus(Constants.APP_LOG_STATUS_SAVE);
        appLogDay.setBelongDate(createLogDayDTO.getBelongDate());
        appLogDay.setTitle(createLogDayDTO.getTitle());
        appLogDay.setCreatedId(createdid);
        appLogDay.setType(createLogDayDTO.getType());
        appLogDay.setUpdatedId(createdid);
        appLogDay.setMessage(createLogDayDTO.getMessage());
        //设置标签
        Set<AppLogDayTag> set = new HashSet<>();
        if(createLogDayDTO.getTags()!=null&&createLogDayDTO.getTags().size()>0){
            for(String id:createLogDayDTO.getTags()){
                //如果存在就设置（反正标签是逻辑删除）
                AppLogTag tag = appLogTagRepository.findOne(id);
                if(tag!=null){
                    //塞入关联表
                    AppLogDayTag appLogDayTag = new AppLogDayTag();
                    appLogDayTag.setLogDayId(appLogDay.getId());
                    appLogDayTag.setTagId(tag.getId());
                    appLogDayTagRepository.save(appLogDayTag);
                    set.add(appLogDayTag);

                }
            }
        }

        appLogDay.setTags(set);
        appLogDayRepository.save(appLogDay);
        AppLogDayDTO dto  =  prepareDayEntityToDTO(appLogDay);

        return dto;
    }
    public AppLogDayDTO prepareDayEntityToDTO(AppLogDay appLogDay){
        AppLogDayDTO dto = new AppLogDayDTO();
        dto.setId(appLogDay.getId());
        dto.setBelongDate(appLogDay.getBelongDate());
        dto.setMessage(appLogDay.getMessage());
        dto.setUpdatedDate(appLogDay.getUpdatedDate());
        dto.setCreatedDate(appLogDay.getCreatedDate());
        dto.setTitle(appLogDay.getTitle());
        Set<AppLogDayTag> tags = appLogDay.getTags();

        Map<String,AppLogTagDTO> tagDTOMap = new HashMap<>();
        if(tags!=null&&tags.size()>0){
            for(AppLogDayTag dayTag: tags){
                AppLogTag tag = appLogTagRepository.findOne(dayTag.getId());
                if(tag!=null){
                    AppLogTagDTO tagDTO = prepareTagEntityToDTO(tag);
                    tagDTOMap.put(tag.getId(),tagDTO);
                }
            }
        }
        dto.setTagMap(tagDTOMap);
        return dto;
    }

    public List<AppLogDayDTO> fingAllDayByBelongDate(String belongDate) {
        List<AppLogDayDTO> list  = new ArrayList<>();
        List<AppLogDay> allByBelongDateAndStatus = appLogDayRepository.findAllByBelongDateAndStatus(belongDate, Constants.APP_LOG_STATUS_SAVE);
        if(allByBelongDateAndStatus!=null||allByBelongDateAndStatus.size()>0){
            for(AppLogDay day:allByBelongDateAndStatus){
                list.add(prepareDayEntityToDTO(day));
            }
        }
        return list;
    }

    public void updateDay(AppLogDay appLogDay, UpdateLogDayDTO updateLogDayDTO,String updateId) {
        appLogDay.setBelongDate(updateLogDayDTO.getBelongDate());
        appLogDay.setTitle(updateLogDayDTO.getTitle());
        appLogDay.setUpdatedId(updateId);
        appLogDay.setMessage(updateLogDayDTO.getMessage());
        //对于分类进行修改
        if(updateLogDayDTO.getTags()==null||updateLogDayDTO.getTags().size()==0){
            //说明全部删除
            Set<AppLogDayTag> tags = appLogDay.getTags();
            for(AppLogDayTag tag:tags){
                tag.setStatus(Constants.APP_LOG_STATUS_DELETE);
                appLogDayTagRepository.save(tag);
            }
        }else {
            Set<AppLogDayTag> tags = appLogDay.getTags();
            for(AppLogDayTag tag:tags){
                if(!updateLogDayDTO.getTags().contains(tag.getId())){
                    tag.setStatus(Constants.APP_LOG_STATUS_DELETE);
                    appLogDayTagRepository.save(tag);
                }else {
                    updateLogDayDTO.getTags().remove(tag.getId());
                }
            }
            //对于未存在的，需要创建
            for(String tagId:updateLogDayDTO.getTags()){
                AppLogDayTag appLogEachTag = new AppLogDayTag();
                appLogEachTag.setStatus(Constants.APP_LOG_STATUS_SAVE);
                appLogEachTag.setTagId(tagId);
                appLogEachTag.setLogDayId(appLogDay.getId());
                appLogDayTagRepository.save(appLogEachTag);
            }
        }
        appLogDayRepository.save(appLogDay);
    }

    public AppLogTag findTagBy(String id) {
        AppLogTag one = appLogTagRepository.findOne(id);
        if(one==null||Constants.APP_LOG_STATUS_DELETE.equals(one.getStatus()))
            return null;
        return one;
    }


    public List<AppLogTagDTO> fingAllTag(String userId) {
        List<AppLogTag> allByStatusNot = appLogTagRepository.findAllByStatusNotAndCreatedId(Constants.APP_LOG_STATUS_DELETE,userId);
        List<AppLogTagDTO> list = new ArrayList<>();
        for(AppLogTag tag:allByStatusNot){
            list.add(prepareTagEntityToDTO(tag));
        }
        return list;
    }

    public AppLogTagDTO prepareTagEntityToDTO(AppLogTag appLogTag) {
        AppLogTagDTO appLogTagDTO = new AppLogTagDTO();
        appLogTagDTO.setType(appLogTag.getType());
        appLogTagDTO.setName(appLogTag.getName());
        appLogTagDTO.setGroup(appLogTag.getGroup());
        appLogTagDTO.setId(appLogTag.getId());
        appLogTagDTO.setUpdatedDate(appLogTag.getUpdatedDate());
        return appLogTagDTO;
    }

    public void updateTag(AppLogTag tag, UpdateLogTagDTO updateLogTagDTO, String updateId) {
        tag.setGroup(updateLogTagDTO.getGroup());
        tag.setName(updateLogTagDTO.getName());
        tag.setType(updateLogTagDTO.getType());
        tag.setUpdatedId(updateId);
        appLogTagRepository.save(tag);
    }

    public void deleteTag(AppLogTag tag) {
        tag.setStatus(Constants.APP_LOG_STATUS_DELETE);
        appLogTagRepository.save(tag);
    }

    public AppLogTagDTO createTag(CreateLogTagDTO createLogTagDTO, String createdid) {
        //先判断tag有没有，有的话忽视
//        List<AppLogTag> lists = null;
//
//        appLogTagRepository.findAllByTypeAndGroupAndName(createLogTagDTO.getType(), createLogTagDTO.getGroup(), createLogTagDTO.getName());
//        appLogTagRepository.findAll(new Specification<AppLogTag>() {
//            @Override
//            public Predicate toPredicate(Root<AppLogTag> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
//                List<Predicate> predicates = new ArrayList<>();
//                predicates.add(criteriaBuilder.equal(root.get("name").as(String.class),createLogTagDTO.getName()));
//                if (Validators.fieldNotBlank()) {
//                    //大于或等于传入时间
//                    predicates.add(cb.greaterThanOrEqualTo(root.get("commitTime").as(String.class), stime));
//                }
//                if (StringUtils.isNotBlank(etime)) {
//                    //小于或等于传入时间
//                    predicates.add(cb.lessThanOrEqualTo(root.get("commitTime").as(String.class), etime));
//                }
//
//                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
//            }
//        });
        //TODO 直接让标签不能同名好点，宁愿阔以给它多个组概念
        AppLogTag appLogTag = appLogTagRepository.findOneByNameAndCreatedId(createLogTagDTO.getName(), createdid);
        if(appLogTag!=null){
            if(Constants.APP_LOG_STATUS_DELETE.equals(appLogTag.getStatus())){
                appLogTag.setStatus(Constants.APP_LOG_STATUS_SAVE);
                appLogTagRepository.save(appLogTag);
            }
            return prepareTagEntityToDTO(appLogTag);
        }else{
            appLogTag = new AppLogTag();
            appLogTag.setId(UUIDGenerator.getUUID());
            appLogTag.setStatus(Constants.APP_LOG_STATUS_SAVE);
            appLogTag.setType(createLogTagDTO.getType()==null?"":createLogTagDTO.getType());
            appLogTag.setName(createLogTagDTO.getName());
            appLogTag.setGroup(createLogTagDTO.getGroup()==null?"":createLogTagDTO.getGroup());
            appLogTag.setCreatedId(createdid);
            appLogTag.setUpdatedId(createdid);
            appLogTag.setVersion("0");
            appLogTag.setTenantCode("0");
            appLogTagRepository.save(appLogTag);
            return prepareTagEntityToDTO(appLogTag);
        }
    }
}

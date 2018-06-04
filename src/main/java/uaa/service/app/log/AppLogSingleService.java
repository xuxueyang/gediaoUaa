package uaa.service.app.log;

import org.springframework.beans.factory.annotation.Autowired;
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
        dto.setMessage(appLogDay.getMessage());
        dto.setUpdatedDate(appLogDay.getUpdateDate());
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


    public List<AppLogTagDTO> fingAllTag() {
        List<AppLogTag> allByStatusNot = appLogTagRepository.findAllByStatusNot(Constants.APP_LOG_STATUS_DELETE);
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
        AppLogTag appLogTag = new AppLogTag();
        appLogTag.setId(UUIDGenerator.getUUID());
        appLogTag.setStatus(Constants.APP_LOG_STATUS_SAVE);
        appLogTag.setType(createLogTagDTO.getType());
        appLogTag.setName(createLogTagDTO.getName());
        appLogTag.setGroup(createLogTagDTO.getGroup());
        appLogTag.setCreatedId(createdid);
        appLogTag.setUpdatedId(createdid);
        appLogTagRepository.save(appLogTag);
        return prepareTagEntityToDTO(appLogTag);
    }
}

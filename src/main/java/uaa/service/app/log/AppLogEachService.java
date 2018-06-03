package uaa.service.app.log;


import org.springframework.beans.factory.annotation.Autowired;
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
        appLogEachDTO.setType(appLogEach.getType());
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
    public void updateEach(AppLogEach appLogEach,UpdateLogEachDTO updateLogEachDTO) {
        appLogEach.setBelongDate(updateLogEachDTO.getBelongDate());
        appLogEach.setTitle(updateLogEachDTO.getTitle());
        appLogEach.setMessage(updateLogEachDTO.getMessage());
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
        appLogEach.setMessage(createLogEachDTO.getMessage());
        appLogEach.setTitle(createLogEachDTO.getTitle());
        appLogEach.setBelongDate(createLogEachDTO.getBelongDate());
        appLogEach.setStatus(Constants.APP_LOG_STATUS_N);
        appLogEach.setId(UUIDGenerator.getUUID());
        appLogEachRepository.save(appLogEach);
        //插入到分类表中
        if(createLogEachDTO.getTags()!=null||createLogEachDTO.getTags().size()>0){
            for(String tagId:createLogEachDTO.getTags()){
                AppLogEachTag appLogEachTag = new AppLogEachTag();
                appLogEachTag.setStatus(Constants.APP_LOG_STATUS_SAVE);
                appLogEachTag.setLogEachId(appLogEach.getId());
                appLogEachTag.setTagId(tagId);
                appLogEachTagRepository.save(appLogEachTag);
            }
        }

        return prepareEachEntityToDTO(appLogEach);
    }

    public void deleteEach(AppLogEach appLogEach) {
        //删除logeach以及附带的logdetail
        Set<AppLogDetail> details = appLogEach.getDetails();
        for(AppLogDetail appLogDetail:details){
            appLogDetail.setStatus(Constants.APP_LOG_STATUS_DELETE);
            appLogDetailService.saveDetail(appLogDetail);
        }
        Set<AppLogEachTag> tags = appLogEach.getTags();
        for(AppLogEachTag tag:tags){
            tag.setStatus(Constants.APP_LOG_STATUS_DELETE);
            appLogEachTagRepository.save(tag);
        }
        appLogEach.setStatus(Constants.APP_LOG_STATUS_DELETE);
        appLogEachRepository.save(appLogEach);
    }

    public List<AppLogEachDTO> getAllEach(String userId) {
        List<AppLogEach> allByCreatedId = appLogEachRepository.findAllByCreatedId(userId);
        List<AppLogEachDTO> dtoList = new ArrayList<>();
        for(AppLogEach appLogEach: allByCreatedId){
            dtoList.add(prepareEachEntityToDTO(appLogEach));
        }
        return dtoList;
    }
}

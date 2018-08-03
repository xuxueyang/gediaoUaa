package uaa.service.app.log;


import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.domain.UaaError;
import uaa.domain.app.log.*;
import uaa.domain.uaa.UaaUser;
import uaa.repository.app.log.AppLogLogRepository;
import uaa.service.UaaUserService;
import uaa.service.dto.login.UserInfo;
import uaa.service.resource.DictService;
import uaa.web.rest.util.CommonUtil;
import uaa.web.rest.util.excel.ExcelUtil;
import uaa.web.rest.util.excel.vo.app.log.LogEachOutVO;
import util.UUIDGenerator;
import util.Validators;

import javax.servlet.http.HttpServletResponse;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@Transactional
public class AppLogStatisService {
    @Autowired
    private AppLogEachService appLogEachService;

    @Autowired
    private AppLogSingleService appLogSingleService;

    @Autowired
    private AppLogLogRepository appLogLogRepository;

    @Autowired
    private DictService dictService;


    @Autowired
    private UaaUserService uaaUserService;

    @Scheduled(cron = "1 0 0 * * ?")
    public void staticDate(){
        try{
            Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
            ca.setTime(new Date()); //设置时间为当前时间
            ca.add(Calendar.DATE, -1); //天数减1，统计昨天的
            Date lastDay = ca.getTime(); //结果
            String lastBelongDate = CommonUtil.formatDateToBelongDate(lastDay);
            //统计AppLogEach
            //得到所有的用户(ProjectType为空或格调的)
            List<UserInfo> usersGEDIAOList = uaaUserService.getUserInfosByProjectType(Constants.ProjectType.GEDIAO.name());
            List<UserInfo> usersNULLList = uaaUserService.getUserInfosByProjectType(Constants.ProjectType.NULL.name());
            if(usersGEDIAOList==null){
                usersGEDIAOList = new ArrayList<>();
            }
            if(usersNULLList!=null&&usersNULLList.size()>0){
                for(UserInfo info:usersNULLList){
                    usersGEDIAOList.add(info);
                }
            }
            if(usersGEDIAOList!=null&&usersGEDIAOList.size()>0){
                for(UserInfo info:usersGEDIAOList){
                    List<AppLogEach> allEachByUserId = appLogEachService.findAllEachByUserId(info.getId());
                    //统计昨天的未完成和已完成数目进去表(静态）
                    calculateDateData(lastBelongDate,allEachByUserId,info.getId());
                    //统计今天的进入表
                    //统计全部的已完成和未完成数目进入表（动态）//统计所有状态的数目（动态）
                    calculateDateData(null,allEachByUserId,info.getId());
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }







    //导出each
    public UaaError exportEach(UaaUser uaaUser, HttpServletResponse response){
        List<AppLogEach> allEachByUserId = appLogEachService.findAllEachByUserId(uaaUser.getId());
        if(allEachByUserId!=null&&allEachByUserId.size()>0){
            List<LogEachOutVO> outs = new ArrayList<>();
            for(AppLogEach each:allEachByUserId){
                LogEachOutVO vo = new LogEachOutVO();
                vo.setBelongDate(each.getBelongDate());
                vo.setCreatedDate(each.getCreatedDate());
                vo.setUpdatedDate(each.getUpdatedDate());
                vo.setType(dictService.getNameByTypeId(each.getType()));
                Set<AppLogEachTag> tags = each.getTags();
                if(tags!=null&&tags.size()>0){
                    Set<String> tagNames = new HashSet<>();
                    for(AppLogEachTag eachTag:tags){
                        AppLogTag tagBy = appLogSingleService.findTagBy(eachTag.getTagId());
                        if(tagBy!=null){
                            tagNames.add(tagBy.getName());
                        }
                    }
                    vo.setTags(tagNames.toString());
                }

                Set<AppLogDetail> details = each.getDetails();
                if(details!=null&&details.size()>0){

                }
                vo.setTitle(each.getTitle());
                vo.setMessage(each.getMessage());
                outs.add(vo);
            }
            ExcelUtil.writeExcel(response,outs,"格调便签"+ CommonUtil.getTodayBelongDate());
        }
        return UaaError.success();
    }

    private void calculateDateData(String belongDate,List<AppLogEach> list,String userId){
        //统计
        List<DictService.DictDTO> codeByType = dictService.getCodeByType(Constants.DictType.APP_GEDIAO_LOG_MESSAGE_STATUS.name());
        //根据code值统计
        if(codeByType!=null&&codeByType.size()>0){
            if(Validators.fieldBlank(belongDate)&&list!=null&&list.size()>0){
                //统计全部，状态
                String todayDate = CommonUtil.getTodayBelongDate();
                List<AppLogEach> today = new ArrayList<>();
                for(AppLogEach each:list){
                    if(todayDate.equals(each.getBelongDate())){
                        today.add(each);
                    }
                }
                for(DictService.DictDTO dto:codeByType){
                    //TODO 统计不同code的数目
                    int num = 0;
                    for(AppLogEach each:today){
                        if(dto.getValue().equals(each.getType())){
                            num+=1;
                        }
                    }
                    //写入数据库
                    AppLogLog appLogLog = new AppLogLog();
                    appLogLog.setStatus(Constants.LogStatus.DYNAMIC.name());
                    appLogLog.setProjectType(Constants.ProjectType.GEDIAO.name());
                    appLogLog.setBelongDate(todayDate);
                    appLogLog.setCreatedId(userId);
                    appLogLog.setUpdatedId(userId);
                    appLogLog.setDictType(Constants.DictType.APP_GEDIAO_LOG_MESSAGE_STATUS.name());
                    appLogLog.setType(dto.getValue());
                    appLogLog.setId(UUIDGenerator.getUUID());
                    appLogLog.setNum(num);
                    //塞入值
                    appLogLogRepository.save(appLogLog);
                }
            }else{
                if(list!=null&&list.size()>0){
                    List<AppLogEach> today = new ArrayList<>();
                    for(AppLogEach each:list){
                        if(belongDate.equals(each.getBelongDate())){
                            today.add(each);
                        }
                    }
                    for(DictService.DictDTO dto:codeByType){
                        //TODO 统计不同code的数目
                        int num = 0;
                        for(AppLogEach each:today){
                            if(dto.getValue().equals(each.getType())){
                                num+=1;
                            }
                        }
                        //写入数据库
                        AppLogLog appLogLog = new AppLogLog();
                        appLogLog.setStatus(Constants.LogStatus.STATIC.name());
                        appLogLog.setProjectType(Constants.ProjectType.GEDIAO.name());
                        appLogLog.setBelongDate(belongDate);
                        appLogLog.setCreatedId(userId);
                        appLogLog.setUpdatedId(userId);
                        appLogLog.setDictType(Constants.DictType.APP_GEDIAO_LOG_MESSAGE_STATUS.name());
                        appLogLog.setType(dto.getValue());
                        appLogLog.setId(UUIDGenerator.getUUID());
                        appLogLog.setNum(num);
                        //塞入值
                        appLogLogRepository.save(appLogLog);
                    }
                }
            }
        }

    }
}

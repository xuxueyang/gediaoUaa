package uaa.service.app.log;


import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.domain.UaaError;
import uaa.domain.app.log.*;
import uaa.domain.uaa.UaaUser;
import uaa.repository.app.log.AppLogEachRepository;
import uaa.repository.app.log.AppLogLogRepository;
import uaa.service.UaaUserService;
import uaa.service.dto.login.UserInfo;
import uaa.service.resource.DictService;
import uaa.web.rest.util.CommonUtil;
import uaa.web.rest.util.excel.ExcelUtil;
import uaa.web.rest.util.excel.vo.app.log.LogEachOutVO;
import util.UUIDGenerator;
import util.Validators;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@Transactional
public class AppLogStatisService {
    @Autowired
    private AppLogEachService appLogEachService;

    @Autowired
    private AppLogSingleService appLogSingleService;

    @Autowired
    private AppLogEachRepository appLogEachRepository;

    @Autowired
    private AppLogLogRepository appLogLogRepository;

    @Autowired
    private DictService dictService;


    @Autowired
    private UaaUserService uaaUserService;

    public List<Map<String,String>> getEachDayOperatorData(String createdid, String belongDate) {
        //查找出更新时间和创建时间在今天的（如果创建时间和更新时间相同，或者误差不超过1s，那么视为一个）
        //先查询出创建的（文案是在前面加上创建了XXX）
        List<AppLogEach> allByCreatedDate = appLogEachRepository.findAll(new Specification<AppLogEach>() {
            @Override
            public Predicate toPredicate(Root<AppLogEach> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ZonedDateTime zonedDateTime = CommonUtil.tranferBelongDateToZoneDate(belongDate);
                ZonedDateTime nextDay = CommonUtil.getBelongDateNextZoneDay(belongDate);
                List<Predicate> predicates = new ArrayList<>();
                //大于或等于传入时间
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("createdDate").as(ZonedDateTime.class), zonedDateTime));
                //小于或等于传入时间
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("createdDate").as(ZonedDateTime.class), nextDay));

                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate").as(ZonedDateTime.class)));

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
        List<AppLogEach> allByUpdatedDate = appLogEachRepository.findAll(new Specification<AppLogEach>() {
            @Override
            public Predicate toPredicate(Root<AppLogEach> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                ZonedDateTime zonedDateTime = CommonUtil.tranferBelongDateToZoneDate(belongDate);
                ZonedDateTime nextDay = CommonUtil.getBelongDateNextZoneDay(belongDate);
                List<Predicate> predicates = new ArrayList<>();
                //大于或等于传入时间
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("updatedDate").as(ZonedDateTime.class), zonedDateTime));
                //小于或等于传入时间
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("updatedDate").as(ZonedDateTime.class), nextDay));

                criteriaQuery.orderBy(criteriaBuilder.desc(root.get("updatedDate").as(ZonedDateTime.class)));

                return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
            }
        });
        //用set存储时间
        Map<String,AppLogEach> logMap = new HashMap<>();
        //计时：小时、分钟、秒，即可
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH-mm-ss", Locale.CHINA);
        for(AppLogEach updatedEach:allByUpdatedDate){
            logMap.put(updatedEach.getUpdatedDate().format(formatter),updatedEach);
        }
        //挡相同时，默认塞入创建了什么
        for(AppLogEach createEach:allByCreatedDate){
            logMap.put(createEach.getCreatedDate().format(formatter),createEach);
        }
        //logMap按照时间排序
        logMap = CommonUtil.sortMap(logMap);
        List<Map<String,String>>  soryList = new ArrayList<>();

        //先将这群数据按照顺序排序
        if(logMap!=null){
            for (Map.Entry<String, AppLogEach> entry : logMap.entrySet()) {
                //如果update和created一样就创建，如果updated晚created就是更新
                //如果标题为空就设置为消息体，默认最多10个字符，多的塞入...
                String createdFormat = entry.getValue().getCreatedDate().format(formatter);
                String updatedFormat = entry.getValue().getUpdatedDate().format(formatter);
                StringBuffer message = new StringBuffer();
                if(createdFormat.equals(updatedFormat)){
                    //说明同时创建
                    message.append("创建了： ");
                }else{
                    //那肯定是更新在后面
                    message.append("更新了： ");
                }
                String title = entry.getValue().getTitle();
                if(title==null||"".equals(title)){
                    title = entry.getValue().getMessage();
                    if(title==null||"".equals(title)){
                        title = "某条消息";
                    }
                }
                if(title.length()>10){
                    message.append(title.substring(0,7));
                    message.append("...");
                }else {
                    message.append(title);
                }
                Map<String,String> map = new HashMap<>();
                //塞入时间，塞入消息
                map.put("message",message.toString());
                map.put("date",updatedFormat);
                soryList.add(map);
            }
        }
        return  soryList;
    }

    @Scheduled(cron = "0 0 0 * * ?")
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
        //TODO 对于每天其实应该只有一个统计,现在会有多个
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

    public Map<String,Object> getEachLineData(String userId) {
        Map<String,Object> returnMap = new HashMap<>();
        List<String> types = new ArrayList<>();
        types.add("1");
        types.add("2");
        for(String type:types){
        //默认得到最近5天的
        List<Map<String,Integer>>  mapList = new ArrayList<>();
        int k=5;
        List<String> date = new ArrayList<>();
        Calendar ca = Calendar.getInstance();//得到一个Calendar的实例
        ca.setTime(new Date()); //设置时间为当前时间
        ca.add(Calendar.DATE, -k); //天数减1，统计昨天的
            for(int i=4;i>=0;i--){
                ca.add(Calendar.DATE, 1);
                Date lastDay = ca.getTime(); //结果
                String lastBelongDate = CommonUtil.formatDateToBelongDate(lastDay);
                List<AppLogLog> log = appLogLogRepository.findOneByBelongDateAndStatusAndCreatedIdAndTypeOrderByUpdatedDateDesc(lastBelongDate,
                    Constants.LogStatus.STATIC.name(),userId,type);
                //先找静态的，没有就找动态的
                if(log==null||log.size()==0){
                    log = appLogLogRepository.findOneByBelongDateAndStatusAndCreatedIdAndTypeOrderByUpdatedDateDesc(lastBelongDate,
                        Constants.LogStatus.DYNAMIC.name(),userId,type);
                    //需要实时统计--
                    if(log!=null&&log.size()>0){
                        List<AppLogEach> dyEachs = appLogEachRepository.
                            findAllByBelongDateAndCreatedIdAndStatusNotAndTypeOrderByUpdatedDateDesc(lastBelongDate, userId,Constants.APP_LOG_STATUS_DELETE, type);
                        for(AppLogLog each:log){
                            each.setNum(dyEachs.size());
                        }
                        appLogLogRepository.save(log);
                    }
                }
                //如果都没有--那就新创建
                if(log==null||log.size()==0){
                    //写入数据库
                    AppLogLog appLogLog = new AppLogLog();
                    appLogLog.setStatus(Constants.LogStatus.DYNAMIC.name());
                    appLogLog.setProjectType(Constants.ProjectType.GEDIAO.name());
                    appLogLog.setBelongDate(lastBelongDate);
                    appLogLog.setCreatedId(userId);
                    appLogLog.setUpdatedId(userId);
                    appLogLog.setDictType(Constants.DictType.APP_GEDIAO_LOG_MESSAGE_STATUS.name());
                    appLogLog.setType(type);
                    appLogLog.setId(UUIDGenerator.getUUID());
                    appLogLog.setNum(0);
                    //塞入值
                    appLogLogRepository.save(appLogLog);
                    log = new ArrayList<>();
                    log.add(appLogLog);
                }
                //塞入值
                Map map = new HashMap();
                map.put(lastBelongDate,log.get(0).getNum());
                mapList.add(map);
            }
            returnMap.put(type,mapList);
        }

        return returnMap;
    }

    public Map<String, ShowDto> getEachBieData(String createdid) {
        //获取所有type。然后，统计数目（以及type和name）
        Map<String,ShowDto> map = new HashMap<>();
        List<DictService.DictDTO> codeByType = dictService.getCodeByType(Constants.DictType.APP_GEDIAO_LOG_MESSAGE_STATUS.name());
        if(codeByType!=null&&codeByType.size()>0){
            for(DictService.DictDTO dict:codeByType){
                //统计不同type的数目
                int num = 0;
                ShowDto dto = new ShowDto();
                dto.setId(dict.getValue());
                dto.setLabel(dict.getLabel());
                dto.setNum(num);
                List<AppLogEach> eaches = appLogEachRepository.findAllByCreatedIdAndStatusNotAndType(createdid, Constants.APP_LOG_STATUS_DELETE, dict.getValue());
                if(eaches!=null){
                    num = eaches.size();
                }
                dto.setNum(num);
                map.put(dict.getValue(),dto);
            }
        }
        return map;
    }


    public class  ShowDto{
        private String label;
        private Integer num;
//        private String type;
        private String id;




        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public Integer getNum() {
            return num;
        }

        public void setNum(Integer num) {
            this.num = num;
        }
    }
}

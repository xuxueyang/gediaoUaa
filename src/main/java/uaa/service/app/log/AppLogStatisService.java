package uaa.service.app.log;


import com.alibaba.fastjson.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.domain.UaaError;
import uaa.domain.app.log.AppLogDetail;
import uaa.domain.app.log.AppLogEach;
import uaa.domain.app.log.AppLogEachTag;
import uaa.domain.app.log.AppLogTag;
import uaa.domain.uaa.UaaUser;
import uaa.service.resource.DictService;
import uaa.web.rest.util.CommonUtil;
import uaa.web.rest.util.excel.ExcelUtil;
import uaa.web.rest.util.excel.vo.app.log.LogEachOutVO;

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class AppLogStatisService {
    @Autowired
    private AppLogEachService appLogEachService;

    @Autowired
    private AppLogSingleService appLogSingleService;

    @Autowired
    private DictService dictService;
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
}

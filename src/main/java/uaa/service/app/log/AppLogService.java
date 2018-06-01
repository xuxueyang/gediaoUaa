package uaa.service.app.log;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.domain.app.log.AppLogDetail;
import uaa.domain.app.log.AppLogEach;
import uaa.repository.app.log.AppLogEachRepository;
import uaa.service.dto.app.log.AppLogDetailDTO;
import uaa.service.dto.app.log.AppLogEachDTO;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class AppLogService {

    @Autowired
    private AppLogEachRepository appLogEachRepository;

    public AppLogEachDTO getEachInfoById(String id){
        if(id==null||"".equals(id)){
            return null;
        }
        AppLogEach appLogEach = appLogEachRepository.findOne(id);
        if(appLogEach==null){
            return null;
        }
        AppLogEachDTO appLogEachDTO = new AppLogEachDTO();
        List<AppLogDetailDTO> detailDTOList = new ArrayList<>();
        //从关联表里找出数据，插入
        for(AppLogDetail appLogDetail:appLogEach.getDetails()){
            //遍历塞入数据
            AppLogDetailDTO appLogDetailDTO = new AppLogDetailDTO();
            appLogDetailDTO.setRemarks(appLogDetail.getRemarks());
            detailDTOList.add(appLogDetailDTO);
        }
        appLogEachDTO.setAppLogDetailDTOList(detailDTOList);
        //塞入自己的数据
//        appLogEachDTO.set
//        appLogEachDTO.setAppLogDetailDTOList();
        return appLogEachDTO;
    }
}

package uaa.service.app.log;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.domain.app.log.AppLogDetail;
import uaa.repository.app.log.AppLogDetailRepository;
import uaa.service.dto.app.log.AppLogDetailDTO;
import util.UUIDGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UKi_Hi on 2018/6/4.
 */

@Transactional
@Service
public class AppLogDetailService {
    @Autowired
    private AppLogDetailRepository appLogDetailRepository;

    public AppLogDetailDTO getDetailInfoById(String id) {
        AppLogDetail one = appLogDetailRepository.findOneByIdAndStatusNot(id,Constants.DELETE);
        if(one==null){
            return null;
        }
        return prepareDetailEntityToDTO(one,true);
    }
    public List<AppLogDetailDTO> getAllDetailInfoByEachId(String eachId){
        List<AppLogDetail> oneByIdAndStatusNot = appLogDetailRepository.findAllByLogEachIdAndStatusNot(eachId, Constants.DELETE);
        List<AppLogDetailDTO> one = new ArrayList<>();
        if(oneByIdAndStatusNot!=null){
            for(AppLogDetail detail:oneByIdAndStatusNot){
                one.add(prepareDetailEntityToDTO(detail,false));
            }
        }
        return one;
    }
    public AppLogDetailDTO prepareDetailEntityToDTO(AppLogDetail appLogDetail,boolean needInsertRemarks){
        AppLogDetailDTO appLogDetailDTO = new AppLogDetailDTO();
        appLogDetailDTO.setCreatedDate(appLogDetail.getCreatedDate());
        appLogDetailDTO.setUpdatedDate(appLogDetail.getUpdatedDate());
        if (needInsertRemarks) {
            appLogDetailDTO.setRemarks(appLogDetail.getRemarks());
        }
        appLogDetailDTO.setId(appLogDetail.getId());
        return appLogDetailDTO;
    }
    public void saveDetail(AppLogDetail appLogDetail){
        appLogDetailRepository.save(appLogDetail);
    }

    public AppLogDetail findDetailBy(String id) {
        return appLogDetailRepository.findOne(id);
    }

    public void deleteDetail(AppLogDetail appLogDetail) {
        appLogDetail.setStatus(Constants.APP_LOG_STATUS_DELETE);
        appLogDetailRepository.save(appLogDetail);
    }

    public AppLogDetailDTO createDetail(String logEachId, String remarks) {
        AppLogDetail appLogDetail = new AppLogDetail();
        appLogDetail.setId(UUIDGenerator.getUUID());
        appLogDetail.setStatus(Constants.APP_LOG_STATUS_SAVE);
        appLogDetail.setRemarks(remarks);
        appLogDetail.setLogEachId(logEachId);
        appLogDetailRepository.save(appLogDetail);
        return prepareDetailEntityToDTO(appLogDetail,true);
    }

    public void updateDetail(AppLogDetail appLogDetail, String remarks,String updateId) {
        appLogDetail.setRemarks(remarks);
        appLogDetailRepository.save(appLogDetail);
    }
}

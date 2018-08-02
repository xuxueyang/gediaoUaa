package uaa.service.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.domain.uaa.UaaDict;
import uaa.repository.uaa.UaaDictRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by UKi_Hi on 2018/7/13.
 */
@Transactional
@Service
public class DictService {

//    APP_GEDIAO_LOG_MESSAGE_STATUS ： 获取格调的消息状态
    private static String APP_GEDIAO_LOG_MESSAGE_STATUS = "APP_GEDIAO_LOG_MESSAGE_STATUS";
    @Autowired
    private UaaDictRepository uaaDictRepository;

    public String getNameByTypeId(String id){
        UaaDict one = uaaDictRepository.findOne(id);
        if(one==null)
            return "";
        else
            return one.getName();
    }
    public List<DictDTO> getCodeByType(String type) {
        List<DictDTO> dtos = new ArrayList<>();
        List<UaaDict> allByType = uaaDictRepository.findAllByType(type);
        if(allByType!=null&&allByType.size()>0){
            for(UaaDict dict:allByType){
                dtos.add(new DictDTO(dict.getType(),dict.getName(),dict.getCode()));
            }
        }
        return dtos;
    }
    public class DictDTO{
        private String type;
        private String label;
        private String value;


        public DictDTO(String type,String label,String value){
            this.type=type;
            this.label=label;
            this.value=value;
        }


        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getLabel() {
            return label;
        }

        public void setLabel(String label) {
            this.label = label;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }
}

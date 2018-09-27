package uaa.service.resource;

import org.neo4j.cypher.internal.frontend.v2_3.ast.functions.Str;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.domain.uaa.UaaDict;
import uaa.domain.uaa.UaaDictValue;
import uaa.repository.uaa.UaaDictRepository;
import uaa.repository.uaa.UaaDictValueRepository;
import util.UUIDGenerator;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    @Autowired
    private UaaDictValueRepository uaaDictValueRepository;

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
    public List<Map> getValueByCode(String code, String content){
        //从uaa_dict_value表中获取值
        if(content==null)
            content="";
        List<Map> list = new ArrayList<>();
        UaaDict oneByCode = uaaDictRepository.findOneByCode(code);
        if(oneByCode!=null){
            List<UaaDictValue> allByDictId = uaaDictValueRepository.findAllByDictId(oneByCode.getId());
            if(allByDictId!=null){
                for(UaaDictValue value:allByDictId){
                    if(value.getValue()!=null&&value.getValue().contains(content)){
                        Map<String,String> map = new HashMap<>();
                        map.put("value",value.getValue());
                        map.put("id",value.getId());
                        list.add(map);
                    }
                }
            }
        }
        return list;
    }
    public void updateValueByCode(String code, String value, String valueId,String userId){
        //从uaa_dict_value表中获取值
//        UaaDict oneByCode = uaaDictRepository.findOneByCode(code);
//        if(oneByCode.getCreatedId()!=null){
//            if(!oneByCode.getCreatedId().equals(userId)){
//                return;
//            }
//        }
//        if(oneByCode!=null){
//            List<UaaDictValue> allByDictId = uaaDictValueRepository.findAllByDictId(oneByCode.getId());
//            if(allByDictId!=null){
//                for(UaaDictValue valueDomain:allByDictId){
//                    valueDomain.setValue(value);
//                    valueDomain.setUpdatedDate(ZonedDateTime.now());
//                    uaaDictValueRepository.save(valueDomain);
//
//                }
//            }
//        }

        UaaDict oneByCode = uaaDictRepository.findOneByCode(code);
        if(oneByCode.getCreatedId()!=null){
            if(!oneByCode.getCreatedId().equals(userId)){
                return;
            }
        }
        UaaDictValue one = uaaDictValueRepository.findOne(valueId);
        if(one!=null){
            one.setValue(value);
            one.setUpdatedDate(ZonedDateTime.now());
            uaaDictValueRepository.save(one);
        }
    }
    public void addValueByCode(String code,String value,String userId){
        //从uaa_dict_value表中获取值
        UaaDict oneByCode = uaaDictRepository.findOneByCode(code);
        if(oneByCode.getCreatedId()!=null){
            if(!oneByCode.getCreatedId().equals(userId)){
                return;
            }
        }
        if(oneByCode!=null){
            UaaDictValue valueDomain = new UaaDictValue();
            valueDomain.setValue(value);
            valueDomain.setDictId(oneByCode.getId());
            valueDomain.setId(UUIDGenerator.getUUID());
            valueDomain.setCreatedDate(ZonedDateTime.now());
            valueDomain.setUpdatedDate(valueDomain.getCreatedDate());
            uaaDictValueRepository.save(valueDomain);
        }
    }

    public void deleteValeById(String id) {
        UaaDictValue one = uaaDictValueRepository.findOne(id);
        if(one!=null){
            uaaDictValueRepository.delete(one);
        }
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

package uaa.service;


import core.ReturnCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.domain.UaaError;
import uaa.domain.uaa.UaaLogMessage;
import uaa.domain.uaa.UaaUser;
import uaa.repository.uaa.UaaLogMessageRepository;
import uaa.repository.uaa.UaaUserRepository;
import uaa.service.dto.message.MessageDTO;
import util.UUIDGenerator;
import util.Validators;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UaaMessageService {
    @Autowired
    private UaaLogMessageRepository messageRepository;
    @Autowired
    private UaaUserRepository uaaUserRepository;
    public List<MessageDTO> getMessagesByType(String projectType){
        List<MessageDTO> messageDTOList = new ArrayList<>();
        //获取到全部的
        List<UaaLogMessage> allByType = messageRepository.findAllByProjectType(projectType);
        //转DTO返回，前端根据BUG、T ODO、DONE，显示红色，黄色，蓝色，做区分
        if(allByType!=null&&allByType.size()>0){
            for(UaaLogMessage logMessage:allByType){
                if(Constants.MESSAGE_STATUS_DELETE.equals(logMessage.getStatus()))
                    continue;
                MessageDTO messageDTO = prepareMessageEntityToDTO(logMessage);
                if(messageDTO!=null)
                    messageDTOList.add(messageDTO);
            }
        }
        return messageDTOList;
    }
    private MessageDTO prepareMessageEntityToDTO(UaaLogMessage message){
        //目前只返回三种状态
//        if(!Validators.fieldRangeValue(message.getType(),
//            Constants.MESSAGE_TYPE.values())){
//            return null;
//        }
        Constants.MESSAGE_TYPE[] values = Constants.MESSAGE_TYPE.values();
        boolean has = false;
        for(Constants.MESSAGE_TYPE value:values){
            if(value.name().equals(message.getType()))
            {
                has = true;
                break;
            }
        }
        if(!has)
            return null;
        MessageDTO dto = new MessageDTO();
        dto.setCreatedDate(message.getCreatedDate());
        dto.setUpdatedDate(message.getUpdatedDate());
        dto.setType(message.getType());
        dto.setValue(message.getValue());
        dto.setProjectType(message.getProjectType());
        UaaUser one = uaaUserRepository.findOne(message.getCreatedID());
        if(one==null||"".equals(one.getName()))
            return null;
        dto.setLoginName(one.getName());
        dto.setTitle(message.getTitle());
        dto.setId(message.getId());
        return dto;
    }
    public void  createMessage(String createdId,String title,String projectType,String type,String value){
        UaaLogMessage logMessage = new UaaLogMessage();
        logMessage.setStatus(Constants.MESSAGE_STATUS_SAVE);
        logMessage.setProjectType(projectType);
        logMessage.setType(type);
        logMessage.setCreatedID(createdId);
        logMessage.setUpdatedID(createdId);
        logMessage.setCreatedDate(Instant.now());
        logMessage.setUpdatedDate(Instant.now());
        logMessage.setValue(value);
        logMessage.setTitle(title);
        logMessage.setId(UUIDGenerator.getUUID());
        messageRepository.save(logMessage);
    }

    public UaaError deleteMessage(String id) {
        UaaError uaaError = new UaaError();
        UaaLogMessage one = messageRepository.findOne(id);
        if(one==null){
            uaaError.addError(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE);
            return uaaError;
        }
        one.setStatus(Constants.MESSAGE_STATUS_DELETE);
        messageRepository.save(one);
        return uaaError;
    }
}

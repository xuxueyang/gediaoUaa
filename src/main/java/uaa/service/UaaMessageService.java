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
    public List<MessageDTO> getMessagesByProjectType(String projectType){
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
//        //验证消息范围
//        if(!Validators.fieldRangeValue(message.getType(),
//            Constants.MESSAGE_TYPE_TODO,
//            Constants.MESSAGE_TYPE_DONE,
//            Constants.MESSAGE_TYPE_BUG,
//            Constants.MESSAGE_TYPE_PAD,
//            Constants.MESSAGE_TYPE_QLH_MEMBER_SAY
//        )){
//            return null;
//        }
        MessageDTO dto = new MessageDTO();
        dto.setCreatedDate(message.getCreatedDate());
        dto.setUpdatedDate(message.getUpdatedDate());
        dto.setType(message.getType());
        dto.setValue(message.getValue());
        dto.setProjectType(message.getProjectType());
        dto.setPs(message.getPs());
        UaaUser one = uaaUserRepository.findOne(message.getCreatedId());
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
        logMessage.setCreatedId(createdId);
        logMessage.setUpdatedId(createdId);
        logMessage.setCreatedDate(Instant.now());
        logMessage.setUpdatedDate(Instant.now());
        logMessage.setValue(value);
        logMessage.setTitle(title);
//        logMessage.setPs(ps);
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

    /**
     * 查找工程下可以被更新的（只更新消息的值和备注）
     * @param id
     * @return
     */
    public UaaLogMessage findProjectCanUpdateMessageById(String projectType,String id) {
        UaaLogMessage logMessage = messageRepository.findOneByProjectTypeAndId(projectType, id);
        if(Constants.MESSAGE_STATUS_DELETE.equals(logMessage.getStatus()))
            return null;
        return logMessage;
    }

    public void updateMessageValueAndPs(UaaLogMessage logMessage,String value, String ps,String updatedId) {
        logMessage.setPs(ps);
        logMessage.setValue(value);
        logMessage.setUpdatedId(updatedId);
        logMessage.setUpdatedDate(Instant.now());
        messageRepository.save(logMessage);
    }

    public void updateMessageTypeAndPs(UaaLogMessage logMessage, String type, String ps, String updatedId) {
        logMessage.setPs(ps);
        logMessage.setType(type);
        logMessage.setUpdatedDate(Instant.now());
        logMessage.setUpdatedId(updatedId);
        messageRepository.save(logMessage);
    }

    public void transferMessageTypeAndPs(UaaLogMessage logMessage, String type, String ps, String updatedId) {
        UaaLogMessage newMessage = new UaaLogMessage();
        newMessage.setPs(ps);
        newMessage.setType(type);
        newMessage.setUpdatedDate(Instant.now());
        newMessage.setUpdatedId(updatedId);
        newMessage.setCreatedDate(Instant.now());
        newMessage.setCreatedId(updatedId);
        newMessage.setValue(logMessage.getValue());
        newMessage.setTitle(logMessage.getTitle());
        newMessage.setId(UUIDGenerator.getUUID());
        newMessage.setLastMessageId(logMessage.getId());
        newMessage.setProjectType(logMessage.getProjectType());
        newMessage.setVersion(logMessage.getVersion());
        newMessage.setStatus(Constants.MESSAGE_STATUS_SAVE);
        logMessage.setStatus(Constants.MESSAGE_STATUS_DELETE);
        messageRepository.save(logMessage);
        messageRepository.save(newMessage);
    }

    public Object getMessagesByProjectTypeAndCreatedId(String projectType, String createdid) {
        List<UaaLogMessage> messageList = messageRepository.findAllByProjectTypeAndCreatedId(projectType, createdid);
        List<MessageDTO> messageDTOList = new ArrayList<>();
        if(messageList!=null&&messageList.size()>0){
            for(UaaLogMessage logMessage:messageList){
                if(Constants.MESSAGE_STATUS_DELETE.equals(logMessage.getStatus()))
                    continue;
                MessageDTO messageDTO = prepareMessageEntityToDTO(logMessage);
                if(messageDTO!=null)
                    messageDTOList.add(messageDTO);
            }
        }
        return messageDTOList;
    }
}

package uaa.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.domain.uaa.UaaLogMessage;
import uaa.repository.uaa.UaaLogMessageRepository;
import uaa.service.dto.message.MessageDTO;
import util.Validators;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class UaaMessageService {
    @Autowired
    private UaaLogMessageRepository messageRepository;

    public List<MessageDTO> getMessagesByType(String projectType){
        List<MessageDTO> messageDTOList = new ArrayList<>();
        //获取到全部的
        List<UaaLogMessage> allByType = messageRepository.findAllByProjectType(projectType);
        //转DTO返回，前端根据BUG、T ODO、DONE，显示红色，黄色，蓝色，做区分
        if(allByType!=null&&allByType.size()>0){
            for(UaaLogMessage logMessage:allByType){
                MessageDTO messageDTO = prepareMessageEntityToDTO(logMessage);
                if(messageDTO!=null)
                    messageDTOList.add(messageDTO);
            }
        }
        return messageDTOList;
    }
    private MessageDTO prepareMessageEntityToDTO(UaaLogMessage message){
        //目前只返回三种状态
        if(!Validators.fieldRangeValue(message.getType(),
            Constants.MESSAGE_TYPE_BUG,
            Constants.MESSAGE_TYPE_DONE,
            Constants.MESSAGE_TYPE_TODO)){
            return null;
        }
        MessageDTO dto = new MessageDTO();
        dto.setCreatedDate(message.getCreatedDate());
        dto.setUpdatedDate(message.getUpdatedDate());
        dto.setType(message.getType());
        dto.setValue(message.getValue());
        dto.setProjectType(message.getProjectType());
        return dto;
    }
}

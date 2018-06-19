package uaa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.domain.uaa.UaaFormData;
import uaa.repository.uaa.UaaFormDataRepository;
import uaa.service.dto.form.CreateDataDTO;
import util.UUIDGenerator;

@Service
@Transactional
public class UaaFormDataService {
    @Autowired
    private UaaFormDataRepository uaaFormDataRepository;


    public void saveFormData(CreateDataDTO createDataDTO) {
        UaaFormData uaaFormData = new UaaFormData();
        uaaFormData.setId(UUIDGenerator.getUUID());
        uaaFormData.setData(createDataDTO.getData());
        uaaFormData.setFormType(createDataDTO.getFormType());
        uaaFormData.setProjectType(createDataDTO.getProjectType());
        uaaFormData.setStatus(Constants.TASK_FORM_DATA_WAIT);
        uaaFormDataRepository.save(uaaFormData);
    }
}

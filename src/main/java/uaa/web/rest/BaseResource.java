package uaa.web.rest;

import core.ReturnResultDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uaa.service.permission.UaaPermissionService;


import java.io.Serializable;

public class BaseResource<T> implements Serializable {
    @Autowired
    protected UaaPermissionService uaaPermissionService;
    protected ResponseEntity<ReturnResultDTO<?>> prepareReturnResult(String returnCode, Object data) {
        return new ResponseEntity<ReturnResultDTO<?>>(new ReturnResultDTO(returnCode, data), HttpStatus.OK);
    }

}
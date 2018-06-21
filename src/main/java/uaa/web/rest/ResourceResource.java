package uaa.web.rest;

import core.BaseResource;
import fastdfs.DTO.UploadFileResultDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
@Api(value = "上传资源",description = "上传资源")
public class ResourceResource  extends BaseResource {
//    //资源名ID，根据表里的类型，返回资源
//    @PostMapping("/resource/{name}")
//    @ResponseBody
//    @ApiOperation(value = "上传文件", httpMethod = "POST", response = UploadFileResultDTO.class, notes = "上传文件")
//    public ResponseEntity<?> uploadFile(final @PathVariable("name") String name, HttpServletRequest request, HttpServletResponse response) {
//        try {
//
//        } catch (Exception e) {
//
//        }
//    }
}

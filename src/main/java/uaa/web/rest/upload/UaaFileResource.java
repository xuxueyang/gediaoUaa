package uaa.web.rest.upload;

import core.BaseResource;
import core.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uaa.domain.uaa.UaaFile;
import uaa.service.dto.upload.UploadResultDTO;
import uaa.service.upload.UaaFileService;
import util.UUIDGenerator;
import util.Validators;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

//import core.BaseResource;
//import core.ReturnCode;
//import fastdfs.ClientGlobal;
//import fastdfs.client.UploadFile;
//import fastdfs.client.UploadManager;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//import org.springframework.web.multipart.MultipartHttpServletRequest;
//import uaa.security.SecurityUtils;
//import uaa.service.dto.upload.UploadResultDTO;
//import util.Validators;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.ArrayList;
//import java.util.Iterator;
//import java.util.List;
//
///**
// * @author yuren
// */
@RestController
@RequestMapping("/api/uaafile")
@Api(value = "文件上传和下载", description = "文件上传和下载")
public class UaaFileResource extends BaseResource {
    @Autowired
    private UaaFileService uaaFileService;

    //文件能穿过来即可，前期不必用fastddfs
    @PostMapping("/upload")
    @ResponseBody
    @ApiOperation(value = "上传文件", httpMethod = "POST", response = UploadResultDTO.class, notes = "上传文件")
    public ResponseEntity<?> uploadFileUaa(HttpServletRequest request, HttpServletResponse response) {
        try {
//            List<String> result = new ArrayList<String>();
            List<UploadResultDTO> result = new ArrayList<UploadResultDTO>();;
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = mRequest.getFileNames();
            while (iter.hasNext()){
                MultipartFile file = mRequest.getFile(iter.next());
                String fileName = null;
                 fileName = file.getOriginalFilename();
                //验证文件名不能含有非法字符
                if(Validators.verifyFileName(fileName))
                    return prepareReturnResult(ReturnCode.ERROR_FIELD_FORMAT,null);
                //获取文件类型
                String extName = null;
                String oldFileName = fileName;
                try {
                    extName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                    if(extName.length()!=fileName.length())
                        extName = extName.replace(".","");
                }catch (Exception e){
                    extName = null;
                }
                fileName = fileName.replace(".","-")+"-"+extName+ UUIDGenerator.getUUID();
                //上传文件——不适用fdfs
                //存ID和路径
                try {
                    String id = uaaFileService.uploadFile(file,fileName);
                    UploadResultDTO uploadResult = new UploadResultDTO();
                    uploadResult.setId(id);
                    uploadResult.setUploadFileName(oldFileName);
                    result.add(uploadResult);
                }catch (Exception e){
                    UploadResultDTO uploadResult = new UploadResultDTO();
                    uploadResult.setId("");
                    uploadResult.setUploadFileName(oldFileName);
                    result.add(uploadResult);
                }
            }
            return prepareReturnResult(ReturnCode.DEFAULT_SUCCESS, result);
        }catch (Exception e){
            return prepareReturnResult(ReturnCode.ERROR_CREATE, null);
        }
    }



    @GetMapping("/down/{id}")
    @ResponseBody
    @ApiOperation(value = "文件下载", httpMethod = "GET", response = UploadResultDTO.class, notes = "文件下载")
    public ResponseEntity<?> downloadFileUaa(@PathVariable("id") String id,HttpServletResponse response) {
        try {
            //判断文件ID
            UaaFile uaaFile = uaaFileService.findUaaFileById(id);
            if(uaaFile==null)
            {
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE,null);
            }
            //获取到文件的输出流
            uaaFileService.downFile(uaaFile.getRootFilePath(),uaaFile.getRelFilePath(),response.getOutputStream());
            // 清空response
            response.reset();
            // 设置response的Header
            response.addHeader("Content-Disposition", "attachment;filename=" + uaaFile.getName());
            response.addHeader("Content-Length", uaaFile.getSize());
            response.setContentType("application/octet-stream");
            return prepareReturnResult(ReturnCode.DEFAULT_SUCCESS, response);
        } catch (Exception e) {
            return prepareReturnResult(ReturnCode.ERROR_CREATE, null);
        }

    }
}
//                //获取文件类型
//                String extName = null;
//                try {
//                    extName = fileName.substring(fileName.lastIndexOf("."))
//                        .toLowerCase();
//                    extName = extName.replace(".", "");
//                } catch (Exception e) {
//                    extName = null;
//                }
//                //上传文件
//                //TODO 暂使用默认的group (group1)
//                UploadFile uploadfile = new UploadFile(fileName, file.getBytes(), extName);
//                String url = UploadManager.upload(uploadfile, "xuxy", ClientGlobal.default_group);
//                //返回绝对路径
//                //TODO 如有需要返回宽和高
//                uploadResult = new UploadResultDTO();
//                uploadResult.setUrl(ClientGlobal.http_url+ ClientGlobal.default_group + "/" + url);
//                uploadResult.setType(extName);
//                uploadResult.setSize(file.getSize());
//                result.add(uploadResult);
//
//            }
//            if (result.isEmpty()) {
//                //文件为空
//                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY, null);
//            }
//            return prepareReturnResult(ReturnCode.DEFAULT_SUCCESS, result);
//        } catch (Exception e) {
////            LogUtil.error(SecurityUtils.getCurrentUserLogin(), "", "", Constants.MODULE_NAME, "uploadFile exception", e);
//            return prepareReturnResult(ReturnCode.ERROR_CREATE, null);
//        }
//    }
//}

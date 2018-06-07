package uaa.web.rest.upload;

import core.BaseResource;
import core.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import uaa.service.upload.UploadResultDTO;
import util.Validators;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
//import uaa.service.upload.UploadResultDTO;
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
@RequestMapping("/api")
@Api(value = "文件上传", description = "文件上传")
public class UaaFileUploadResource extends BaseResource {
    //文件能穿过来即可，前期不必用fastddfs
    @PostMapping("/uaafile/upload")
    @ResponseBody
    @ApiOperation(value = "上传文件", httpMethod = "POST", response = UploadResultDTO.class, notes = "上传文件")
    public ResponseEntity<?> uploadFileUaa(final String _fileName, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<String> result = new ArrayList<String>();
//            List<UploadResultDTO> result = new ArrayList<UploadResultDTO>();
            UploadResultDTO uploadResult = null;
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = mRequest.getFileNames();
            while (iter.hasNext()){
                MultipartFile file = mRequest.getFile(iter.next());
                String fileName = null;
                if(Validators.fieldBlank(_fileName))
                    fileName = _fileName;
                else
                    fileName = file.getOriginalFilename();
                //获取文件类型
                String extName = null;
                try {
                    extName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
                    if(extName.length()!=fileName.length())
                        extName = extName.replace(".","");
                }catch (Exception e){
                    extName = null;
                }
                //上传文件——不适用fdfs
                //存ID和路径

            }
            return prepareReturnResult(ReturnCode.DEFAULT_SUCCESS, result);
        }catch (Exception e){
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

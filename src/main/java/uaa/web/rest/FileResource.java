package uaa.web.rest;


import core.BaseResource;
import core.ReturnCode;
import fastdfs.ClientGlobal;
import fastdfs.DTO.UploadFileResultDTO;
import fastdfs.client.UploadFile;
import fastdfs.client.UploadManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import util.Validators;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Api(value = "上传下载文件",description = "上传下载文件")
@RestController
@RequestMapping("/api")
public class FileResource extends BaseResource {

    @PostMapping("/uploadfile")
    @ResponseBody
    @ApiOperation(value = "上传文件", httpMethod = "POST", response = UploadFileResultDTO.class, notes = "上传文件")
    public ResponseEntity<?> uploadFile(final String _fileName, HttpServletRequest request, HttpServletResponse response) {
        try {
            List<UploadFileResultDTO> result = new ArrayList<UploadFileResultDTO>();
            UploadFileResultDTO uploadResult = null;
            MultipartHttpServletRequest mRequest = (MultipartHttpServletRequest) request;
            Iterator<String> iter = mRequest.getFileNames();
            while (iter.hasNext()) {
                MultipartFile file = mRequest.getFile(iter.next());
                String fileName = null;
                if (Validators.fieldNotBlank(_fileName)) {
                    fileName = _fileName;
                } else {
                    fileName = file.getOriginalFilename();
                }
                //获取文件类型
                String extName = null;
                try {
                    extName = fileName.substring(fileName.lastIndexOf("."))
                        .toLowerCase();
                    extName = extName.replace(".", "");
                } catch (Exception e) {
                    extName = null;
                }
                //上传文件
                //TODO 暂使用默认的group (group1)
                UploadFile uploadfile = new UploadFile(fileName, file.getBytes(), extName);
                String url = UploadManager.upload(uploadfile, "上传测试者", ClientGlobal.default_group);
                //返回绝对路径
                //TODO 如有需要返回宽和高
                uploadResult = new UploadFileResultDTO();
                uploadResult.setUrl(ClientGlobal.http_url+ ClientGlobal.default_group + "/" + url);
                uploadResult.setType(extName);
                uploadResult.setSize(file.getSize());
                result.add(uploadResult);

            }
            if (result.isEmpty()) {
                //文件为空
                return prepareReturnResult(ReturnCode.ERROR_FIELD_EMPTY, null);
            }
            return prepareReturnResult(ReturnCode.DEFAULT_SUCCESS, result);
        } catch (Exception e) {
//            LogUtil.error(SecurityUtils.getCurrentUserLogin(), "", "", Constants.MODULE_NAME, "uploadFile exception", e);
            return prepareReturnResult(ReturnCode.ERROR_CREATE, null);
        }
    }
}

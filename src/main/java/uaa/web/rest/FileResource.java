package uaa.web.rest;


import core.BaseResource;
import core.ReturnCode;
import fastdfs.ClientGlobal;
import fastdfs.DTO.UploadFileResultDTO;
import fastdfs.client.UploadFile;
import fastdfs.client.UploadManager;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import util.Validators;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Api(value = "上传下载文件",description = "上传下载文件")
@RestController
@RequestMapping("/api")
public class FileResource extends BaseResource {

    @PostMapping("/file/upload")
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

    @GetMapping("/file/down")
    @ResponseBody
    @ApiOperation(value = "文件下载", httpMethod = "GET", response = UploadFileResultDTO.class, notes = "文件下载")
    public ResponseEntity<?> downloadFile(final String fileName, final String filePath, HttpServletResponse response) {
        try {
            URL url = new URL(filePath);
            URLConnection uc = url.openConnection();
            String contentType = uc.getContentType();
            int contentLength = uc.getContentLength();
            if (contentType.startsWith("text/") || contentLength == -1) {
                return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE, null);
            }
            try (InputStream raw = uc.getInputStream()) {
                InputStream in = new BufferedInputStream(raw);
                byte[] data = new byte[contentLength];
                int offset = 0;
                while (offset < contentLength) {
                    int bytesRead = in.read(data, offset, data.length - offset);
                    if (bytesRead == -1) {
                        break;
                    }
                    offset += bytesRead;
                }
                if (offset != contentLength) {
                    return prepareReturnResult(ReturnCode.ERROR_RESOURCE_NOT_EXIST_CODE, null);
                }
                // 清空response
                response.reset();
                // 设置response的Header
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);
                response.addHeader("Content-Length", "" + contentLength);
                response.setContentType("application/octet-stream");
                try (OutputStream out = new BufferedOutputStream(response.getOutputStream())) {
                    out.write(data);
                    out.flush();
                }
            }
            return prepareReturnResult(ReturnCode.DEFAULT_SUCCESS, response);
        } catch (Exception e) {
//            LogUtil.error(SecurityUtils.getCurrentUserLogin(), "", "", Constants.MODULE_NAME, "uploadFile exception", e);
            return prepareReturnResult(ReturnCode.ERROR_CREATE, null);
        }

    }

//    @Autowired
//    private UeditorService ueditoreService;
//
//    @RequestMapping("/ueditor/execute")
//    @ResponseBody
//    public ResponseEntity<?> uploadFile(HttpServletRequest request, HttpServletResponse response) {
//        String rootPath = "/opt/config/";
//        String resultMsg = new UeditorActionEnter(request, rootPath, this.ueditoreService).exec();
//        return ResponseEntity.ok().body(resultMsg);
//    }
}

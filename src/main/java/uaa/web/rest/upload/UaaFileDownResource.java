package uaa.web.rest.upload;


import core.BaseResource;
import core.ReturnCode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import uaa.service.upload.UploadResultDTO;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

@RestController
@RequestMapping("/api")
@Api(value = "文件下载", description = "文件下载")
public class UaaFileDownResource extends BaseResource {

    @GetMapping("/uaafile/down/{id}")
    @ResponseBody
    @ApiOperation(value = "文件下载", httpMethod = "GET", response = UploadResultDTO.class, notes = "文件下载")
    public ResponseEntity<?> downloadFileUaa(final String fileName, final String filePath, HttpServletResponse response) {
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
}

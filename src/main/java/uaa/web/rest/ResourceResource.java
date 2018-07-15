package uaa.web.rest;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;

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

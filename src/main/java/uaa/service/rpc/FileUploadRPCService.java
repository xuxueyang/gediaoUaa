package uaa.service.rpc;

import fastdfs.client.UploadFile;
import fastdfs.client.UploadManager;
import org.springframework.stereotype.Service;

//@Service
//@JsonRpcService("/fileuploadrpcservice")
public class FileUploadRPCService {

//    public String upload(String fileName, byte[] fileByteContent) throws Exception{
//        String extName = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
//        extName = extName.replace(".", "");
//        //上传文件
//        //TODO 暂使用默认的group (group1)
//        UploadFile uploadfile = new UploadFile(fileName, fileByteContent, extName);
//        String url = UploadManager.upload(uploadfile, SecurityUtils.getCurrentUserLogin(), ClientGlobal.default_group);
//        //返回绝对路径
//        return url;
//    }

}

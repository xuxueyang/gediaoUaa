package uaa.service.upload;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sun.net.ftp.FtpClient;
import sun.net.ftp.FtpProtocolException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Created by UKi_Hi on 2018/6/10.
 */
@Service
@Transactional
public class UploadFTPFileService {
    //https://blog.csdn.net/lemon_tree12138/article/details/49777467
    private  String ipAddr ;
    private  String username ;
    private  String password ;
    private  String uploadServerFileRootPath;
    private  int port = 22;

    public FtpClient connectFTP(String url, int port, String username, String password) {
        //创建ftp
        FtpClient ftp = null;
        try {
            //创建地址
            SocketAddress addr = new InetSocketAddress(url, port);
            //连接
            ftp = FtpClient.create();
            ftp.connect(addr);
            //登陆
            ftp.login(username, password.toCharArray());
            ftp.setBinaryType();
        } catch (FtpProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ftp;
    }
    public FtpClient connectFTP(){
        return this.connectFTP(ipAddr,port,username,password);
    }

    public static void upload(String localFile, String ftpFile, FtpClient ftp) {
        OutputStream os = null;
        FileInputStream fis = null;
        try {
            // 将ftp文件加入输出流中。输出到ftp上
            os = ftp.putFileStream(ftpFile);
            File file = new File(localFile);

            // 创建一个缓冲区
            fis = new FileInputStream(file);
            byte[] bytes = new byte[1024];
            int c;
            while((c = fis.read(bytes)) != -1){
                os.write(bytes, 0, c);
            }
            System.out.println("upload success!!");
        } catch (FtpProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(os!=null) {
                    os.close();
                }
                if(fis!=null) {
                    fis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

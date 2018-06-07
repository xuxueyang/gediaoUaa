package uaa.service.upload;


import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uaa.config.ApplicationProperties;
import uaa.domain.uaa.UaaFile;
import uaa.repository.uaa.UaaFileRepository;
import util.UUIDGenerator;

import java.io.*;

import java.time.Instant;
import java.util.Properties;
import java.util.Vector;

@Service
@Transactional
public class UaaFileService {
    @Autowired
    private UaaFileRepository fileRepository;

    @Autowired
    private ApplicationProperties applicationProperties;
    //采用默认的参数目前
    private  String ipAddr ;
    private  String username ;
    private  String password ;
    private  String uploadServerFileRootPath;

    private ChannelSftp channelSftp;
    public UaaFileService(){
        this.ipAddr = applicationProperties.getConfig().getFileService().getIpAddr();
        this.username = applicationProperties.getConfig().getFileService().getUsername();
        this.password = applicationProperties.getConfig().getFileService().getPassword();
        this.uploadServerFileRootPath = applicationProperties.getConfig().getFileService().getUploadServerFileRootPath();
        this.channelSftp  =  connect(ipAddr,22,username,password);

    }
    private ChannelSftp connect(String host, int port, String username,
                                String password) {
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, host, port);
            Session sshSession = jsch.getSession(username, host, port);
            System.out.println("Session created.");
            sshSession.setPassword(password);
            Properties sshConfig = new Properties();
            sshConfig.put("StrictHostKeyChecking", "no");
            sshSession.setConfig(sshConfig);
            sshSession.connect();
            System.out.println("Session connected.");
            System.out.println("Opening Channel.");
            Channel channel = sshSession.openChannel("sftp");
            channel.connect();
            sftp = (ChannelSftp) channel;
            System.out.println("Connected to " + host + ".");
        } catch (Exception e) {

        }
        return sftp;
    }
//    /**
//     * 连接sftp服务器
//     * @param host 主机
//     * @param port 端口
//     * @param username 用户名
//     * @param password 密码
//     * @return
//     */
//    private  ChannelSftp getConnect(String host, int port, String username,
//                                          String password){
//
//        StringBuffer bufferString = new StringBuffer();
//        bufferString.append(host);
//        bufferString.append(port);
//        bufferString.append(username);
//        bufferString.append(password);
//        String mad5 = MD5Util.MD5(bufferString.toString());
//        if(sftpMap.containsKey(mad5)){
//            return sftpMap.get(mad5);
//        }else{
//            ChannelSftp channelSftp  =  linkSftp.connect(host,port,username,password);
//            sftpMap.put(mad5,channelSftp);
//            return channelSftp;
//        }
//    }

    /**
     * 上传文件
     */
    private  boolean upload(String directory, InputStream fileStream, String fileName) {
        try {
            channelSftp.cd(directory);
            channelSftp.put(fileStream, fileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public  void download( String downloadFile,String saveFile, ChannelSftp sftp){
        download(uploadServerFileRootPath,downloadFile,saveFile,sftp);
    }
    /**
     * 下载文件
     * @param directory 下载目录
     * @param downloadFile 下载的文件
     * @param saveFile 存在本地的路径
     * @param sftp
     */
    private  boolean download(String directory, String downloadFile,String saveFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            File file=new File(saveFile);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            sftp.get(downloadFile,fileOutputStream);
            fileOutputStream.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public  void delete(String deleteFile,ChannelSftp sftp){
        delete(uploadServerFileRootPath,deleteFile,sftp);
    }
    /**
     * 删除文件
     * @param directory 要删除文件所在目录
     * @param deleteFile 要删除的文件
     * @param sftp
     */
    private  void delete(String directory, String deleteFile, ChannelSftp sftp) {
        try {
            sftp.cd(directory);
            sftp.rm(deleteFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  Vector listFiles(ChannelSftp sftp)throws SftpException {
        return listFiles(uploadServerFileRootPath,sftp);
    }
    /**
     * 列出目录下的文件
     * @param directory 要列出的目录
     * @param sftp
     * @return
     * @throws SftpException
     */
    private   Vector listFiles(String directory, ChannelSftp sftp) throws SftpException{
        return sftp.ls(directory);
    }

    public String uploadFile(MultipartFile file, String fileName) {
        //上传到服务器
        boolean uploadResult;
        try {
            uploadResult = upload(this.uploadServerFileRootPath,file.getInputStream(),fileName);
        } catch (IOException e) {
            uploadResult =false;
        }
        if(uploadResult){
            UaaFile uaaFile = new UaaFile();
            uaaFile.setId(UUIDGenerator.getUUID());
            uaaFile.setCreatedId("0");
            uaaFile.setUpdatedId("0");
            uaaFile.setTenantCode("0");
            uaaFile.setSize(""+file.getSize());
            uaaFile.setName(fileName);
            uaaFile.setServiceIp(this.ipAddr);
            uaaFile.setRootFilePath(this.uploadServerFileRootPath);
            uaaFile.setRelFilePath("/"+fileName);
            uaaFile.setCreatedDate(Instant.now());
            uaaFile.setUpdateDate(Instant.now());
            fileRepository.save(uaaFile);
            return uaaFile.getId();
        }else {
            return "";
        }
    }
}

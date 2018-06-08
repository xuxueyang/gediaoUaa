package uaa.service.upload;


import com.jcraft.jsch.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uaa.config.ApplicationProperties;
import uaa.domain.uaa.UaaFile;
import uaa.repository.uaa.UaaFileRepository;
import uaa.service.dto.upload.UaaFileDTO;
import util.UUIDGenerator;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.*;

import java.lang.reflect.Field;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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
    private Session sshSession;
    private JSch jsch;
    private Channel channel;

    private void connect(){
        if(channelSftp==null||channelSftp.isClosed()){
            this.ipAddr = applicationProperties.getConfig().getFileService().getIpAddr();
            this.username = applicationProperties.getConfig().getFileService().getUsername();
            this.password = applicationProperties.getConfig().getFileService().getPassword();
            this.uploadServerFileRootPath = applicationProperties.getConfig().getFileService().getUploadServerFileRootPath();
//            this.channelSftp  =  connect(ipAddr,22,username,password);
            try {
                jsch = new JSch();
                jsch.getSession(username, ipAddr, 22);
                sshSession = jsch.getSession(username, ipAddr, 22);
                System.out.println("Session created.");
                sshSession.setPassword(password);
                Properties sshConfig = new Properties();
                sshConfig.put("StrictHostKeyChecking", "no");
                sshSession.setConfig(sshConfig);
                sshSession.connect();
//                System.out.println("Session connected.");
//                System.out.println("Opening Channel.");
                channel = sshSession.openChannel("sftp");
                channel.connect();
                this.channelSftp = (ChannelSftp) channel;
//                System.out.println("Connected to " + ipAddr + ".");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
//    private ChannelSftp connect(String host, int port, String username,
//                                String password) {
//        ChannelSftp sftp = null;
//        try {
//            JSch jsch = new JSch();
//            jsch.getSession(username, host, port);
//            Session sshSession = jsch.getSession(username, host, port);
//            System.out.println("Session created.");
//            sshSession.setPassword(password);
//            Properties sshConfig = new Properties();
//            sshConfig.put("StrictHostKeyChecking", "no");
//            sshSession.setConfig(sshConfig);
//            sshSession.connect();
//            System.out.println("Session connected.");
//            System.out.println("Opening Channel.");
//            Channel channel = sshSession.openChannel("sftp");
//            channel.connect();
//            sftp = (ChannelSftp) channel;
//            System.out.println("Connected to " + host + ".");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return sftp;
//    }
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
            if(channelSftp==null||channelSftp.isClosed()){
                connect();
//                Class cl = ChannelSftp.class;
//                Field field =cl.getDeclaredField("server_version");
//                field.setAccessible(true);
//                field.set(channelSftp, 2);
//                channelSftp.setFilenameEncoding("GBK");
//                Thread.currentThread().sleep(5000);
            }
            //等等一会看看吧
            channelSftp.cd(directory);
            channelSftp.put(fileStream, fileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
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
    /**
     * 删除文件
     * @param directory 要删除文件所在目录
     * @param deleteFile 要删除的文件
     */
    private  void delete(String directory, String deleteFile) throws Exception{
        channelSftp.cd(directory);
        channelSftp.rm(deleteFile);
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

    public UaaFile uploadFile(MultipartFile file, String fileName,String pathFilehName) {
        //上传到服务器
        boolean uploadResult;
        try {
            uploadResult = upload(this.uploadServerFileRootPath,file.getInputStream(),pathFilehName);
        } catch (IOException e) {
            uploadResult =false;
            e.printStackTrace();
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
            uaaFile.setRelFilePath("/"+pathFilehName);
            uaaFile.setCreatedDate(Instant.now());
            uaaFile.setUpdatedDate(Instant.now());
            fileRepository.save(uaaFile);
            return uaaFile;
        }else {
            return null;
        }
    }

    public UaaFile findUaaFileById(String id) {
        UaaFile one = fileRepository.findOne(id);
        return one;
    }

    public byte[] downFile(String directory,String downloadFile,OutputStream fileOutputStream) throws Exception{
        if(channelSftp==null||channelSftp.isClosed())
            connect();
        channelSftp.cd(directory);
        ByteArrayOutputStream f = new ByteArrayOutputStream();
        channelSftp.get(downloadFile,f);
        byte[] data =   f.toByteArray();
        f.close();
        return data;
    }

    public List<UaaFileDTO> searchFilesByName(String name) {
        List<UaaFileDTO> result = new ArrayList<>();
        List<UaaFile> files  = null;
        if(name==null||"".equals(name))
            files = fileRepository.findAll();
        else{
//            files =  fileRepository.findAllByName(name);
            //JPA查询
            files = fileRepository.findAll(new Specification<UaaFile>() {
                @Override
                public Predicate toPredicate(Root<UaaFile> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                    Predicate nameQuery = criteriaBuilder.like(root.get("name"), "%" + name + "%");
                    criteriaQuery.where(nameQuery);
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get("createdDate")));
                    return criteriaQuery.getRestriction();
                }
            });
        }
        if(files!=null&&files.size()>0){
            //安装时间排序

            for(UaaFile uaaFile:files){
                UaaFileDTO dto = new UaaFileDTO();
                dto.setId(uaaFile.getId());
                dto.setName(uaaFile.getName());
                dto.setCreatedId(uaaFile.getCreatedId());
                dto.setUpdatedId(uaaFile.getUpdatedId());
                dto.setCreatedDate(uaaFile.getCreatedDate());
                dto.setUpdatedDate(uaaFile.getUpdatedDate());
                dto.setSize(uaaFile.getSize());
                result.add(dto);
            }
        }
        return result;
    }

    public void deleteFile(UaaFile uaaFile) throws Exception {
        delete(uaaFile.getRootFilePath(), uaaFile.getRelFilePath().substring(1, uaaFile.getRelFilePath().length()));
        fileRepository.delete(uaaFile);
    }
}

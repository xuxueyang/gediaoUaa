package uaa.service.upload;


import com.jcraft.jsch.*;
//import jcifs.smb.SmbFile;
//import jcifs.smb.SmbFileInputStream;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import uaa.config.ApplicationProperties;
import uaa.config.Constants;
import uaa.domain.uaa.UaaFile;
import uaa.repository.uaa.UaaFileRepository;
import uaa.service.dto.upload.UaaFileDTO;
import uaa.web.rest.util.MD5Util;
import util.UUIDGenerator;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.servlet.http.HttpServletResponse;
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
    private  int port = 22;
    private  String uploadServerFileRootPath;
    private  boolean isLoadDate = false;
    private ChannelSftp channelSftp;
    private Session sshSession;
    private JSch jsch;
    private Channel channel;
    private Properties sshConfig;

    private void connect(){
        if(channelSftp==null||channelSftp.isClosed()){
            this.ipAddr = applicationProperties.getConfig().getFileService().getIpAddr();
            this.username = applicationProperties.getConfig().getFileService().getUsername();
            this.password = applicationProperties.getConfig().getFileService().getPassword();
            this.uploadServerFileRootPath = applicationProperties.getConfig().getFileService().getUploadServerFileRootPath();
//            this.channelSftp  =  connect(ipAddr,22,username,password);
            try {
                jsch = new JSch();
                jsch.getSession(username, ipAddr, port);
                sshSession = jsch.getSession(username, ipAddr, port);
                System.out.println("Session created.");
                sshSession.setPassword(password);
                sshConfig = new Properties();
                sshConfig.put("StrictHostKeyChecking", "no");
                sshSession.setConfig(sshConfig);
                sshSession.connect();
//                System.out.println("Session connected.");
//                System.out.println("Opening Channel.");
                channel = sshSession.openChannel("sftp");
                channel.connect();
                this.channelSftp = (ChannelSftp) channel;
                //等待一会
//                Thread.currentThread().sleep(5000);
//                System.out.println("Connected to " + ipAddr + ".");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    private  ChannelSftp getConnect() {
        if(!isLoadDate){
            this.ipAddr = applicationProperties.getConfig().getFileService().getIpAddr();
            this.username = applicationProperties.getConfig().getFileService().getUsername();
            this.password = applicationProperties.getConfig().getFileService().getPassword();
            this.uploadServerFileRootPath = applicationProperties.getConfig().getFileService().getUploadServerFileRootPath();
            isLoadDate = true;
        }
        ChannelSftp sftp = null;
        try {
            JSch jsch = new JSch();
            jsch.getSession(username, ipAddr, port);
            Session sshSession = jsch.getSession(username, ipAddr, port);
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
            System.out.println("Connected to " + ipAddr + ".");
        } catch (Exception e) {

        }
        return sftp;
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
    private  boolean upload(String directory, InputStream fileStream, String fileName){
        try {
            ChannelSftp channelSftp = getConnect();
            if(channelSftp==null){
//                throw new Exception("创建连接失败");
                return  false;
            }
//            if(channelSftp==null||channelSftp.isClosed()){
//                connect();
//            }
            //等等一会看看吧
            channelSftp.cd(directory);
            channelSftp.put(fileStream, fileName);
            channelSftp.disconnect();
            return true;
        } catch (SftpException e) {
            if(e.id==0){
                return true;
            }
            e.printStackTrace();
            return false;
        }
    }

//    /**
//     * 下载文件
//     * @param directory 下载目录
//     * @param downloadFile 下载的文件
//     * @param saveFile 存在本地的路径
//     * @param sftp
//     */
//    private  boolean download(String directory, String downloadFile,String saveFile, ChannelSftp sftp) {
//        try {
//            sftp.cd(directory);
//            File file=new File(saveFile);
//            FileOutputStream fileOutputStream = new FileOutputStream(file);
//            sftp.get(downloadFile,fileOutputStream);
//            fileOutputStream.close();
//            channelSftp.exit();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//            return false;
//        }
//    }
//
//    public  Vector listFiles(ChannelSftp sftp)throws SftpException {
//        return listFiles(uploadServerFileRootPath,sftp);
//    }
//    /**
//     * 列出目录下的文件
//     * @param directory 要列出的目录
//     * @param sftp
//     * @return
//     * @throws SftpException
//     */
//    private   Vector listFiles(String directory, ChannelSftp sftp) throws SftpException{
//        return sftp.ls(directory);
//    }

    public UaaFile uploadFile(MultipartFile file, String fileName,String pathFilehName) {
        //判断MD5码
        String md5 = null;
        try {
            md5 = DigestUtils.md5Hex(file.getInputStream());
        } catch (IOException e) {

        }
        UaaFile uaaFile = null;
        if(md5!=null){
            List<UaaFile> list = fileRepository.findAllByMd5(md5);
            if(list!=null&list.size()>0)
                uaaFile =list.get(0);
        }
        if(uaaFile==null){
            //上传到服务器
            boolean uploadResult;
            try {
                uploadResult = upload(this.uploadServerFileRootPath,file.getInputStream(),pathFilehName);
            } catch (IOException e) {
                uploadResult =false;
                e.printStackTrace();
            }
            if(uploadResult){
                uaaFile = new UaaFile();
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
                uaaFile.setMd5(md5);
                fileRepository.save(uaaFile);
                return uaaFile;
            }else {
                return null;
            }
        }else{
            //复制现在的UaaFile
            UaaFile newFile = new UaaFile();
            newFile.setId(UUIDGenerator.getUUID());
            newFile.setCreatedId("0");
            newFile.setUpdatedId("0");
            newFile.setTenantCode("0");
            newFile.setStatus(Constants.FILE_STATUS_SAVE);
            newFile.setSize(uaaFile.getSize());
            newFile.setName(fileName);
            newFile.setServiceIp(uaaFile.getServiceIp());
            newFile.setRelFilePath(uaaFile.getRelFilePath());
            newFile.setRootFilePath(uaaFile.getRootFilePath());
            newFile.setCreatedDate(Instant.now());
            newFile.setUpdatedDate(Instant.now());
            newFile.setMd5(uaaFile.getMd5());
            fileRepository.save(newFile);
            return newFile;
        }

    }

    public UaaFile findUaaFileById(String id) {
        UaaFile one = fileRepository.findOne(id);
        if(Constants.FILE_STATUS_DELETE.equals(one))
            return null;
        //还需要判断远程文件存不存在
        return one;
    }
//    public void downFile_smb(String directory, String downloadFile,OutputStream outputStream){
//        InputStream in = null;
//        OutputStream out = null;
//        try {
//        SmbFile remoteFile = new SmbFile("smb://"+username+password+"@"+this.ipAddr+directory+"/"+downloadFile);
//
//        if (remoteFile != null && remoteFile.exists()) {
//
//            String fileName = remoteFile.getName();
//            in = new BufferedInputStream(new SmbFileInputStream(remoteFile));
//            out = new BufferedOutputStream(outputStream);
//            byte[] buffer = new byte[1024];
//            while (in.read(buffer) != -1) {
//                out.write(buffer);
//                buffer = new byte[1024];
//            }
//        }else{
//            //文件不存在
//        }
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            try {
//                out.close();
//                out = null;
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//
//                try {
//                    in.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    }

    public byte[] downFile(UaaFile uaaFile,String directory, String downloadFile,OutputStream outputStream) throws Exception{
        //TODO 为了不影响上传的功能，下载均新建个连接
        ChannelSftp channelSftp = getConnect();
        if(channelSftp==null){
            throw new  Exception("创建连接失败");
        }
        channelSftp.cd(directory);
        ByteArrayOutputStream f = new ByteArrayOutputStream();
        channelSftp.get(downloadFile,f);
//        InputStream inputStream = channelSftp.get(directory + "/" + downloadFile);
//        BufferedInputStream br = new BufferedInputStream(inputStream);
//        int buf_size = 1024;
//        byte[] buffer = new byte[buf_size];
//        int len = 0;
//        while (-1 != (len = inputStream.read(buffer, 0, buf_size))) {
//            outputStream.write(buffer, 0, len);
//        }

//        byte[] data = new byte[Integer.parseInt(size)];
//        whil{
//            outputStream.write(br.readLine());
//        }
        byte[] data =   f.toByteArray();
        f.close();
        channelSftp.disconnect();
        //将文件下载计数+1(自己的文件以及md5相同的）
        if(uaaFile.getMd5()==null||"".equals(uaaFile.getMd5())){
            //这种情况只需要要自己的计数+1
            if(uaaFile.getDownNum()==null)
                uaaFile.setDownNum("1");
            uaaFile.setDownNum("" + Integer.parseInt(uaaFile.getDownNum())+1);
            fileRepository.save(uaaFile);
        }else{
            //找出一样的MD5的
            List<UaaFile> allByMd5 = fileRepository.findAllByMd5(uaaFile.getMd5());
            for(UaaFile file:allByMd5){
                if(file.getDownNum()==null)
                    file.setDownNum("0");
                file.setDownNum("" + Integer.parseInt(file.getDownNum())+1);
                fileRepository.save(file);
            }
        }
        return data;
    }
    public void downFile(String size,String name,String directory,String downloadFile,HttpServletResponse response) throws Exception{
        // 清空response
        response.reset();
        response.addHeader("Accept-Language" ,"zh-cn,zh");
        response.addHeader("Content-Disposition", "attachment;filename=" + name);
        response.addHeader("Content-Length", ""+size);
        response.setContentType("application/octet-stream");
        if(channelSftp==null||channelSftp.isClosed())
            connect();
//
        channelSftp.cd(directory);
        BufferedOutputStream f = new BufferedOutputStream(response.getOutputStream());
        channelSftp.get(downloadFile,f);
        f.flush();
//        //TODO 先缓存到本地上
//        channelSftp.cd(directory);
//        File file=new File("D:/tmp/"+downloadFile);
//        FileOutputStream fileOutputStream = new FileOutputStream(file);
//        channelSftp.get(downloadFile,fileOutputStream);
//        fileOutputStream.close();
//        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
//        String data = null;
//        while((data = br.readLine())!=null)
//        {
//            response.getOutputStream().write(data.getBytes());
//        }

    }

    public List<UaaFileDTO> searchFilesByName(String name) {
        if(name==null)
            name = "";
        List<UaaFileDTO> result = new ArrayList<>();
        List<UaaFile> files  = null;
//        TODO if(name==null||"".equals(name))
//            files = fileRepository.findAll();
//        else{
////            files =  fileRepository.findAllByName(name);
//            //JPA查询
////            files = fileRepository.findAll(new Specification<UaaFile>() {
////                @Override
////                public Predicate toPredicate(Root<UaaFile> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
////                    Predicate statusQuery = cb.notEqual(root.get("status"), Constants.FILE_STATUS_DELETE);
////                    Predicate nameQuery = cb.like(root.get("name").as(String.class), "%"+name+"%");
////                    cb.and(statusQuery,nameQuery);
////                    criteriaQuery.orderBy(cb.asc(root.get("createdDate").as(Instant.class)));
////                    return criteriaQuery.getRestriction();
////                }
////            });
//            files = fileRepository.findAllByStatusNotAndNameLike(Constants.FILE_STATUS_DELETE,name);
//        }
        files = fileRepository.findAll();
        if(files!=null&&files.size()>0){
            for(UaaFile uaaFile:files){
                //自己判断name的状态的删选
                if(Constants.FILE_STATUS_DELETE.equals(uaaFile.getStatus()))
                    continue;
                if(uaaFile.getName()==null||!uaaFile.getName().contains(name))
                    continue;
                UaaFileDTO dto = new UaaFileDTO();
                dto.setId(uaaFile.getId());
                dto.setName(uaaFile.getName());
                dto.setCreatedId(uaaFile.getCreatedId());
                dto.setUpdatedId(uaaFile.getUpdatedId());
                dto.setCreatedDate(uaaFile.getCreatedDate());
                dto.setUpdatedDate(uaaFile.getUpdatedDate());
                dto.setSize(uaaFile.getSize());
                if(uaaFile.getDownNum()==null)
                    uaaFile.setDownNum("0");
                dto.setDownNum(uaaFile.getDownNum());
                dto.setMd5(uaaFile.getMd5());
                result.add(dto);
            }
        }
        return result;
    }

    public void deleteFile(UaaFile uaaFile) throws Exception {
//        delete(uaaFile.getRootFilePath(), uaaFile.getRelFilePath().substring(1, uaaFile.getRelFilePath().length()));
//        fileRepository.delete(uaaFile);
        uaaFile.setStatus(Constants.FILE_STATUS_DELETE);
        fileRepository.save(uaaFile);
    }
}

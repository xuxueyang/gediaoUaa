package uaa.service.app.chat.rebot;

import org.checkerframework.checker.units.qual.A;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uaa.config.Constants;
import uaa.domain.app.chat.AppChatContent;
import uaa.domain.uaa.UaaVisitRecord;
import uaa.repository.app.blog.AppChatContentRepository;
import uaa.repository.uaa.UaaVisitCountRepository;
import uaa.service.dto.app.chat.AppChatReplyDto;
import util.UUIDGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.time.ZonedDateTime;
import java.util.Scanner;

@Service
@Transactional
public class AppChatTLRobotService  {
    @Autowired
    private AppChatContentRepository appChatContentRepository;
    @Autowired
    private UaaVisitCountRepository uaaVisitCountRepository;

    public static void main(String[] args) {
        //声明并实例化我们刚刚封装好的工具类
        AppChatTLRobotService util = new AppChatTLRobotService();
        //接收用户输入
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()){
            //直接输出机器人的回复
            System.err.println("Ta 对你说 -> " + util.getMessage(scanner.nextLine()));
        }
    }

    //存储APIkey
    public static final String API_KEY = "876112e84b3c448d9f34e8df6ebd1d22";
    //存储接口请求地址
    public static final String API_URL = "http://www.tuling123.com/openapi/api";

    /**
     * 拼接出我们的接口请求地址
     *
     * @param msg 需要发送的消息
     * @return
     */
    private String setParameter(String msg) {
        //在接口请求中 中文要用URLEncoder encode成UTF-8
        try {
            return API_URL + "?key=" + API_KEY + "&info=" + URLEncoder.encode(msg, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 拿到消息回复的内容的方法
     * @param json 请求接口得到的JSON
     * @return text的部分
     */
    private String getString(String json){
        try {
            JSONObject object = new JSONObject(json);
            return object.getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 提供对外公开的方法用于最终拿到机器人回复的消息
     * @param msg 传入你需要发送的信息
     * @return 机器人对你的回复
     */
    public String getMessage(String msg){
        return getString(getHTML(setParameter(msg)));
    }


    private String getHTML(String url) {
        StringBuffer buffer = new StringBuffer();
        BufferedReader bufferedReader = null;
        try {
            //创建URL对象
            URL u = new URL(url);
            //打开连接
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            //从连接中拿到InputStream并由BufferedReader进行读取
            bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
            String line = "";
            //循环每次加入一行HTML内容 直到最后一行
            while ((line = bufferedReader.readLine()) != null) {
                buffer.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                //结束时候关闭释放资源
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return buffer.toString();
    }

    /**
     * 根据回复
     * @param chatContent
     * @return
     */
    public AppChatReplyDto getReply(String chatContent,String ipAddr,String userId) {
        //保存这次的请求
        AppChatContent currentContent = new AppChatContent();
        String currentUUID = UUIDGenerator.getUUID();
        currentContent.setId(currentUUID);
        currentContent.setStatus(Constants.SAVE);
        currentContent.setCreatedDate(ZonedDateTime.now());
        currentContent.setUpdatedDate(ZonedDateTime.now());
        String createdId = null;
        if(userId==null){
            UaaVisitRecord byIpOrderByCreatedDate = uaaVisitCountRepository.findByIpOrderByCreatedDate(ipAddr);
            if(byIpOrderByCreatedDate!=null){
                createdId=byIpOrderByCreatedDate.getId();
            }
        }
        if(createdId==null){
            createdId = ipAddr+"_"+UUIDGenerator.getUUID();
        }
        currentContent.setCreatedId(userId==null?createdId:userId);
        currentContent.setUpdatedId(userId==null?createdId:userId);
        currentContent.setContent(chatContent);
        currentContent.setReplyId("");
        currentContent.setReplyType(Constants.REPLY_Type.All.name());

        String message = getMessage(chatContent);
        AppChatReplyDto dto = new AppChatReplyDto();
        AppChatContent appChatContent = new AppChatContent();
        // 记录本次的聊天内容(聊天机械人的）和本次发送的消息
        String uuid = UUIDGenerator.getUUID();
        appChatContent.setId(uuid);
        appChatContent.setStatus(Constants.SAVE);
        appChatContent.setCreatedDate(ZonedDateTime.now());
        appChatContent.setUpdatedDate(ZonedDateTime.now());
        appChatContent.setCreatedId("reboot");
        appChatContent.setUpdatedId("reboot");
        appChatContent.setContent(message);
        appChatContent.setReplyId(currentUUID);
        appChatContent.setReplyType(Constants.REPLY_Type.All.name());
        BeanUtils.copyProperties(appChatContent,dto);
        //如果是未登录的，允许匿名（为以后匿名做铺垫，采用IP+UUID的形式，标识）
//        appChatContent.setCreatedId(userId==null?ipAddr+"_"+UUIDGenerator.getUUID():userId);

        return dto;
    }
//---------------------
//    作者：4everlynn
//    来源：CSDN
//    原文：https://blog.csdn.net/disware/article/details/78827307
//    版权声明：本文为博主原创文章，转载请附上博文链接！
}

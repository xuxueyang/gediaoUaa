package uaa.web.rest.demo;

//import sun.misc.BASE64Encoder;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MessageDigestDemo extends Thread {
    public void run() {
        String text = "abc";
        byte data[] = null;
        MessageDigest m;
        try {
            data = text.getBytes("UTF8");
            m = MessageDigest.getInstance("MD5");
            m.update(data);
            byte resultData[] = m.digest();
            System.out.println(convertToHexString(resultData));
        } catch (NoSuchAlgorithmException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
// TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    static String convertToHexString(byte data[]) {
        StringBuffer strBuffer = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            strBuffer.append(Integer.toHexString(0xff & data[i]));
        }
        return strBuffer.toString();
    }
//    在这里我们将对计算生成的md5使用 sun.misc.BASE64Encoder进行简单的加密。
//
//    public String md5sumWithEncoder(String text) throws NoSuchAlgorithmException,
//        UnsupportedEncodingException{
//        /*确定计算方法*/
//        MessageDigest md5=MessageDigest.getInstance("MD5");
//        BASE64Encoder base64en = new BASE64Encoder();
//        /*加密后的散列码字符串*/
//        String strMd5=base64en.encode(md5.digest(text.getBytes("utf-8")));
//        return strMd5;
//    }
}

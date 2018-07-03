package uaa.web.rest.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;



import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

/**
 * rsa加密
 * 此类主要针对于jsencrypt.js给明文加密，server端java解密
 * @author liangjiawei
 *
 */
@SuppressWarnings("restriction")
public class RSAUtil {
    public static final Provider provider = new BouncyCastleProvider();

    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDMUEUdN4uipfDE1Xzxv5hlDb4L" +
        "cTd16amKcbAb18ZbDQPLa2vH9Vj/h+JitNwPGTk6rpBMgiYUZgoGrgue5iebwLJK" +
        "II/J9zuRIaAXXLWlErtrcTAZ42L8xvtSdR9nLwNhwwYj0gVmxHBrGSU6JyflBTCs" +
        "kiuUcRywvsitkdoKHwIDAQAB";


    private static String PRIVATE_KEY = "MIICXAIBAAKBgQDMUEUdN4uipfDE1Xzxv5hlDb4LcTd16amKcbAb18ZbDQPLa2vH" +
        "9Vj/h+JitNwPGTk6rpBMgiYUZgoGrgue5iebwLJKII/J9zuRIaAXXLWlErtrcTAZ" +
        "42L8xvtSdR9nLwNhwwYj0gVmxHBrGSU6JyflBTCskiuUcRywvsitkdoKHwIDAQAB" +
        "AoGAD9l6QxX4AxoUX3CgTLCsRcqXH5Qc1UzXFiJToxVOEeokjTc8GTrzOH92FGor" +
        "WBCA3AiGsSi3g4WW6YW+6Qi5sbvLE1IgE8jIczrrdDQCtio+NtUW7tbmQzADVans" +
        "RzoMriMXjnoo1L6lQaafEx6gHpHOBAbdFHcow+Rt2Ks9V6ECQQD8/EqaUw28oVuG" +
        "uBTmsIgh/idlqXE3wLC5c9nymUdD/ymyoeFrjwzOAIPPJkGvnQy5Fr7Eu1gb+LjY" +
        "PYdJd7YLAkEAzr+CYmpcT71BUag0cG9Z6+LgYQn8XgiC7MXTSQXDwOzYZx3i17Fr" +
        "6kBatOlU1NYdSUCCM7O+b3bMVaNDooRsvQJAYt0JHnP0e1GnXedUK66zFG2o4b6s" +
        "wvfnWCHg+0BRLE5r8iX23LyjaWGkIPC3XLSIA1DTLjh0P0IcokyxMNG6bwJAAYh+" +
        "CQjM4nJ+14It0V+iBRPLoiyWyIO7pNHavUHjr7yR5kHceskACd6sTYclb+aelPp7" +
        "fLKqiN0SqtfpGn1s6QJBAMmDE6ePt3pmwZc7NLkx7rCoJmCkOY2H3WpsC8o2Z5tP" +
        "2zJgSfFXG0h5/78wtq6jSwI9pXroU7MwnE8vbCKnOAk=";

    private static final String charSet = "UTF-8";

    public static final String KEY_ALGORITHM = "RSA";

    //    public static void main(String[] args){
////        String ps = "B7jJbF+Dbjnhke6QRKYo+zkGVRanzROSIWTFDolVPm7tS0p69kfOveHw7wdbrfDDsTrzZrpRf3UWPI4ALajqqNgb/kCk4arzomqMS/mt8m6+4Dqm3M+J1IwO1fSz4KznG/lGyf5gJ5Fz3tSDNvIGOkAo8pzFQNEkI8tZmHF9keA=";
//        //你好
//        String ps = "ae";
//        try {
//            System.out.println(decode(ps));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public static String decode(String encode)throws Exception{
        return RSAUtil.segmentdecrypt(encode, PRIVATE_KEY,charSet);
    }

    // 种子,改变后,生成的密钥对会发生变化
    //private static final String seedKey = "seedKey";

    /**
     * RSA最大加密明文大小
     */
    private static final int MAX_ENCRYPT_BLOCK = 117;

    /**
     * RSA最大解密密文大小
     */
    private static final int MAX_DECRYPT_BLOCK = 128;

    /**
     * 生成密钥对(公钥和私钥)
     * @return
     * @throws Exception
     */
    private static synchronized Map<String, Object> generateKeyPair() throws Exception {
        KeyPairGenerator kpg = KeyPairGenerator.getInstance(KEY_ALGORITHM, provider);
        kpg.initialize(1024, new SecureRandom());//seedKey.getBytes()
        KeyPair keyPair = kpg.generateKeyPair();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        Map<String, Object> keyMap = new HashMap<String, Object>(2);
        keyMap.put(PUBLIC_KEY, publicKey);
        keyMap.put(PRIVATE_KEY, privateKey);
        return keyMap;
    }

    private static PublicKey getPublicRSAKey(String modulus, String exponent)
        throws Exception {
        RSAPublicKeySpec spec = new RSAPublicKeySpec(
            new BigInteger(modulus, 16), new BigInteger(exponent, 16));
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM, provider);
        return kf.generatePublic(spec);
    }

    /**
     * 获取公钥
     * @param key base64加密后的公钥
     * @return
     * @throws Exception
     */
    private static PublicKey getPublicRSAKey(String key) throws Exception {
        X509EncodedKeySpec x509 = new X509EncodedKeySpec(decryptBase64(key));
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM, provider);
        return kf.generatePublic(x509);
    }

    /**
     * 获取私钥
     * @param key base64加密后的私钥
     * @return
     * @throws Exception
     */
    private static PrivateKey getPrivateRSAKey(String key) throws Exception {
        PKCS8EncodedKeySpec pkgs8 = new PKCS8EncodedKeySpec(decryptBase64(key));
        KeyFactory kf = KeyFactory.getInstance(KEY_ALGORITHM, provider);
        return kf.generatePrivate(pkgs8);
    }

    /**
     * 加密
     * @param input 明文
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    private static byte[] encrypt(String input, PublicKey publicKey)
        throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", provider);
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] re = cipher.doFinal(input.getBytes(charSet));
        return re;
    }

    /**
     * 解密
     * @param encrypted
     * @param privateKey
     * @return
     * @throws Exception
     */
    private static byte[] decrypt(byte[] encrypted, PrivateKey privateKey)
        throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/None/PKCS1Padding", provider);
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] re = cipher.doFinal(encrypted);
        return re;
    }
    /**
     * base64加密
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] decryptBase64(String key) throws Exception {
        return (new BASE64Decoder()).decodeBuffer(key);
    }

    /**
     * base64解密
     * @param key
     * @return
     * @throws Exception
     */
    private static String encryptBase64(byte[] key) throws Exception {
        return (new BASE64Encoder()).encodeBuffer(key);
    }

    private static String getPrivateKey(Map<String, Object> keyMap)
        throws Exception {
        Key key = (Key) keyMap.get(PRIVATE_KEY);
        return encryptBase64(key.getEncoded());
    }

    private static String getPublicKey(Map<String, Object> keyMap)
        throws Exception {
        Key key = (Key) keyMap.get(PUBLIC_KEY);
        return encryptBase64(key.getEncoded());
    }

    /**
     * 分段解密
     * @param jsonEncryptStr 密文 格式 base64(rsa(明文)),base64(rsa(明文)),base64(rsa(明文))
     * @param privateKey base64加密后的秘钥
     * @return
     * @throws Exception
     */
    private static String segmentdecrypt(String jsonEncryptStr,String privateKey,String code) throws Exception {
        String jsonStr = "";
        String[] str = jsonEncryptStr.split(",");
        if(str !=null && str.length > 0) {
            int inputLen = str.length;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            byte[] cache;
            int i = 0;
            // 对数据分段解密
            while (inputLen - 1 >= 0) {
                byte[] bt = RSAUtil.decryptBase64(str[i]);
                cache = RSAUtil.decrypt(bt, RSAUtil.getPrivateRSAKey(privateKey));
                out.write(cache, 0, cache.length);
                i++;
                inputLen--;
            }


            byte[] decryptedData = out.toByteArray();
            out.close();
            jsonStr = new String(decryptedData,code);
        }
        return jsonStr;
    }
}

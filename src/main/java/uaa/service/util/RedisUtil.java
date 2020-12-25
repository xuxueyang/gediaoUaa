package uaa.service.util;

import redis.clients.jedis.Jedis;

/**
 * @author xuxy
 * @date 2020/12/25 上午9:56
 */
public class RedisUtil {

    /**
     * 　　　　* 检查redis是否存活 　　　　* @param url  服务器地址 　　　　* @param port 端口　　　　 * @return
     */
    public static Integer getRedisIsOk(String url, int port) {
        int result = 0;
        try {
            //连接本地Redis服务 　　　　　　
            Jedis jedis = new Jedis(url, port);
//            jedis.auth("admin");
            String ping = jedis.ping();
            if (ping.equalsIgnoreCase("PONG")) {
                System.out.println("redis缓存有效！" + ping);
                result = 0;
            }
        } catch (Exception e) {
            System.out.println("redis缓存失败！");
            result = 1;
        }
        return result;
    }


    public static void main(String[] args) {

        int res = getRedisIsOk("106.14.9.239", 6379);
        if (res == 0) {
            System.out.println("redis缓存有效！" + res);
        } else {
            System.out.println("redis缓存失败！" + res);
        }
    }
}


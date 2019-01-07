package uaa.web.rest.util.redis;

import redis.clients.jedis.Jedis;

public class Redis {
    public static void main(String[] args){
        Jedis jedis = new Jedis("193.112.17.169",9200);
        jedis.set("age","12");
        System.out.println(jedis.get("age"));
    }
    private static  Jedis jedis = new Jedis("193.112.17.169",9200);
    public static Jedis getJedis(){
        return jedis;
    }
}

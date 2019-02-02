
package uaa.web.rest.util.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtil {

	// 服务器IP地址
	private static String ADDR = "193.112.17.169";

	// 端口
	private static int PORT = 9200;
	// 密码
	private static String AUTH = "Xuxueyang1";
	// 连接实例的最大连接数
	private static int MAX_ACTIVE = 1024;

	// 控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
	private static int MAX_IDLE = 200;

	// 等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException
	private static int MAX_WAIT = 10000;

	// 连接超时的时间
	private static int TIMEOUT = 10000;

	// 在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
	private static boolean TEST_ON_BORROW = true;

	private static JedisPool jedisPool = null;

	/**
	 * 初始化Redis连接池
	 */
    public static void main(String[] args){
        Jedis jedis = RedisUtil.getJedis();
        System.out.println(jedis);
    }
	static {

		try {

//			JedisPoolConfig config = new JedisPoolConfig();
//			config.setMaxTotal(MAX_ACTIVE);
//			config.setMaxIdle(MAX_IDLE);
//			config.setMaxWaitMillis(MAX_WAIT);
//			config.setTestOnBorrow(TEST_ON_BORROW);
//			jedisPool = new JedisPool(config, ADDR, PORT, TIMEOUT, AUTH);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * 获取Jedis实例
	 */

	private synchronized static Jedis getJedis() {

		try {

			if (jedisPool != null) {
				Jedis resource = jedisPool.getResource();
				return resource;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	/***
	 *
	 * 释放资源
	 */

	public static void returnResource(final Jedis jedis) {
		if (jedis != null) {
			jedisPool.returnResource(jedis);
		}

	}

}

package uaa.web.rest.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;


/**
 *
 * @author ld
 *
 */
public class ConnectUtil {
	//默认编码UTF8
	private static String CHARSET = "UTF-8";
	// 连接上一个url,获取response的返回等待时间
	public static final int SOCKET_TIMEOUT = 20 * 1000;
	// 连接一个url的连接等待时间
	public static final int CONNECT_TIMEOUT = 10 * 1000;

	public static String doGet(String url) {
		CloseableHttpClient httpClient = null;
		String result = null;
		try {
			HttpClientBuilder builder = HttpClientBuilder.create();
			httpClient = builder.build();
			HttpGet httpGet = new HttpGet(url);
			RequestConfig requestConfig = RequestConfig.custom()
			// 连接上一个url,获取response的返回等待时间
			.setSocketTimeout(SOCKET_TIMEOUT)
			// 连接一个url的连接等待时间
			.setConnectTimeout(CONNECT_TIMEOUT).build();
			httpGet.setConfig(requestConfig);
			HttpResponse response = httpClient.execute(httpGet);
			if (response != null) {
				HttpEntity resEntity = response.getEntity();
				if (resEntity != null) {
					result = EntityUtils.toString(resEntity, CHARSET);
				}
			}
		} catch (Exception e) {
				e.printStackTrace();
		} finally {
			try {
				if (httpClient != null) {
					httpClient.close();
				}
			} catch (IOException | RuntimeException e) {
				e.printStackTrace();
			}
		}
		return result;
	}


}

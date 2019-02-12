package uaa.web.rest.util.es;

import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
//import org.elasticsearch.common.settings.
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * 向ES添加索引对象
 * @author donlian
 */
public class IndexTest {
//    public static void main(String[] argv) throws UnknownHostException {
//        Settings settings =  Settings.builder()
//            //指定集群名称
//            .put("cluster.name", "elasticsearch")
//            //探测集群中机器状态
//            .put("client.transport.sniff", true).build();
//        /*
//         * 创建客户端，所有的操作都由客户端开始，这个就好像是JDBC的Connection对象
//         * 用完记得要关闭
//         */
//        Client client =  new PreBuiltTransportClient(settings)
//            .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("193.112.17.169"), 9200));
//        String json = ESUtils.toJson(new LogModel());
//        //在这里创建我们要索引的对象
//        IndexResponse response = client.prepareIndex("twitter", "tweet")
//            //必须为对象单独指定ID
//            .setId("1")
//            .setSource(json)
//            .execute()
//            .actionGet();
//        //多次index这个版本号会变
//        System.out.println("response.version():"+response.getVersion());
//        client.close();
//    }
}

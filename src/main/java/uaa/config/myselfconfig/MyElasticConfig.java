package uaa.config.myselfconfig;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.context.annotation.Bean;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class MyElasticConfig {
//    @Bean
    public TransportClient clent() throws UnknownHostException{
        InetSocketTransportAddress node = new InetSocketTransportAddress(
            InetAddress.getByName("localhost"),
            9300
        );
        Settings settings = Settings.builder().put("cluster.name","TODO").build();
        TransportClient client = new PreBuiltTransportClient(settings);
        client.addTransportAddress(node);
        return client;
    }
}

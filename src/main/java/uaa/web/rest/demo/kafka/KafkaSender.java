package uaa.web.rest.demo.kafka;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Date;
import java.util.UUID;

public class KafkaSender {
//    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private Gson gson = new GsonBuilder().create();

    public void send() {
        Message message = new Message();
        message.setId(System.currentTimeMillis());
        message.setMsg(UUID.randomUUID().toString());
        message.setSendTime(new Date());
        kafkaTemplate.send("abc123", gson.toJson(message));
    }

}

package uaa.service;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring4.SpringTemplateEngine;
import uaa.config.ApplicationProperties;
import uaa.config.Constants;
import uaa.domain.SenderInfo;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

@Transactional
@Service
public class EmailService {
    private final Logger log = LoggerFactory.getLogger(EmailService.class);
    @Autowired
    private SpringTemplateEngine templateEngine;
    @Autowired
    private Sender sender;
    @Autowired
    private ApplicationProperties applicationProperties;


    public void sendEmail()throws Exception{
        Context context = new Context(Locale.CHINA);
        context.setVariable(Constants.EMAIL_VAR_URL,"http://193.112.161.157:8080");
        String subject = applicationProperties.getConfig().getEmail().getSubject().getCreate();
        String emailContent = templateEngine.process(Constants.EMAIL_TEMPLATE_TEST_SEND, context);

        SenderInfo senderInfo = new SenderInfo();
        senderInfo.setSubject(subject);
        senderInfo.setToAddress("1059738716@qq.com");
        senderInfo.setHtmlMessage(emailContent);
        senderInfo.setTextMessage("测试Uaa短信");
        sender.sendHTMLFormattedEmail(senderInfo);
    }
}

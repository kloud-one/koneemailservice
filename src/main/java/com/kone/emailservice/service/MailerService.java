package com.kone.emailservice.service;


import com.kone.emailservice.domain.EmailDTO;
import io.github.jhipster.config.JHipsterProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import javax.transaction.Transactional;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;

@Service
@Transactional
public class MailerService {

    private final Logger log = LoggerFactory.getLogger(MailerService.class.getName());
    private final JavaMailSender javaMailSender;
    private final JHipsterProperties jHipsterProperties;

    public MailerService(JavaMailSender javaMailSender, JHipsterProperties jHipsterProperties) {
        this.javaMailSender = javaMailSender;
        this.jHipsterProperties = jHipsterProperties;
    }

    @Async
    public void sendEmail(String to, String subject, String content, boolean isMultipart, boolean isHtml) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        try {
            MimeMessageHelper message = new MimeMessageHelper(mimeMessage, isMultipart, StandardCharsets.UTF_8.name());
            message.setTo(to);
            message.setFrom(jHipsterProperties.getMail().getFrom());
            message.setSubject(subject);
            message.setText(content, isHtml);
            javaMailSender.send(mimeMessage);
            log.debug("Sent email to User '{}'", to);
        } catch (Exception e ) {
            if (log.isDebugEnabled()) {
                log.warn("EmailDTO could not be sent to user '{}'", to, e);
            } else {
                log.warn("EmailDTO could not be sent to user '{}': {}", to, e.getMessage());
            }
        }
    }

    @Async
    public void sendEmail(EmailDTO emailDTO) {
        log.debug("Sending details through email to {}", emailDTO.getToEmail());
//        sendEmail(emailDTO.getToEmail(), emailDTO.getSubject(), emailDTO.getMsgContent(), false, true);
    }

}

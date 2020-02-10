package com.company.project.infrastructure.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class MailSenderImpl implements MailSender {

    private static final Logger log = LoggerFactory.getLogger(MailSenderImpl.class);
    private JavaMailSender javaMailSender;

    @Autowired
    public MailSenderImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    @Async
    public void send(Mail mail) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject(mail.getTitle());
        message.setText(mail.getContent());
        message.setTo(mail.getRecipient());

        sendMessage(message, mail.getRecipient());
    }

    private void sendMessage(SimpleMailMessage message, String recipient) {
        try {
            javaMailSender.send(message);
            log.info("Mail sent to {}", recipient);
        } catch (MailParseException e) {
            String errorMsg = String.format("Mail did not send to %s - failure when parsing the message", recipient);
            log.error(errorMsg, e);
        } catch (MailAuthenticationException e) {
            String errorMsg = String.format("Mail did not send to %s - authentication error", recipient);
            log.error(errorMsg, e);
        } catch (MailSendException e) {
            String errorMsg = String.format("Mail did not send to %s - failure when sending the message", recipient);
            log.error(errorMsg, e);
        }
    }
}

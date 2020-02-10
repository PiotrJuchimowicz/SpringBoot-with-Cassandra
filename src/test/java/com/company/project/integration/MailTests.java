package com.company.project.integration;

import com.company.project.application.MessageManager;
import com.company.project.application.dto.MagicNumberDto;
import com.company.project.application.dto.MessageDto;
import com.company.project.infrastructure.repository.MessageSpringRepository;
import com.company.project.integration.util.EmbeddedCassandraTestConfig;
import com.icegreen.greenmail.util.GreenMail;
import com.icegreen.greenmail.util.ServerSetupTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import javax.mail.Address;
import javax.mail.internet.MimeMessage;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EmbeddedCassandraTestConfig.class, properties = {"spring.mail.host = localhost", "spring.mail.port = 3025"})
public class MailTests {

    private MessageSpringRepository repository;
    private MessageManager manager;
    private GreenMail greenMail;

    @Autowired
    public MailTests(MessageSpringRepository repository, MessageManager manager) {
        this.repository = repository;
        this.manager = manager;
    }

    @BeforeEach
    public void cleanUp() {
        greenMail = new GreenMail(ServerSetupTest.SMTP);
        greenMail.start();
        repository.deleteAll();
    }

    @AfterEach
    public void setUp() {
        greenMail.stop();
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = {"API"})
    @DisplayName("Should properly send mail")
    public void sendMailTest() throws Exception {
        //given
        String recipient = "example@email.com";
        String subject = "title";
        String content = "content";
        int magicNumber = 10;
        MessageDto message = new MessageDto(recipient, subject, content, magicNumber);

        //when
        manager.store(message);
        manager.send(new MagicNumberDto(magicNumber));
        greenMail.waitForIncomingEmail(5000, 1);

        //then
        List<MimeMessage> messages = Arrays.asList(greenMail.getReceivedMessages());
        assertEquals(1, messages.size());
        MimeMessage mimeMessage = messages.get(0);

        assertThatEmailWasSentToSpecifiedRecipient(mimeMessage, recipient);
        assertEquals(subject, mimeMessage.getSubject());
        assertEquals(content, mimeMessage.getContent().toString().trim());
    }

    private void assertThatEmailWasSentToSpecifiedRecipient(MimeMessage mimeMessage, String recipientEmail) throws Exception {
        List<String> recipientEmails = Arrays.stream(mimeMessage.getAllRecipients()).map(Address::toString).collect(Collectors.toList());
        assertEquals(1, recipientEmails.size());
        assertTrue(recipientEmails.contains(recipientEmail));
    }
}

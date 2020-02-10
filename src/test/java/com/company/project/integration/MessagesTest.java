package com.company.project.integration;

import com.company.project.application.MessageManager;
import com.company.project.application.dto.MessageDto;
import com.company.project.application.dto.MessageListDto;
import com.company.project.application.dto.Pagination;
import com.company.project.domain.Message;
import com.company.project.infrastructure.repository.MessageSpringRepository;
import com.company.project.integration.util.EmbeddedCassandraTestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = EmbeddedCassandraTestConfig.class)
public class MessagesTest {

    private MessageManager manager;
    private MessageSpringRepository repository;

    @Autowired
    public MessagesTest(MessageManager manager, MessageSpringRepository repository) {
        this.repository = repository;
        this.manager = manager;
    }

    @BeforeEach
    public void cleanUp() {
        repository.deleteAll();
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = {"API"})
    @DisplayName("Should add message")
    public void addMessageTest() {
        //given
        String email = "example@email.com";
        MessageDto message = new MessageDto(email, "title", "content", 10);

        //when
        manager.store(message);

        //then
        Pagination firstPage = new Pagination(0, 20);
        MessageListDto messageList = manager.getMessages(email, firstPage);
        assertTrue(messageList.getMessages().contains(message));
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = {"API"})
    @DisplayName("Should remove outdated message")
    public void removeOutdatedMessageTest() {
        //given
        String email = "example@email.com";
        MessageDto message = new MessageDto(email, "title", "content", 10);

        //when
        manager.store(message);
        moveMessageToPast(repository.findAll().get(0));

        //then
        await().atMost(10, TimeUnit.SECONDS).
                untilAsserted(() -> assertThat(repository.findAll()).isEmpty());
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = {"API"})
    @DisplayName("Should return messages with given email")
    public void returnMessagesTest() {
        //given
        String email = "example@mail.com";
        int count = 12;
        addMessages(email, count);
        addMessages("other@email.com", 4);
        addMessages("other2@email.com", 3);

        //when
        Pagination pagination = new Pagination(0, count);
        MessageListDto messageList = manager.getMessages(email, pagination);

        //then
        assertEquals(count, messageList.getTotalCount());
        assertTrue(messageList.getMessages().stream().allMatch(message -> email.equals(message.getEmail())));
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = {"API"})
    @DisplayName("Should return page if received negative page number")
    public void returnPageIfReceivedNegativePageNumberTest() {
        //given
        String email = "example@email.com";
        int messagesCount = 25;
        int pageLimit = 10;
        addMessages(email, messagesCount);

        //when
        Pagination wrongPagination = new Pagination(-2,  pageLimit);
        MessageListDto messageList = manager.getMessages(email, wrongPagination);

        //then
        assertEquals(pageLimit, messageList.getMessages().size());
    }

    @Test
    @WithMockUser(username = "user", password = "pass", roles = {"API"})
    @DisplayName("Should return last page if received page number out of bound")
    public void shouldReturnLastPageTest() {
        //given
        String email = "example@email.com";
        int messagesCount = 25;
        int pageLimit = 10;
        addMessages(email, messagesCount);

        //when
        Pagination wrongPagination = new Pagination(1000,  pageLimit);
        MessageListDto messageList = manager.getMessages(email, wrongPagination);

        //then
        assertEquals(messagesCount % pageLimit, messageList.getMessages().size());
    }

    private void moveMessageToPast(Message message) {
        message.setCreateDate(message.getCreateDate().minusYears(2));
        repository.save(message);
    }

    private void addMessages(String email, int count) {
        MessageDto message = new MessageDto(email, "title", "content", 10);

        for (int i = 0; i < count; i++) {
            manager.store(message);
        }
    }
}
package com.company.project.infrastructure.scheduler;

import com.company.project.domain.Message;
import com.company.project.infrastructure.repository.MessageSpringRepository;
import com.company.project.infrastructure.time.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageSchedulerImpl {

    private MessageSpringRepository repository;
    private TimeProvider timeProvider;

    @Autowired
    public MessageSchedulerImpl(MessageSpringRepository repository, TimeProvider timeProvider) {
        this.repository = repository;
        this.timeProvider = timeProvider;
    }

    @Scheduled(fixedDelay = 5000)
    public void removeOutdatedMessages() {
        List<Message> outdatedMessages = repository.findAll().stream()
                .filter(message -> timeProvider.now().minusMinutes(5).isAfter(message.getCreateDate()))
                .collect(Collectors.toList());

        repository.deleteAll(outdatedMessages);
    }
}

package com.company.project.application;

import com.company.project.application.dto.MessageListDto;
import com.company.project.application.dto.Pagination;
import com.company.project.domain.Message;
import com.company.project.infrastructure.mail.MailSender;
import com.company.project.infrastructure.repository.MessageSpringRepository;
import com.company.project.application.dto.MagicNumberDto;
import com.company.project.application.dto.MessageDto;
import com.company.project.infrastructure.time.TimeProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageManagerImpl implements MessageManager {

    private static final int DEFAULT_PAGE_SIZE = 10;
    private MessageSpringRepository repository;
    private MailSender mailSender;
    private TimeProvider timeProvider;

    @Autowired
    public MessageManagerImpl(MessageSpringRepository repository, MailSender mailSender, TimeProvider timeProvider) {
        this.repository = repository;
        this.mailSender = mailSender;
        this.timeProvider = timeProvider;
    }

    @Override
    public void store(MessageDto dto) {
        Message message = new Message(dto.getEmail(), dto.getTitle(), dto.getContent(), dto.getMagicNumber(), timeProvider);
        repository.save(message);
    }

    @Override
    public void send(MagicNumberDto dto) {
        List<Message> messages = repository.findAllByMagicNumber(dto.getMagicNumber());
        messages.stream().map(Message::toMail).forEach(mailSender::send);
        repository.deleteAll(messages);
    }

    @Override
    public MessageListDto getMessages(String email, Pagination pagination) {
        long totalCount = repository.countAllByEmail(email);
        handleInvalidPagination(pagination, totalCount);
        List<Message> page = getPage(pagination, email);
        return new MessageListDto(totalCount, page.stream().map(Message::toDto).collect(Collectors.toList()));
    }

    private void handleInvalidPagination(Pagination pagination, long totalCount) {
        int limit = pagination.getLimit() <= 0 ? DEFAULT_PAGE_SIZE : pagination.getLimit();
        pagination.setLimit(limit);

        int maxPage = (int) Math.ceil((double) totalCount / limit) - 1;
        if (maxPage < 0) {
            maxPage = 0;
        }

        if (pagination.getPage() < 0) {
            pagination.setPage(0);
        } else if (pagination.getPage() > maxPage) {
            pagination.setPage(maxPage);
        }
    }

    private List<Message> getPage(Pagination pagination, String email) {
        int pageNumber = 0;
        Slice<Message> page = repository.findAllByEmail(email, CassandraPageRequest.of(pageNumber, pagination.getLimit()));

        while (page.hasNext() && pageNumber != pagination.getPage()) {
            page = repository.findAllByEmail(email, page.nextPageable());
            pageNumber++;
        }

        return page.getContent();
    }
}
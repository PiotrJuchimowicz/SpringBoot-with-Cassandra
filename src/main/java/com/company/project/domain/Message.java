package com.company.project.domain;

import com.company.project.application.dto.MessageDto;
import com.company.project.infrastructure.mail.Mail;
import com.company.project.infrastructure.time.TimeProvider;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.cassandra.core.mapping.*;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Table
public class Message {

    @PrimaryKey
    private UUID uuid = UUID.randomUUID();

    @Indexed
    private String email;

    private String title;

    private String content;

    @Column("magic_number")
    @Indexed
    private Integer magicNumber;

    @Column("create_date")
    private LocalDateTime createDate;

    private Message() {
    }

    public Message(String email, String title, String content, Integer magicNumber, TimeProvider timeProvider) {
        this.email = email;
        this.title = title;
        this.content = content;
        this.magicNumber = magicNumber;
        this.createDate = timeProvider.now();
    }

    public MessageDto toDto() {
        return new MessageDto(email, title, content, magicNumber);
    }

    public Mail toMail() {return new Mail(this.title, this.content, this.email);}

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(uuid, message.uuid) &&
                Objects.equals(email, message.email) &&
                Objects.equals(title, message.title) &&
                Objects.equals(content, message.content) &&
                Objects.equals(magicNumber, message.magicNumber) &&
                Objects.equals(createDate, message.createDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(uuid, email, title, content, magicNumber, createDate);
    }

    @Override
    public String toString() {
        return "Message{" +
                "uuid=" + uuid +
                ", email='" + email + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", magicNumber=" + magicNumber +
                ", createDate=" + createDate +
                '}';
    }
}

package com.company.project.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@AllArgsConstructor
@Getter
@Setter
public class MessageListDto {

    private long totalCount;
    private List<MessageDto> messages = new ArrayList<>();

    private MessageListDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageListDto that = (MessageListDto) o;
        return totalCount == that.totalCount &&
                Objects.equals(messages, that.messages);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalCount, messages);
    }
}

package com.company.project.application.dto;

import com.company.project.application.dto.validator.Email;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MessageDto {

    @NotNull(message = "Email must be provided")
    @Email
    private String email;

    @NotNull(message = "Title must be provided")
    private String title;

    @NotNull(message = "Content must be provided")
    private String content;

    @NotNull(message = "Magic number must be provided")
    @JsonProperty("magic_number")
    private Integer magicNumber;

    private MessageDto() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageDto that = (MessageDto) o;
        return Objects.equals(email, that.email) &&
                Objects.equals(title, that.title) &&
                Objects.equals(content, that.content) &&
                Objects.equals(magicNumber, that.magicNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(email, title, content, magicNumber);
    }
}

package com.company.project.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MagicNumberDto {

    @JsonProperty("magic_number")
    @NotNull(message = "Magic number must be provided")
    private Integer magicNumber;

    private MagicNumberDto() {
    }
}

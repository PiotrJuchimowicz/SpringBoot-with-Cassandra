package com.company.project.application;

import com.company.project.application.dto.MagicNumberDto;
import com.company.project.application.dto.MessageDto;
import com.company.project.application.dto.MessageListDto;
import com.company.project.application.dto.Pagination;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Validated
public interface MessageManager {

    @PreAuthorize("hasRole('ROLE_API')")
    void store(@Valid MessageDto message);

    @PreAuthorize("hasRole('ROLE_API')")
    void send(@Valid MagicNumberDto magicNumber);

    @PreAuthorize("hasRole('ROLE_API')")
    MessageListDto getMessages(String email, Pagination pagination);
}

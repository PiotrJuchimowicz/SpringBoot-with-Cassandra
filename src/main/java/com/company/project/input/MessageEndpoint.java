package com.company.project.input;

import com.company.project.application.MessageManager;
import com.company.project.application.dto.MagicNumberDto;
import com.company.project.application.dto.MessageDto;
import com.company.project.application.dto.MessageListDto;
import com.company.project.application.dto.Pagination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.ValidationException;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api")
public class MessageEndpoint {

    private MessageManager manager;

    @Autowired
    public MessageEndpoint(MessageManager manager) {
        this.manager = manager;
    }

    @PostMapping("/message")
    public ResponseEntity<String> store(@RequestBody MessageDto request) {
        try {
            manager.store(request);
            return ResponseEntity.ok().build();
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/send")
    public ResponseEntity<String> send(@RequestBody MagicNumberDto request) {
        try {
            manager.send(request);
            return ResponseEntity.ok().build();
        } catch (ValidationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/messages/{emailValue}")
    public MessageListDto get(@PathVariable("emailValue") String email, @RequestBody Pagination pagination) {
        return manager.getMessages(email, pagination);
    }
}

package com.company.project.infrastructure.time;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TimeProviderImpl implements TimeProvider {

    @Override
    public LocalDateTime now() {
        return LocalDateTime.now();
    }
}

package com.company.project.infrastructure.time;

import java.time.LocalDateTime;

public interface TimeProvider {

    LocalDateTime now();
}

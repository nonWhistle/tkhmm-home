package com.tkhmm.application.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.logging.Logger;

public class PostTextEvent extends ApplicationEvent {

    private static final Logger log = Logger.getLogger(PostTextEvent.class.getSimpleName());

    @Getter
    private final String message;

    @Getter
    private final Long userId;

    public PostTextEvent(Object source, String message, Long userId) {
        super(source);
        this.message = message;
        this.userId = userId;
    }
}

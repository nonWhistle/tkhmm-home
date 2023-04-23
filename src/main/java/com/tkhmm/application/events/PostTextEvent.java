package com.tkhmm.application.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.logging.Logger;

public class PostTextEvent extends ApplicationEvent {

    private static final Logger log = Logger.getLogger(PostTextEvent.class.getSimpleName());

    @Getter
    private final String message;

    public PostTextEvent(Object source, String message) {
        super(source);
        log.info("Post text event has been FIRED!");
        this.message = message;
    }
}

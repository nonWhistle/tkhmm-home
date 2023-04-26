package com.tkhmm.application.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

import java.util.logging.Logger;

public class GetJokeEvent extends ApplicationEvent {

    private static final Logger log = Logger.getLogger(GetJokeEvent.class.getSimpleName());

    @Getter
    private final Long userId;

    public GetJokeEvent(Object source, Long userId) {
        super(source);
        this.userId = userId;
        log.info("Get Joke event has been FIRED!");
    }
}

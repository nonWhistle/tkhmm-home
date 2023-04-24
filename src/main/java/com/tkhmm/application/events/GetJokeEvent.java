package com.tkhmm.application.events;

import org.springframework.context.ApplicationEvent;

import java.util.logging.Logger;

public class GetJokeEvent extends ApplicationEvent {

    private static final Logger log = Logger.getLogger(GetJokeEvent.class.getSimpleName());

    public GetJokeEvent(Object source) {
        super(source);
        log.info("Get Joke event has been FIRED!");
    }
}

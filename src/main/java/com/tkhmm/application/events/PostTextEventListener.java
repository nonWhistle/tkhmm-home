package com.tkhmm.application.events;

import com.tkhmm.application.rest.CvViewRestCall;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@AllArgsConstructor
@Component
public class PostTextEventListener implements ApplicationListener<PostTextEvent> {

    private static final Logger log = Logger.getLogger(PostTextEventListener.class.getSimpleName());

    @Override
    public void onApplicationEvent(PostTextEvent event) {
        log.info("Post text event listener has been HEARD!");

        CvViewRestCall cvViewRestCall = new CvViewRestCall();
        cvViewRestCall.postText(event.getMessage());
    }
}

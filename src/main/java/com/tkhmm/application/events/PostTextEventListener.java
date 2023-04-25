package com.tkhmm.application.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.tkhmm.application.broadcaster.CvViewBroadcaster;
import com.tkhmm.application.rest.CvViewRestCall;
import com.tkhmm.application.views.cvview.CvViewData;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@Component
@AllArgsConstructor
public class PostTextEventListener implements ApplicationListener<PostTextEvent> {

    private static final Logger log = Logger.getLogger(PostTextEventListener.class.getSimpleName());

    @Override
    public void onApplicationEvent(PostTextEvent event) {
        log.info("Post text event HEARD!");

        CvViewRestCall cvViewRestCall = new CvViewRestCall();
        cvViewRestCall.postText(event.getMessage());
    }
}

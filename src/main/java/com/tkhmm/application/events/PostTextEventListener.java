package com.tkhmm.application.events;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.tkhmm.application.rest.CvViewRestCall;
import elemental.json.JsonObject;
import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@AllArgsConstructor
@Component
public class PostTextEventListener implements ApplicationListener<PostTextEvent> {

    private static final Logger log = Logger.getLogger(PostTextEventListener.class.getSimpleName());

    @Override
    public void onApplicationEvent(PostTextEvent event) {
        log.info("Post text event listener has been HEARD!");

        Map<String, String> headers = new HashMap();
        headers.put("Content-Type", "application/octet-stream");
        headers.put("X-RapidAPI-Key", "16c69c1f13msh133d75bbaa890bap1a5920jsn34fee168be5f");
        headers.put("X-RapidAPI-Host", "jokes-by-api-ninjas.p.rapidapi.com");

        try {
            HttpResponse<String> response = Unirest.get("https://jokes-by-api-ninjas.p.rapidapi.com/v1/jokes")
                    .header("content-type", "application/octet-stream")
                    .header("X-RapidAPI-Key", "16c69c1f13msh133d75bbaa890bap1a5920jsn34fee168be5f")
                    .header("X-RapidAPI-Host", "jokes-by-api-ninjas.p.rapidapi.com")
                    .asString();

            log.info(response.getBody());
        } catch (Exception e) {
            log.warning("Unirest failed");
        }

    }
}

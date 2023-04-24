package com.tkhmm.application.events;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.tkhmm.application.broadcaster.CvViewBroadcaster;
import com.tkhmm.application.views.cvview.CvViewData;

import lombok.AllArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

@AllArgsConstructor
@Component
public class GetJokeEventListener implements ApplicationListener<GetJokeEvent> {

    private static final Logger log = Logger.getLogger(GetJokeEventListener.class.getSimpleName());

    private ObjectMapper objectMapper;

    @Override
    public void onApplicationEvent(GetJokeEvent event) {
        log.info("Get Joke event has been HEARD!");

        try {
            HttpResponse<String> response = Unirest.get("https://jokes-by-api-ninjas.p.rapidapi.com/v1/jokes")
                    .header("content-type", "application/octet-stream")
                    .header("X-RapidAPI-Key", "16c69c1f13msh133d75bbaa890bap1a5920jsn34fee168be5f")
                    .header("X-RapidAPI-Host", "jokes-by-api-ninjas.p.rapidapi.com")
                    .asString();

            String aJoke = "";
            for (JsonNode value : objectMapper.readTree(response.getBody())) {
                aJoke = value.path("joke").asText();
                log.info(aJoke);
            }

            CvViewData cvViewData = new CvViewData();
            cvViewData.setAJoke(aJoke);
            CvViewBroadcaster.broadcast(cvViewData, 1L);

        } catch (Exception e) {
            log.warning("Unirest failed");
        }

    }
}

package com.tkhmm.application.rest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/tkhmm")
public class CvViewController {

    private static final Logger log = Logger.getLogger(CvViewController.class.getSimpleName());

    protected static final String API_KEY_HEADER_NAME = "api-key";

    public CvViewController() {
        log.info("Created");
    }

    @PostMapping("/labeltext/{message}")
    @ResponseStatus(HttpStatus.OK)
    public void updateLabelTextInCvView(@PathVariable("message") String message,
                                        @RequestHeader(name = API_KEY_HEADER_NAME) String apiKey) {
        log.info("HERE -> " + message);
        log.info("HERE -> " + apiKey);
    }
}
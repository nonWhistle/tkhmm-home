package com.tkhmm.application.rest;

import com.tkhmm.application.broadcaster.CvViewBroadcaster;
import com.tkhmm.application.views.cvview.CvViewData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.logging.Logger;

import static com.tkhmm.application.rest.CvViewRestCall.ApiCredentials.API_KEY;

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
        log.info("Checking credentials of Post request");
        if (apiKey.equals(API_KEY)) {
            log.info("Credentials match, broadcasting data");
            CvViewData cvViewData = new CvViewData();
            cvViewData.setAMessage(message);
            CvViewBroadcaster.broadcast(cvViewData, 1L);
        } else {
            log.warning("Credentials do not match");
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorised");
        }
    }
}
package com.tkhmm.application.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

@NoArgsConstructor
@Component
public class CvViewRestCall {

    private static final Logger log = Logger.getLogger(CvViewRestCall.class.getSimpleName());

    /**
     * Creates the body of the JSON message
     */
    protected static final ObjectMapper objectMapper = new ObjectMapper();

    /**
     * The returned code of a successfully posted JSON message and successfully created object.
     */
    protected final static int RESPONSE_201 = 201;

    /**
     * The returned code of a successfully posted JSON message.
     */
    protected final static int RESPONSE_200 = 200;

    /**
     * Number of attempts to make before giving up.
     */
    protected static int NUMBER_OF_TRYS = 20;

    /**
     * Number of seconds to wait between each post attempt.
     */
    protected static int SECONDS_BETWEEN_TRY = 15;

    private static final String API_KEY = "abc123SuperSecretApiKey";

    private static final String ENDPOINT = "https://localhost:8080/api/cvview/";

    public void postText(String message) {
        int returnedStatus = 0;
        int count = 0;

        while ((returnedStatus != RESPONSE_200) && count < NUMBER_OF_TRYS) {
            try {
                returnedStatus = postTextToEndpoint(message);
                if (returnedStatus != RESPONSE_200) {
                    log.warning("Post text did not return " + RESPONSE_200);
                } else {
                    log.info("Post text was successfully posted");
                    return;
                }
            } catch (Exception e) {
                log.warning(String.format("Post text threw exception of type %s", e.getClass().getSimpleName()));
                log.warning(e.getLocalizedMessage());
                log.warning(e.getMessage());
            }
            count++;
            log.warning("Try " + count);
            log.warning("Waiting " + SECONDS_BETWEEN_TRY + " before trying again.");
            waitForSeconds();
        }
    }

    /**
     * Pauses for a number seconds between attempts at making the post request.
     * Moved to a method to speed up testing.
     *
     * @throws InterruptedException if interupted.
     */
    protected void waitForSeconds()  {
        try {
            Thread.sleep(TimeUnit.SECONDS.toMillis(SECONDS_BETWEEN_TRY));
        } catch (InterruptedException ex) {
            log.warning("Sleep was interrupted");
        }
    }

    /**
     * Send a request to the desired url, with a string of parameters that have been converted to json.
     *
     * @return The status code from the post request.
     * @throws UnirestException if the post was unsuccessful.
     */
    private int postTextToEndpoint(String message) throws UnirestException {

        Unirest.setTimeouts(0, 0);

        HttpResponse<String> response = Unirest.put(ENDPOINT + message)
                .header("API_KEY", API_KEY).asString();

        log.info("\n\033[1mPost Text\033[0m" +
                "\nurl: " + ENDPOINT +
                "\nAPI_KEY: " + API_KEY +
                "\nResponse status: " + response.getStatus() +
                "\nResponce status text: " + response.getStatusText() +
                "\nResponce Body: " + response.getBody());

        return response.getStatus();
    }
}
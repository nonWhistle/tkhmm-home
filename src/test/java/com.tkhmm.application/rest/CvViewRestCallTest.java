package com.tkhmm.application.rest;

import com.mashape.unirest.http.exceptions.UnirestException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.springframework.test.util.ReflectionTestUtils;



class CvViewRestCallTest {

    private final CvViewRestCall underTest = new CvViewRestCall();
    private final CvViewRestCall spy = Mockito.spy(underTest);

    @Test
    void testPostText() throws UnirestException {
        ReflectionTestUtils.setField(spy, "NUMBER_OF_TRYS", 2);
        ReflectionTestUtils.setField(spy, "SECONDS_BETWEEN_TRY", 0);

        //Check when a status 200 is returned on the first attempt.
        doReturn(200).when(spy).postTextToEndpoint(anyString(), anyLong());
        spy.postText(anyString(), anyLong());
        verify(spy, times(1)).postTextToEndpoint(anyString(), anyLong());
        reset(spy);

        //Check when a status 200 takes 2 attempts
        doReturn(500).when(spy).postTextToEndpoint(anyString(), anyLong());
        spy.postText(anyString(), anyLong());
        verify(spy, times(2)).postTextToEndpoint(anyString(), anyLong());
    }
}
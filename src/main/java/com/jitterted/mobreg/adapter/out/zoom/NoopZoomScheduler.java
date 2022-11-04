package com.jitterted.mobreg.adapter.out.zoom;

import com.jitterted.mobreg.adapter.DateTimeFormatting;
import com.jitterted.mobreg.application.port.FailedToScheduleMeeting;
import com.jitterted.mobreg.application.port.VideoConferenceScheduler;
import com.jitterted.mobreg.domain.ConferenceDetails;
import com.jitterted.mobreg.domain.Ensemble;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

// TODO : use conditional configuration to enable / disable it
@Component
public class NoopZoomScheduler implements VideoConferenceScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(NoopZoomScheduler.class);


    @Override
    public ConferenceDetails createMeeting(Ensemble ensemble) {
        LOGGER.info("NoopZoomScheduler : createMeeting {}", ensemble);
        return new ConferenceDetails("",URI.create("https://baldir.fr"),URI.create("https://baldir.fr"));
    }

    @Override
    public boolean deleteMeeting(ConferenceDetails conferenceDetails) {
        LOGGER.info("NoopZoomScheduler : deleteMeeting {}", conferenceDetails);
        return true;
    }
}

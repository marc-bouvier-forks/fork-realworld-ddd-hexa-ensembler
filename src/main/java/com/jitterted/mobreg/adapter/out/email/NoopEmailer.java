package com.jitterted.mobreg.adapter.out.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

// TODO : use conditional configuration to enable / disable it
@Component
public class NoopEmailer implements Emailer {
    private static final Logger LOGGER = LoggerFactory.getLogger(NoopEmailer.class);

    @Override
    public void send(EmailToSend emailToSend) {
        LOGGER.info("NoopEmailer : send {}", emailToSend);
    }
}

package com.ueh.my.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class EventTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static Event getEventSample1() {
        return new Event()
            .id(1L)
            .eventName("eventName1")
            .eventDesc("eventDesc1")
            .eventOrg("eventOrg1")
            .eventLocation("eventLocation1")
            .enteredBy("enteredBy1")
            .modifiedBy("modifiedBy1")
            .eventStatus("eventStatus1");
    }

    public static Event getEventSample2() {
        return new Event()
            .id(2L)
            .eventName("eventName2")
            .eventDesc("eventDesc2")
            .eventOrg("eventOrg2")
            .eventLocation("eventLocation2")
            .enteredBy("enteredBy2")
            .modifiedBy("modifiedBy2")
            .eventStatus("eventStatus2");
    }

    public static Event getEventRandomSampleGenerator() {
        return new Event()
            .id(longCount.incrementAndGet())
            .eventName(UUID.randomUUID().toString())
            .eventDesc(UUID.randomUUID().toString())
            .eventOrg(UUID.randomUUID().toString())
            .eventLocation(UUID.randomUUID().toString())
            .enteredBy(UUID.randomUUID().toString())
            .modifiedBy(UUID.randomUUID().toString())
            .eventStatus(UUID.randomUUID().toString());
    }
}

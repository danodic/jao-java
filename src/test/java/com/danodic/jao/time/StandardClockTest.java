package com.danodic.jao.time;

import java.time.Instant;

import org.junit.Test;

public class StandardClockTest {
    
    @Test
    public void testNow() {
        StandardClock clock = new StandardClock();
        long before = Instant.now().toEpochMilli();
        long now = clock.now();
        long after = Instant.now().toEpochMilli();
        assert before <= now && after >= now;
    }

}
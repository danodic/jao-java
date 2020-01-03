package com.danodic.jao.time;

import java.time.Instant;

import org.junit.Test;

public class StandardClockTest {
    
    @Test
    public void testNow() throws InterruptedException {
        StandardClock clock = new StandardClock();
        long before = clock.now();
        Thread.sleep(1000);

        long now = clock.now();
        Thread.sleep(1000);

        long after = clock.now();
        
        assert before == 0L;
        assert now > before;
        assert now < after;
    }

    @Test
    public void testReset() throws InterruptedException {
        StandardClock clock = new StandardClock();
        
        clock.now();
        Thread.sleep(1000);
        
        long now = clock.now();
        assert now >= 1L;
        assert clock.getLastFrameDelta() != 0L;
        assert clock.getLastFrameTime() != 0L;
        
        long before = Instant.now().toEpochMilli();
        
        clock.reset();
        now = clock.now();
        assert now == 0L;
        assert clock.getLastFrameDelta() == 0L;
        assert clock.getLastFrameTime() >= before;
    }
    
    @Test
    public void testGetLastFrameTime() throws InterruptedException {
        StandardClock clock = new StandardClock();
        long interval = Instant.now().toEpochMilli();
        clock.now();
        Thread.sleep(1000);
        interval = Instant.now().toEpochMilli() - interval;
        clock.now();
        assert clock.getLastFrameTime() >= interval;
    }
    
    @Test
    public void testGetLastFrameDelta() throws InterruptedException {
        StandardClock clock = new StandardClock();
        clock.now();
        Thread.sleep(1000);
        clock.now();
        assert clock.getLastFrameDelta() >= 1000L;
    }

}
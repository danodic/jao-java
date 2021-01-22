package com.danodic.jao.time;

import java.time.Instant;

/**
 * The standard clock. Just a simple wrapper for the Instant class.
 */
public class StandardClock implements IClock {

    private Long startTime;
    private Long lastFrameTime;
    private Long lastFrameDelta;
    private Long currentTime;

    public StandardClock() {
        startTime = null;
        lastFrameTime = 0L;
        lastFrameDelta = 0L;
    }

    @Override
    public long now() {
        if (startTime == null) {
            reset();
        } else {

            currentTime = Instant.now().toEpochMilli();
            lastFrameDelta = currentTime - lastFrameTime;
            lastFrameTime = currentTime;
            currentTime = lastFrameTime - startTime;
        }
        return currentTime;
    }

    @Override
    public void reset() {
        startTime = Instant.now().toEpochMilli();
        currentTime = 0L;
        lastFrameDelta = 0L;
        lastFrameTime = startTime;
    }

    @Override
    public long getLastFrameTime() {
        return lastFrameTime;
    }

    @Override
    public long getLastFrameDelta() {
        return lastFrameDelta;
    }

    @Override
    public IClock clone() {
        StandardClock clone = new StandardClock();
        clone.startTime = startTime;
        clone.lastFrameTime = lastFrameTime;
        clone.lastFrameDelta = lastFrameDelta;
        clone.currentTime = currentTime;
        return clone;
    }

}

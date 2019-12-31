package com.danodic.jao.support.clocks;

import com.danodic.jao.time.IClock;

/**
 * This is a clock that allows for full time control, used for unit testing the
 * timing elements of the library.
 */
public class TimeLordClock implements IClock {

    private long currentTime;
    private long delta;

    public TimeLordClock() {
        currentTime = 0L;
        delta = 0L;
    }

    @Override
    public long now() {
        return currentTime;
    }

    /**
     * Return the time between the last two frames.
     * 
     * @return The time between the last two frames.
     */
    public long getTimeDelta() {
        return delta;
    }

    /**
     * Updates the time with the given time value and updates the delta.
     */
    public long setTime(long time) {
        delta = time - currentTime;
        currentTime = time;
        return currentTime;
    }

}
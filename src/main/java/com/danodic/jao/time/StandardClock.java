package com.danodic.jao.time;

import java.time.Instant;

/**
 * The standard clock. Just a simple wrapper for the Instant class.
 */
public class StandardClock implements IClock {

    @Override
    public long now() {
        return Instant.now().toEpochMilli();
    }
    
}
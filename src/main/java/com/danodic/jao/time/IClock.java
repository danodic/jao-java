package com.danodic.jao.time;

/**
 * Defines the interface for a clock to be used by the JAO animation. It allows
 * for different kinds of clocks to be implemented to that the user can take
 * control over the time when running the animations. Please be aware that make
 * time run slower will NOT make your animation run slower unless the action
 * takes that into account. Reversing time will also not execute anything
 * backwards, unless the renderer and the actions take that into account.
 */
public interface IClock {
    
    /**
     * Returns the current time in milliseconds.
     * 
     * @return Current time in milliseconds.
     */
    public long now();
}
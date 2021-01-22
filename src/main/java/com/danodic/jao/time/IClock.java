package com.danodic.jao.time;

/**
 * Defines the interface for a clock to be used by the JAO animation. It allows
 * for different kinds of clocks to be implemented to that the user can take
 * control over the time when running the animations. Please be aware that make
 * time run slower will NOT make your animation run slower unless the action
 * takes that into account. Reversing time will also not execute anything
 * backwards, unless the renderer and the actions take that into account.
 * 
 * Important: The clock must return a value starting from 0, that is the elapsed
 * animation time.
 */
public interface IClock {

    /**
     * Returns the elapsed animation time.
     * 
     * @return Current elpsed time in milliseconds.
     */
    public long now();

    /**
     * Reset the clock, starting from zero again.
     */
    public void reset();

    /**
     * Return the absolute time in milliseconds in which the last frame has been
     * updated.
     * 
     * @return The last frame time in milliseconds.
     */
    public long getLastFrameTime();

    /**
     * The time interval between the last two frames.
     * 
     * @return The interval between the last two frames in milliseconds.
     */
    public long getLastFrameDelta();
    
    public IClock clone();
}
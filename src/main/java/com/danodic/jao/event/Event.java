package com.danodic.jao.event;

import com.danodic.jao.core.JaoLayer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * An event is a collection of EventAction items that hold an Action each. The
 * Event class manages the execution of the actions.
 */
public class Event {

    private boolean sorted;
    private boolean hasStarted;

    private List<EventAction> allEvents;
    private List<EventAction> pool;
    private List<EventAction> running;

    public Event() {
        // Initialize stuff
        allEvents = new ArrayList<>();
        pool = new ArrayList<>();
        running = new ArrayList<>();
        sorted = false;
        hasStarted = false;
    }

    /**
     * Will add a new action to the list of actions in this event.
     *
     * @param event An instance of EventAction.
     */
    public void addAction(EventAction event) {
        sorted = false;
        allEvents.add(event);
        pool.add(event);
    }

    /**
     * Will append a list of actions to the existing list of actions in this
     * event.
     *
     * @param events A list of EventAction instances.
     */
    public void addActions(List<EventAction> events) {
        events.stream().forEach(this::addAction);
    }

    /**
     * Will return the list of EventAction instances currently being executed.
     *
     * @param elapsed How much time has elapsed since the beginning of the
     * execution.
     * @return A list of EventAction instances.
     */
    public List<EventAction> getRunningItems(long elapsed) {

        hasStarted = true;

        // Clean the list of new running events
        List<EventAction> newRunning = new ArrayList<>();

        // Sort the events in case they aren't
        if (!sorted) {
            Collections.sort(pool);
            sorted = true;
        }

        // Poll items from the pool
        for (EventAction event : pool) {

            // Check if it is time to run the event
            if (event.getWhen() <= elapsed) {
                newRunning.add(event);
                continue;
            }

            // Leave the loop because none of the further items should be used
            break;
        }

        // Remove new running items from the polling list
        pool.removeAll(newRunning);

        // Add the items to run to the running array
        running.addAll(newRunning);

        // Return the running list iterator
        return running;
    }

    /**
     * Removes all actions marked as done from the list of events that are
     * currently running.
     */
    public void cleanDone() {

        List<EventAction> toRemove = new ArrayList<>();

        // Get items done
        for (EventAction event : running) {

            // Check if it is time to run the event
            if (event.isDone()) {
                toRemove.add(event);
            }
        }

        // Remove items done
        running.removeAll(toRemove);

    }

    /**
     * Will check if there are items running and if there are still items left
     * in the pool to be executed. Also, it will only return true in case it has
     * been executed at least once.
     */
    public boolean isDone() {
        return (running.isEmpty() && pool.isEmpty() && hasStarted);
    }

    /**
     * Will tell all actions wheter to loop or not.
     *
     * @param loop Wheter actions should loop or not.
     */
    public void setLoop(boolean loop) {
        for (EventAction event : allEvents) {
            event.setLoop(loop);
        }
    }

    /**
     * Will feed the execution pool with all actions to be executed and then
     * will clean up the list of events being executed. Then, it will invoke
     * reset() on each action in this event.
     */
    public void reset() {

        // Mark to be sorted again
        sorted = false;

        // Clean up the lists
        pool = new ArrayList<>();
        running = new ArrayList<>();

        // Copy events to the poll
        pool.addAll(allEvents);

        // Reset the events
        for (EventAction event : allEvents) {
            event.reset();
        }

        hasStarted = false;
    }

    /**
     * Will return the list of all EventActions that are going to run got this
     * event.
     *
     * @return A list of EventAction instances.
     */
    public List<EventAction> getAllActionEvents() {
        return allEvents;
    }

    /**
     * Generates a deep copy of an instance of the Event class.
     *
     * @param layer The parent layer of this event.
     * @return A deep copy of the caller instance.
     */
    public Event clone(JaoLayer layer) {
        Event clone = new Event();
        
        Map<EventAction, EventAction> eventMap = new HashMap<>();
        
        clone.allEvents = allEvents.stream().map(eventAction -> {
            EventAction action = eventAction.clone(layer);
            eventMap.put(eventAction, action);
            return action;
        }).collect(Collectors.toList());
        
        pool.forEach(event -> clone.pool.add(eventMap.get(event)));
        running.forEach(event -> clone.pool.add(eventMap.get(event)));

        clone.sorted = sorted;
        clone.hasStarted = hasStarted;
        
        return clone;
    }

}

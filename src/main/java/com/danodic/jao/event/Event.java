package com.danodic.jao.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Event {
	
	private boolean sorted;
	
	private List<EventAction> allEvents;
	private List<EventAction> pool;
	private List<EventAction> running;
	
	public Event() {
		// Initialize stuff
		allEvents = new ArrayList<>();
		pool = new ArrayList<>();
		running = new ArrayList<>();
		sorted = false;
	}
	
	public void addAction(EventAction event) {
		sorted = false;
		allEvents.add(event);
		pool.add(event);
	}
	
	public void addActions(List<EventAction> events) {
		events.stream().forEach(this::addAction);
	}
	
	public void sort() {
		sorted = true;
		Collections.sort(pool);
	}
	
	public List<EventAction> getRunningItems(long elapsed) {
		
		// Clean the list of new running events
		List<EventAction> toRemove = new ArrayList<>();
		List<EventAction> newRunning = new ArrayList<>();
		
		// Sort the events in case they aren't
		if(!sorted) {
			Collections.sort(pool);
			sorted = true;
		}
		
		// Poll items from the pool
		for(EventAction event : pool) {
			
			// Check if it is time to run the event
			if(event.getWhen()<=elapsed) {
				newRunning.add(event);
				continue;
			}
			
			// Leave the loop because none of the further items should be used
			break;
		}
		
		// Get items done
		for(EventAction event : running) {
			
			// Check if it is time to run the event
			if(event.isDone()) {
				toRemove.add(event);
			}
		}
		
		// Remove new running items from the polling list
		pool.removeAll(newRunning);
		
		// Add the items to run to the running array
		running.addAll(newRunning);
		
		// Remove items done
		running.removeAll(toRemove);
		
		// Return the running list iterator
		return running;
	}
	
	public boolean isDone() {
		return (running.isEmpty() && pool.isEmpty());
	}

	public void setLoop(boolean loop) {
		for(EventAction event : allEvents) {
			event.setLoop(loop);
		}
	}

	public void reset() {
		
		// Mark to be sorted again
		sorted = false;
		
		// Clean up the lists
		pool = new ArrayList<>();
		running = new ArrayList<>();
		
		// Copy events to the poll
		pool.addAll(allEvents);
		
		// Reset the events
		for(EventAction event : allEvents) {
			event.reset();
		}
	}

}

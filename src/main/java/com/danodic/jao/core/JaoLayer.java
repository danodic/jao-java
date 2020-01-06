package com.danodic.jao.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.danodic.jao.action.IInitializer;
import com.danodic.jao.event.Event;
import com.danodic.jao.event.EventAction;
import com.danodic.jao.event.InitializerEvent;
import com.danodic.jao.exceptions.JaoEventNotFoundException;
import com.danodic.jao.renderer.IRenderer;
import com.danodic.jao.time.IClock;

/**
 * The JaoLayer class will hold all events and initializers from a given layer
 * entry specified in the JSON file. The layer is initialized when it is
 * instantiated, what means that all actions listed under the "initialize" event
 * are going to be executed at constructor time. The initializer is also called
 * when updating the list of initializers at addInitializers().
 * 
 * The layer carries an instance of LayerParameters, that hold the values that
 * are manipulated by the actions inside the events. Those values are
 * initialized by the initializer actions.
 * 
 * The interaction with the events is delegated to the Event class itself, and
 * the current event can be reached out by calling getEvent().
 * 
 * Also, if an event with the name "default" is specified is added to the layer,
 * it will be set as the currentEvent in case currentEvent is null. Else, the
 * currentEvent will remain null until the event is set by calling set
 * setEvent().
 */
public class JaoLayer {

	private Jao jao;
	private IRenderer renderer;
	private Map<String, Event> events;
	private List<IInitializer> initializers;
	private Event currentEvent;
	private LayerParameters parameters;

	private static final String DEFAULT_EVENT_NAME = "default";

	public JaoLayer(Jao jao, IRenderer rendererImpl) {
		this.jao = jao;

		events = new HashMap<>();
		initializers = new ArrayList<>();
		parameters = new LayerParameters();
		renderer = rendererImpl;

		// Current event always start as null. When selecting the action to run
		// the library will try to engage the specific action.
		currentEvent = null;

		// Initialize the layer prior to using it.
		initialize();
	}

	/**
	 * Will execute the init events for this layer. It is invoked automatically when
	 * the class is instantiated.
	 */
	public void initialize() {
		initializers.forEach(event -> event.run(this));
	}

	/**
	 * Will restor the layer to its original state, along with all events and
	 * initializers.
	 */
	public void reset() {
		initialize();
		events.forEach((key, value) -> value.reset());
	}

	/**
	 * Will return the layer parameters. The parameters are manipulated by the
	 * initializers and the actions.
	 * 
	 * @return The current layer parameters.
	 */
	public LayerParameters getParameters() {
		return parameters;
	}

	/**
	 * Will return if a given event exists in this layer or not.
	 * 
	 * @param eventName Name of the event to be checked for.
	 * @return Wheter the event exists or not.
	 */
	public boolean hasEvent(String eventName) {
		return events.containsKey(eventName);
	}

	/**
	 * Will return the currently engaged event for this layer.
	 * 
	 * @return A instance of Event in case there is an event engaged, else it will
	 *         return null.
	 */
	public Event getEvent() {
		return currentEvent;
	}

	/**
	 * Will return the instance of an event with the given name.
	 * 
	 * @param eventName Name of the event to be retrieved.
	 * @return An instance of Event.
	 * @throws JaoEventNotFoundException In case the event does not exist in the
	 *                                   layer.
	 */
	public Event getEvent(String eventName) throws JaoEventNotFoundException {
		if(!events.containsKey(eventName))
			throw new JaoEventNotFoundException(eventName);
		return events.get(eventName);
	}

	/**
	 * Will set the current event to be executed by this layer. In case the event
	 * cannot be found in the list of events, it will remain as null and this layer
	 * will not do anything. That is also the behavior when a layer is empty: it
	 * won`t do anything.
	 * 
	 * @param eventName Name of the event to be executed.
	 */
	public Event setEvent(String eventName) {
		if (events.containsKey(eventName)) {
			currentEvent = events.get(eventName);
		} else {
			currentEvent = null;
		}
		return currentEvent;
	}

	/**
	 * Add the initializer actions to the layer. Those are handled as a single
	 * initializer event.
	 * 
	 * @param initializer The event containing the initializer actions.
	 */
	public void addInitializers(InitializerEvent initializer) {
		this.initializers.addAll(initializer);
		initialize();
	}

	/**
	 * Add a list of events to the layer, along with their names.
	 * 
	 * @param events A Map containing the events to be added to the layer.
	 */
	public void addEvents(Map<String, Event> events) {
		this.events.putAll(events);
		if (this.events.containsKey(DEFAULT_EVENT_NAME))
			this.currentEvent = this.events.get(DEFAULT_EVENT_NAME);
	}

	/**
	 * Will set the current event to be executed by this layer, and then it will
	 * reset it.
	 * 
	 * @param eventName Name of the event to be executed and reset.
	 */
	public void setEventAndReset(String eventName) {
		setEvent(eventName);
		if (currentEvent != null)
			currentEvent.reset();
	}

	/**
	 * Will return the renderer being used by the layer.
	 * 
	 * @return The instance of the renderer being used by the layer.
	 */
	public IRenderer getRenderer() {
		return renderer;
	}

	/**
	 * Will update the renderer used by the layer.
	 */
	public void setRenderer(IRenderer renderer) {
		this.renderer = renderer;
	}

	/**
	 * Will render the current laer at the current event in the screen. Will not do
	 * anything in case the current event is null.
	 * 
	 * @param elapsed How much has elapsed since the beginning of the current event
	 *                execution.
	 */
	public void render(long elapsed) {
		for (EventAction action : currentEvent.getRunningItems(elapsed)) {
			action.run();
		}
		renderer.render(this);
		currentEvent.cleanDone();
	}	

	/**
	 * Add a single event in the list of events for this layer.
	 * 
	 * @param eventName Name of the event.
	 * @param event     The instance of the event.
	 */
	public void addEvent(String eventName, Event event) {
		events.put(eventName, event);
		if(eventName.equals(DEFAULT_EVENT_NAME))
			currentEvent = event;
	}

	/**
	 * Will tell if the current event is done.
	 * 
	 * @return If the current event is done.
	 */
	public boolean isDone() {
		if (currentEvent != null)
			return currentEvent.isDone();
		return true;
	}

	/**
	 * Get the current scale factor, so that the renderer can resize elements to fit
	 * a reference resolution if necessary
	 * 
	 * @return A float that tells what is the scale factor.
	 */
	public float getScaleFactor() {
		return jao.getScaleFactor();
	}

	/**
	 * Get how much time has elapsed since the beginning of the current event.
	 * 
	 * @return A long with the amount of miliseconds elapsed.
	 */
	public Long getElapsed() {
		return jao.getElapsed();
	}

	/**
	 * Will return how much time has elapsed between the last two frames.
	 * 
	 * @return The last frame time measured.
	 */
	public Long getLastFrameTime() {
		return jao.getLastFrameTime();
	}

	/**
	 * Will define if this layer is expected to loop or not.
	 * 
	 * @param loop Whether the layer should loop or not.
	 */
	public void setLoop(boolean loop) {
		if (currentEvent != null)
			currentEvent.setLoop(loop);
	}

	/**
	 * Will add an initializer to the list of initializers in this layer. Also, will
	 * re-execute all initializers. It is recommended to add them all at once with
	 * addInitializers() for better performance.
	 * 
	 * @param initializer An instance of IInitializer.
	 */
	public void addInitializer(IInitializer initializer) {
		initializers.add(initializer);
		initialize();
	}

	/**
	 * Returns the instance of clock currently being used by the main animation.
	 * 
	 * @return An instance of IClock being used by the main Jao instance.
	 */
	public IClock getClock() {
		return jao.getClock();
	}
}

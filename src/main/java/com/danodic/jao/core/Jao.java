package com.danodic.jao.core;

import java.util.ArrayList;
import java.util.List;

import com.danodic.jao.time.IClock;
import com.danodic.jao.time.StandardClock;

/**
 * This is the main class for the Jao library. The instance of this class is
 * provided by parsing a JSON file using the JaoParser class.
 * 
 * This class allows to interface with the events contained in the JSON file and
 * execute them through a renderer.
 * 
 * The renderer is the actual piece of code that materializes the contents of
 * the .jao file in the screen. What the Jao class does is to orchestrate the
 * execution of all events on all layers and stopping, reseting, looping and
 * etc.
 * 
 * In case the JSON file has a "default" event defined for at least one of the
 * layers, you can just call render() right after getting the instance from the
 * parser. If there is no "default" event defined, you'll have to call
 * setEvent() to define the first event to be executed.
 * 
 * The libraries mentioned in the JSON file are scanned from the classpath when
 * the parsing starts, and they have to be defined by the user of the class.
 * There should be a default library with sample entries as a separate project
 * that can be added as another dependency in the project, but nothing avoids
 * the programmer to implement new libraries and mix and match them with the
 * existing ones. Take a look at the IAction and IInitializer interfaces to
 * understand how it is done.
 */
public class Jao {

	private List<JaoLayer> layers;
	private Long elapsed;
	private float scaleFactor;

	private IClock clock;

	public Jao() {
		clock = new StandardClock();
		layers = new ArrayList<>();
		elapsed = 0L;
		scaleFactor = 1.0f;
	}

	/**
	 * Adds a single layer to the list of layers. Layers are rendered in the screen
	 * in the order they are added.
	 * 
	 * @param layer A instance of JaoLayer.
	 */
	public void addLayer(JaoLayer layer) {
		this.layers.add(layer);
	}

	/**
	 * Adds a list of layers on top of the existing list of layers. Layers are
	 * rendered in the screen in the order they are added, the newly added ones will
	 * be rendered after the existing ones in the order they are placed in the list.
	 * 
	 * @param layers A list of JaoLayer.
	 */
	public void addLayers(List<JaoLayer> layers) {
		this.layers.addAll(layers);
	}

	/**
	 * Will render each of the layers.
	 */
	public void render() {
		updateElapsed();
		for (JaoLayer layer : layers)
			if (layer.getEvent() != null)
				layer.render(elapsed);
	}

	/**
	 * Will call a reset for each layer and will also re-trigger the initializers
	 * for the current event.
	 */
	public void reset() {
		for (JaoLayer layer : layers)
			layer.reset();
		clock.reset();
		elapsed = 0L;
	}

	/**
	 * Deactvates the loop for all layers in the current event so that the animation
	 * can be marked as done as soon as the layers are done.
	 * 
	 * It will have no effect in case you have an action in the current event that
	 * loops forever and ignores the loop flag
	 */
	public void prepareToFinish() {
		for (JaoLayer layer : layers)
			layer.setLoop(false);
	}

	/**
	 * Updates the elapsed time since the start of the current event.
	 */
	private void updateElapsed() {
		elapsed = clock.now();
	}

	/**
	 * Returns how much time has run since the last render() call.
	 * 
	 * @return The elapsed time.
	 */
	public long getElapsed() {
		return elapsed;
	}

	/**
	 * Will tell if the current event is done or not. It will never return true in
	 * case any of the actions being executed in the current event ignores the loop
	 * flag and never return done=true.
	 * 
	 * @return If all layers in the current event are done or not.
	 */
	public boolean isDone() {
		for (JaoLayer layer : layers) {
			if (!layer.isDone())
				return false;
		}
		return true;
	}

	/**
	 * Returns the scale factor being currently used by the renderer.
	 * 
	 * @return The current scale factor as float.
	 */
	public float getScaleFactor() {
		return scaleFactor;
	}

	/**
	 * Sets the scale factor that is used by the renderer to scale elements in the
	 * screen.
	 * 
	 * @param scaleFactor A float value. Use 1f for real-size.
	 */
	public void setScaleFactor(float scaleFactor) {
		this.scaleFactor = scaleFactor;
	}

	/**
	 * Return the list of layers currently loaded in the Jao file.
	 * 
	 * @returnA list of JaoLayer.
	 */
	public List<JaoLayer> getLayers() {
		return layers;
	}

	/**
	 * Get the amount of time elapsed between the last two frames.
	 * 
	 * @return The frame time in miliseconds.
	 */
	public Long getLastFrameDelta() {
		return clock.getLastFrameDelta();
	}

	/**
	 * Get the time in milliseconds in which the last frame was triggered.
	 */
	public Long getLastFrameTime() {
		return clock.getLastFrameTime();
	}

	/**
	 * Sets the current event to be executed. Switching events will cause the
	 * startTime to be reset and the elapsed to be calcuated starting from zero
	 * again.
	 * 
	 * Switching to an invalid event name will make the layer to be ignored until a
	 * valid event name is provided. That allows for layers to be rendered
	 * selectively by setting different event names for specific layers.
	 */
	public void setEvent(String eventName) {
		layers.forEach(layer -> layer.setEvent(eventName));
	}

	/**
	 * Will return the current clock in use by the Jao animation.
	 * 
	 * @return The instance of the clock being used at the moment.
	 */
	public IClock getClock() {
		return clock;
	}

	/**
	 * Set the clock to be used by the Jao animation. Setting a custom clock allows
	 * the user to manipulate time as necessary.
	 * 
	 * @param clock An instance of IClock.
	 */
	public void setClock(IClock clock) {
		this.clock = clock;
	}

}

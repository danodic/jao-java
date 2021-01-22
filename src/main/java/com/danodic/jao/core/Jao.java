package com.danodic.jao.core;

import com.danodic.jao.exceptions.CannotInstantiateJaoRenderer;
import com.danodic.jao.exceptions.JaoCloneException;
import java.util.ArrayList;
import java.util.List;

import com.danodic.jao.extractor.IExtractor;
import com.danodic.jao.time.IClock;
import com.danodic.jao.time.StandardClock;
import java.util.stream.Collectors;

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

    private IExtractor extractor;

    private IClock clock;

    public Jao() {
        clock = new StandardClock();
        layers = new ArrayList<>();
        elapsed = 0L;
        scaleFactor = 1.0f;
        extractor = null;
    }

    /**
     * Adds a single layer to the list of layers. Layers are rendered in the
     * screen in the order they are added.
     *
     * @param layer A instance of JaoLayer.
     */
    public void addLayer(JaoLayer layer) {
        this.layers.add(layer);
    }

    /**
     * Adds a list of layers on top of the existing list of layers. Layers are
     * rendered in the screen in the order they are added, the newly added ones
     * will be rendered after the existing ones in the order they are placed in
     * the list.
     *
     * @param layers A list of JaoLayer.
     */
    public void addLayers(List<JaoLayer> layers) {
        this.layers.addAll(layers);
    }

    /**
     * Will render each of the layers allowing to pass arguments to the
     * renderer.
     */
    public void render(Object... args) {
        updateElapsed();
        for (JaoLayer layer : layers) {
            if (layer.getEvent() != null) {
                layer.render(elapsed, args);
            }
        }
    }

    /**
     * Will call a reset for each layer and will also re-trigger the
     * initializers for the current event.
     */
    public void reset() {
        for (JaoLayer layer : layers) {
            layer.reset();
        }
        clock.reset();
        elapsed = 0L;
    }

    /**
     * Deactvates the loop for all layers in the current event so that the
     * animation can be marked as done as soon as the layers are done.
     *
     * It will have no effect in case you have an action in the current event
     * that loops forever and ignores the loop flag
     */
    public void prepareToFinish() {
        for (JaoLayer layer : layers) {
            layer.setLoop(false);
        }
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
     * Will tell if the current event is done or not. It will never return true
     * in case any of the actions being executed in the current event ignores
     * the loop flag and never return done=true.
     *
     * @return If all layers in the current event are done or not.
     */
    public boolean isDone() {
        for (JaoLayer layer : layers) {
            if (!layer.isDone()) {
                return false;
            }
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
     * Sets the scale factor that is used by the renderer to scale elements in
     * the screen.
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
     * Switching to an invalid event name will make the layer to be ignored
     * until a valid event name is provided. That allows for layers to be
     * rendered selectively by setting different event names for specific
     * layers.
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
     * Set the clock to be used by the Jao animation. Setting a custom clock
     * allows the user to manipulate time as necessary.
     *
     * @param clock An instance of IClock.
     */
    public void setClock(IClock clock) {
        this.clock = clock;
    }

    /**
     * The extractor provides the data that the layers will have to access.
     *
     * @param extractor An instance of extractor can be obtained using the
     * ExtractorFactory class.
     */
    public void setExtractor(IExtractor extractor) {
        this.extractor = extractor;
    }

    /**
     * Returns the extractor currently assigned to this Jao instance.
     *
     * @return An instance of IExtractor.
     */
    public IExtractor getExtractor() {
        return extractor;
    }

    /**
     * Makes a deep copy of an existing Jao instance without loading all data
     * again or repeating the json load process. Used in cases where you want
     * many copies of a single Jao animation with the same data but different
     * states.
     *
     * @return A deep copy of a Jao instance.
     *
     */
    @Override
    public Jao clone() {
        Jao cloneJao = new Jao();
        cloneJao.clock = clock.clone();
        cloneJao.extractor = extractor;
        cloneJao.layers = layers.stream().map(layer -> {
            try {
                return layer.clone(cloneJao);
            } catch (CannotInstantiateJaoRenderer e) {
                throw new JaoCloneException(e);
            }
        }).collect(Collectors.toList());
        cloneJao.elapsed = elapsed;
        cloneJao.scaleFactor = scaleFactor;
        return cloneJao;
    }

}

package com.danodic.jao.parser;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.danodic.jao.action.ActionFactory;
import com.danodic.jao.action.IAction;
import com.danodic.jao.action.IInitializer;
import com.danodic.jao.core.Jao;
import com.danodic.jao.core.JaoLayer;
import com.danodic.jao.event.Event;
import com.danodic.jao.event.EventAction;
import com.danodic.jao.event.InitializerEvent;
import com.danodic.jao.exceptions.CannotFindJaoActionException;
import com.danodic.jao.exceptions.CannotFindJaoInitializerException;
import com.danodic.jao.exceptions.CannotFindJaoLibraryException;
import com.danodic.jao.exceptions.CannotInstantiateJaoActionException;
import com.danodic.jao.exceptions.CannotInstantiateJaoRenderer;
import com.danodic.jao.model.ActionModel;
import com.danodic.jao.model.EventModel;
import com.danodic.jao.model.JaoModel;
import com.danodic.jao.model.LayerModel;
import com.danodic.jao.parser.expressions.TimeExpressionParser;
import com.danodic.jao.renderer.IRenderer;
import com.google.gson.Gson;

/**
 * This class is the main parser, that will extract the information from the
 * json file and build the Jao instance with the information contained in the
 * json.
 * 
 * @author danodic
 */
public class JaoParser {

	/**
	 * Will parse the json provided as a string and will instantiate a Jao object
	 * and populate it with the data obtained from the JSON. It needs a renderer
	 * class reference so that it knows which renderer to use when instantiating the
	 * IAction intances in the deeper, darkest levels of this dunge.. code.
	 * 
	 * @param json     The contents of the json to be parsed.
	 * @param renderer A reference to the desired renderer class.
	 * @return An instance of Jao.
	 * @throws CannotInstantiateJaoActionException In case one of the actions used
	 *                                              couldn`t be instantiated.
	 * @throws CannotInstantiateJaoRenderer         In case the renderer couldn`t be
	 *                                              instantiated.
	 * @throws CannotFindJaoInitializerException
	 * @throws CannotFindJaoLibraryException
	 * @throws CannotFindJaoActionException
	 */
	public static Jao parseJson(String json, Class<? extends IRenderer> renderer)
			throws CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer, CannotFindJaoLibraryException,
			CannotFindJaoInitializerException, CannotFindJaoActionException {

		// Get the Jao Model
		JaoModel model = deserializeJson(json);

		// Initialize the action factory to find all the entries in the classpath
		ActionFactory.initializeFactory();

		// Parse the model and return the instance
		return parseJaoModel(model, renderer);
	}

	/**
	 * Transform the JSON string into a JaoModel instance.
	 * 
	 * @param json A JSON file following the JAO schema.
	 * @return An instance of JaoModel with all the json data fed into it.
	 */
	private static JaoModel deserializeJson(String json) {
		return new Gson().fromJson(json, JaoModel.class);
	}

	/**
	 * Will parse a JaoModel and populate a Jao instance with the information from
	 * the JSON file.
	 * 
	 * @param model    The model extracted from the JSON file.
	 * @param renderer The renderer class that will be used.
	 * @return An instance of Jao with all the information needed in it.
	 * @throws CannotInstantiateJaoActionException In case an action couldn`t be
	 *                                              instantiated.
	 * @throws CannotInstantiateJaoRenderer         In case the renderer couldn`t be
	 *                                              instantiated.
	 * @throws CannotFindJaoInitializerException
	 * @throws CannotFindJaoLibraryException
	 * @throws CannotFindJaoActionException
	 */
	private static Jao parseJaoModel(JaoModel model, Class<? extends IRenderer> renderer)
			throws CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer, CannotFindJaoLibraryException,
			CannotFindJaoInitializerException, CannotFindJaoActionException {

		// Parse the layers in the model extracted from the JSON
		Jao jao = new Jao();
		List<JaoLayer> layers = parseLayers(jao, model.getLayers(), renderer);

		// Populate the main Jao information and return it
		jao.addLayers(layers);
		return jao;
	}

	/**
	 * Will parse each individual layer and return a list of layers to be added to
	 * the main Jao instance.
	 * 
	 * @param layers   List of layer models parsed from the JSON file.
	 * @param renderer The class of the renderer to be used for this layer.
	 * @return A list of JaoLayer instances.
	 * @throws CannotInstantiateJaoActionException In case an action couldn`t be
	 *                                              initialized.
	 * @throws CannotInstantiateJaoRenderer         In case the rendered couldn`t be
	 *                                              initialized.
	 * @throws CannotFindJaoInitializerException
	 * @throws CannotFindJaoLibraryException
	 * @throws CannotFindJaoActionException
	 */
	private static List<JaoLayer> parseLayers(Jao jao, List<LayerModel> layers, Class<? extends IRenderer> renderer)
			throws CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer, CannotFindJaoLibraryException,
			CannotFindJaoInitializerException, CannotFindJaoActionException {
		List<JaoLayer> layerInstances = new ArrayList<JaoLayer>();
		for (LayerModel layer : layers)
			layerInstances.add(parseLayer(jao, layer, renderer));
		return layerInstances;
	}

	/**
	 * Parse the layer models, add both the events and the initializers to the
	 * layer. W
	 * 
	 * @param layerModel
	 * @param renderer
	 * @return
	 * @throws CannotInstantiateJaoActionException
	 * @throws CannotInstantiateJaoRenderer
	 * @throws CannotFindJaoInitializerException
	 * @throws CannotFindJaoLibraryException
	 * @throws CannotFindJaoActionException
	 */
	private static JaoLayer parseLayer(Jao jao, LayerModel layerModel, Class<? extends IRenderer> renderer)
			throws CannotInstantiateJaoActionException, CannotInstantiateJaoRenderer, CannotFindJaoLibraryException,
			CannotFindJaoInitializerException, CannotFindJaoActionException {
		// Create the renderer and set the data type into it
		IRenderer rendererImpl = getRendererInstance(renderer);
		rendererImpl.setDataType(layerModel.getDataType());

		// Instantiate the JAO layer and add the initializers and events to it
		JaoLayer layer = new JaoLayer(jao, rendererImpl);
		layer.addInitializers(parseInitializers(layer, layerModel.getEvents()));
		layer.addEvents(parseEvents(layer, layerModel.getEvents()));

		return layer;
	}

	/**
	 * Will instantiate the renderer class provided. That renderer must be able to
	 * be instantiated using an argument-free constructor.
	 * 
	 * @param renderer A class corresponding to the renderer to be used.
	 * @return An instance of the renderer provided.
	 * @throws CannotInstantiateJaoRenderer
	 */
	private static IRenderer getRendererInstance(Class<? extends IRenderer> renderer)
			throws CannotInstantiateJaoRenderer {
		IRenderer rendererImpl = null;
		for (Constructor<?> constructor : renderer.getConstructors()) {
			if (constructor.getParameterCount() == 0) {
				try {
					rendererImpl = (IRenderer) constructor.newInstance();
					break;
				} catch (Exception e) {
					throw new CannotInstantiateJaoRenderer(e);
				}
			}
		}
		return rendererImpl;
	}

	/**
	 * Will parse all events aside from the "initialize" event.
	 * 
	 * @param jaoLayer    The current animation layer, used to instantiate the
	 *                    initializers.
	 * @param eventModels The event model data extracted from the JSON file.
	 * @return A map containing the events listed in the JSON alongside with their
	 *         names.
	 * @throws CannotInstantiateJaoActionException In case any of the actions
	 *                                              inside the events couldn`t be
	 *                                              instantiated.
	 * @throws CannotFindJaoInitializerException
	 * @throws CannotFindJaoLibraryException
	 * @throws CannotFindJaoActionException
	 */
	private static Map<String, Event> parseEvents(JaoLayer jaoLayer, List<EventModel> eventModels)
			throws CannotInstantiateJaoActionException, CannotFindJaoLibraryException, CannotFindJaoActionException {
		Map<String, Event> events = new HashMap<String, Event>();
		for (EventModel event : eventModels) {
			if (event.getName().equalsIgnoreCase("initialize"))
				continue;
			events.put(event.getName(), parseEvent(jaoLayer, event));
		}
		return events;
	}

	/**
	 * Will parse all actions inside the "initialize" event.
	 * 
	 * @param jaoLayer    The current animation layer, used to instantiate the
	 *                    initializers.
	 * @param eventModels The model data extracted from a JSON.
	 * @return an instance of InitializerEvent containing instances of all
	 *         initializers ready to run.
	 * @throws CannotInstantiateJaoActionException In case any of the initializers
	 *                                              couldn`t be instantiated.
	 * @throws CannotFindJaoInitializerException
	 * @throws CannotFindJaoLibraryException
	 */
	private static InitializerEvent parseInitializers(JaoLayer jaoLayer, List<EventModel> eventModels)
			throws CannotInstantiateJaoActionException, CannotFindJaoLibraryException,
			CannotFindJaoInitializerException {
		InitializerEvent init = new InitializerEvent();
		for (EventModel event : eventModels) {
			if (event.getName().equalsIgnoreCase("initialize")) {
				for (ActionModel action : event.getActions())
					init.add(parseInitializer(jaoLayer, action));
				break;
			}
		}
		return init;
	}

	/**
	 * Will generate a new instance of an Event based on the data contained inside
	 * an EventModel instance.
	 * 
	 * @param jaoLayer   The current animation layer.
	 * @param eventModel The model of the event.
	 * @return An instance of Event with the data fed by EventModel.
	 * @throws CannotInstantiateJaoActionException In case the Action cannot be
	 *                                              instantiated.
	 * @throws CannotFindJaoInitializerException
	 * @throws CannotFindJaoLibraryException
	 * @throws CannotFindJaoActionException
	 */
	private static Event parseEvent(JaoLayer jaoLayer, EventModel eventModel)
			throws CannotInstantiateJaoActionException, CannotFindJaoLibraryException, CannotFindJaoActionException {
		Event event = new Event();
		event.addActions(parseActions(jaoLayer, event, eventModel.getActions()));
		return event;
	}

	/**
	 * Will parse the action contained in an event and add them to this event.
	 * 
	 * @param jaoLayer The current animation layer, used to initialize the action
	 *                 implementations.
	 * @param event    The event to where the actions are going to be added to.
	 * @param actions  The list of action models to instantiate the implementations
	 *                 from.
	 * @throws CannotInstantiateJaoActionException In case the action could not be
	 *                                              implemented by some reason.
	 * @throws CannotFindJaoInitializerException
	 * @throws CannotFindJaoLibraryException
	 * @throws CannotFindJaoActionException
	 */
	private static List<EventAction> parseActions(JaoLayer jaoLayer, Event event, List<ActionModel> actionModels)
			throws CannotInstantiateJaoActionException, CannotFindJaoLibraryException, CannotFindJaoActionException {
		List<EventAction> actions = new ArrayList<EventAction>();
		for (ActionModel action : actionModels) {
			actions.add(parseAction(jaoLayer, action));
		}
		return actions;
	}

	/**
	 * Will parse the data in the ActionModel and instantiate the proper initializer
	 * for it.
	 * 
	 * @param jaoLayer The current animation layer.
	 * @param action   The initializer data extracted from the JSON.
	 * @return An instance of IInitializer setup the with the model data.
	 * @throws CannotInstantiateJaoActionException In case the initializer couldn`t
	 *                                              be instantiated.
	 * @throws CannotFindJaoInitializerException
	 * @throws CannotFindJaoLibraryException
	 */
	private static IInitializer parseInitializer(JaoLayer jaoLayer, ActionModel action)
			throws CannotInstantiateJaoActionException, CannotFindJaoLibraryException,
			CannotFindJaoInitializerException {
		IInitializer initImpl = ActionFactory.getInitializer(action.getLibrary(), action.getName(), action);
		initImpl.loadModel(action);
		return initImpl;
	}

	/**
	 * Return an instance of EventAction, initialized with the implementation of the
	 * action to be executed and fed with the data model.
	 * 
	 * @param jaoLayer The current animation layer.
	 * @param action   The action model data extracted from the JSON.
	 * @return An instance of EventAction.
	 * @throws CannotInstantiateJaoActionException In case the Action cannot be
	 *                                              instantiated.
	 * @throws CannotFindJaoInitializerException
	 * @throws CannotFindJaoLibraryException
	 * @throws CannotFindJaoActionException
	 */
	private static EventAction parseAction(JaoLayer jaoLayer, ActionModel action)
			throws CannotInstantiateJaoActionException, CannotFindJaoLibraryException, CannotFindJaoActionException {

		// Find the action implementation in the factory that is going to be
		// used by this action. Also feed the model so that it initializes the
		// data.
		IAction actionImpl = ActionFactory.getAction(action.getLibrary(), action.getName());
		actionImpl.loadModel(action);

		// Parse the when expression and get the actual when value in long
		Long when = TimeExpressionParser.parseExpression(action.getWhen());

		return new EventAction(jaoLayer, actionImpl, when);

	}
}

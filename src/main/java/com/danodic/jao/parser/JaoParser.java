package com.danodic.jao.parser;

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
import com.danodic.jao.exceptions.CannotInstantiateJaoActiontException;
import com.danodic.jao.model.ActionModel;
import com.danodic.jao.model.DataTypeModel;
import com.danodic.jao.model.EventModel;
import com.danodic.jao.model.JaoModel;
import com.danodic.jao.model.LayerModel;
import com.danodic.jao.parser.expressions.TimeExpressionParser;
import com.google.gson.Gson;

/**
 * This class parses the contents of a json.jao file and return an instance of
 * the Jao class.
 * 
 * @author danodic
 *
 */
public class JaoParser {

	public static Animation parseJson(String json) {

		// Get the Jao Model
		JaoModel model = deserializeJson(json);

		// Initialize the action factory to find all the entries in the classpath
		ActionFactory.initializeFactory();

		// Start parsing the model
		parseJaoModel(model);

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

	private static void parseJaoModel(JaoModel model) throws CannotInstantiateJaoActiontException {
		parseLayers(model.getLayers());
	}

	private static void parseLayers(List<LayerModel> layers) throws CannotInstantiateJaoActiontException {
		for (LayerModel layer : layers)
			parseLayer(layer);
	}

	private static JaoLayer parseLayer(LayerModel layerModel) throws CannotInstantiateJaoActiontException {
		JaoLayer layer = new JaoLayer();

		// parseDataType(layerModel.getDataType());
		layer.addInitializers(parseInitializers(layer, layerModel.getEvents()));
		layer.addEvents(parseEvents(layer, layerModel.getEvents()));

		return layer;
	}

	/**
	 * Will parse all events aside from the "initialize" event.
	 * 
	 * @param jaoLayer    The current animation layer, used to instantiate the
	 *                    initializers.
	 * @param eventModels The event model data extracted from the JSON file.
	 * @return A map containing the events listed in the JSON alongside with their
	 *         names.
	 * @throws CannotInstantiateJaoActiontException In case any of the actions
	 *                                              inside the events couldn`t be
	 *                                              instantiated.
	 */
	private static Map<String, Event> parseEvents(JaoLayer jaoLayer, List<EventModel> eventModels)
			throws CannotInstantiateJaoActiontException {
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
	 * @throws CannotInstantiateJaoActiontException In case any of the initializers
	 *                                              couldn`t be instantiated.
	 */
	private static InitializerEvent parseInitializers(JaoLayer jaoLayer, List<EventModel> eventModels)
			throws CannotInstantiateJaoActiontException {
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
	 * @throws CannotInstantiateJaoActiontException In case the Action cannot be
	 *                                              instantiated.
	 */
	private static Event parseEvent(JaoLayer jaoLayer, EventModel eventModel)
			throws CannotInstantiateJaoActiontException {
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
	 * @throws CannotInstantiateJaoActiontException In case the action could not be
	 *                                              implemented by some reason.
	 */
	private static List<EventAction> parseActions(JaoLayer jaoLayer, Event event, List<ActionModel> actionModels)
			throws CannotInstantiateJaoActiontException {
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
	 * @throws CannotInstantiateJaoActiontException In case the initializer couldn`t
	 *                                              be instantiated.
	 */
	private static IInitializer parseInitializer(JaoLayer jaoLayer, ActionModel action)
			throws CannotInstantiateJaoActiontException {
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
	 * @throws CannotInstantiateJaoActiontException In case the Action cannot be
	 *                                              instantiated.
	 */
	private static EventAction parseAction(JaoLayer jaoLayer, ActionModel action)
			throws CannotInstantiateJaoActiontException {

		// Find the action implementation in the factory that is going to be
		// used by this action. Also feed the model so that it initializes the
		// data.
		IAction actionImpl = ActionFactory.getAction(action.getLibrary(), action.getName());
		actionImpl.loadModel(action);

		// Parse the when expression and get the actual when value in long
		Long when = TimeExpressionParser.parseExpression(action.getWhen());

		return new EventAction(jaoLayer, actionImpl, when);

	}

	private static void parseDataType(DataTypeModel dataType) {

	}
}

package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A singleton EventBus for managing and dispatching events to registered
 * observers.
 * This class handles the registration of observers for specific event types and
 * broadcasts messages to all registered observers when an event is posted.
 */
public class EventBus {
    private static EventBus instance;
    private final Map<EventType, List<org.example.IObserver>> observersMap;

    /**
     * Private constructor to create a new EventBus and initialize an observers map
     * for all event types.
     */
    private EventBus() {
        observersMap = new HashMap<>();
        for (EventType eventType : EventType.values()) {
            observersMap.put(eventType, new ArrayList<>());
        }
    }

    /**
     * Provides the global access point for the EventBus instance and creates it
     * if it does not already exist.
     *
     * @return the single, shared instance of the EventBus
     */
    public static synchronized EventBus getInstance() {
        if (instance == null) {
            instance = new EventBus();
        }
        return instance;
    }

    /**
     * Attaches an observer to a specific event type. Observers attached this way
     * will be notified when the event type they are registered for is posted to the
     * EventBus.
     *
     * @param observer  the observer to register
     * @param eventType the type of event to observe
     */
    public void attach(IObserver observer, EventType eventType) {
        List<IObserver> observers = observersMap.get(eventType);
        if (observers != null) {
            observers.add(observer);
        }
    }

    /**
     * Posts a message to the EventBus, triggering an update to all observers
     * registered for the specified event type.
     *
     * @param eventType        the type of event being posted
     * @param eventDescription the description or data associated with the event
     */
    public void postMessage(EventType eventType, String eventDescription) {
        List<IObserver> observers = observersMap.get(eventType);
        if (observers != null) {
            for (IObserver observer : observers) {
                observer.update(eventDescription);
            }
        }
    }
}

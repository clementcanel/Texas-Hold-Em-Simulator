package org.example;

/**
 * Defines the contract for observer objects that receive notifications from
 * observable objects. This interface is part of the Observer pattern, which
 * allows objects to receive updates when changes occur in another object.
 */
public interface IObserver {
    /**
     * Called by the Observable to notify this Observer about an event or change.
     *
     * @param eventDescription a description of the event or change
     */
    void update(String eventDescription);
}
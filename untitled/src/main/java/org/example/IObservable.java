package org.example;

/**
 * Defines the contract for classes that can be observed. This interface is part
 * of the Observer pattern, where an observable object notifies observers about
 * changes in its state.
 */
public interface IObservable {
    /**
     * Registers an observer to be notified of changes.
     *
     * @param observer the observer that wants to be notified
     */
    void registerObserver(IObserver observer);

    /**
     * Removes an observer from the notification list.
     *
     * @param observer the observer to be removed
     */
    void removeObserver(IObserver observer);
}
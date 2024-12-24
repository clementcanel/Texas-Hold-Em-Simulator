package org.example;

import java.util.List;

/**
 * The {@code audibleObserver} class implements the {@link IObserver} interface
 * and
 * provides mechanisms to react to specific events by "saying" them out loud
 * after a delay.
 */
public class audibleObserver implements IObserver {

    private final IObservable game;
    private List<EventType> interestingEvents;
    private final Integer delayInSeconds;

    /**
     * Constructs a new {@code audibleObserver} with specified game observable,
     * events of interest, and delay.
     *
     * @param game              the observable game to observe
     * @param interestingEvents the types of events this observer is interested in
     * @param delayInSeconds    the delay in seconds before saying the event
     *                          description
     */
    public audibleObserver(IObservable game, List<EventType> interestingEvents, Integer delayInSeconds) {
        this.game = game;
        this.interestingEvents = interestingEvents;
        this.delayInSeconds = delayInSeconds;
        registerAsObserver();
    }

    /**
     * Registers this observer with the observable game.
     */
    private void registerAsObserver() {
        game.registerObserver(this);
    }

    /**
     * Updates the observer with a new event description, which is then "said" out
     * loud after a delay.
     *
     * @param eventDescription the description of the event that has occurred
     */
    @Override
    public void update(String eventDescription) {
        say(eventDescription);
    }

    /**
     * Outputs the message vocally using the system's text-to-speech capabilities.
     * 
     * @param message the message to be said out loud
     */
    private void say(String message) {
        try {
            String command = "say " + message;
            Runtime.getRuntime().exec(command).waitFor();
            Thread.sleep(delayInSeconds * 1000);
        } catch (Exception e) {
            System.err.println("Error saying message: " + e.getMessage());
        }
    }
}
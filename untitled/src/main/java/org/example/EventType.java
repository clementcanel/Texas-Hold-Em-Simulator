package org.example;

/**
 * Enumerates the types of events that can be handled by the EventBus.
 * Each event type represents a specific event that can occur within the system.
 */
public enum EventType {
    /** Represents all types of events. */
    All,
    /** Represents the event when a player folds. */
    Fold,
    /** Represents the event when a player wins. */
    Win,
    /** Represents the event when a player loses. */
    Lose,
    /** Represents the event when a player bets. */
    Bet,
    /** Represents the event when a game starts. */
    GameStart,
    /** Represents the event when a new hand is dealt. */
    NewHand
}

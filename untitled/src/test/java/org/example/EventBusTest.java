package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class EventBusTest {

    private EventBus eventBus;

    @BeforeEach
    // Resets the EventBus instance before each test to ensure a clean state.
    void setUp() {
        try {
            java.lang.reflect.Field instance = EventBus.class.getDeclaredField("instance");
            instance.setAccessible(true);
            instance.set(null, null);
            eventBus = EventBus.getInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    // Verifies that the EventBus class correctly implements the singleton pattern.
    void getInstance_ShouldReturnSameInstance() {
        EventBus anotherInstance = EventBus.getInstance();
        assertSame(eventBus, anotherInstance, "getInstance should return the same instance of EventBus");
    }

    @Test
    // Ensures that attaching an observer and posting a message results in the
    // observer receiving the message.
    void attach_ShouldAddObserverAndReceiveMessage() {
        AtomicBoolean messageReceived = new AtomicBoolean(false);
        IObserver observer = eventDescription -> messageReceived.set(true);

        eventBus.attach(observer, EventType.All);
        eventBus.postMessage(EventType.All, "Test Message");

        assertTrue(messageReceived.get(), "Observer should receive a message after being attached");
    }

    @Test
    // Confirms that observers receive the correct message when one is posted to
    // their event type.
    void postMessage_ShouldNotifyObserversWithCorrectMessage() {
        AtomicReference<String> receivedMessage = new AtomicReference<>("");
        IObserver observer = receivedMessage::set;

        eventBus.attach(observer, EventType.All);
        eventBus.postMessage(EventType.All, "Test Message");

        assertEquals("Test Message", receivedMessage.get(), "Observer should receive the correct message");
    }
}

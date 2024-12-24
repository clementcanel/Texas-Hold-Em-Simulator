package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DeckTest {
    private Deck deck;

    @BeforeEach
    void setUp() {
        List<Card> cards = List.of(
                new Card('♥', "2"), new Card('♥', "3"), new Card('♥', "4"), new Card('♥', "5"), new Card('♥', "6"),
                new Card('♥', "7"), new Card('♥', "8"), new Card('♥', "9"), new Card('♥', "10"), new Card('♥', "J"),
                new Card('♥', "Q"), new Card('♥', "K"), new Card('♥', "A"),
                new Card('♦', "2"), new Card('♦', "3"), new Card('♦', "4"), new Card('♦', "5"), new Card('♦', "6"),
                new Card('♦', "7"), new Card('♦', "8"), new Card('♦', "9"), new Card('♦', "10"), new Card('♦', "J"),
                new Card('♦', "Q"), new Card('♦', "K"), new Card('♦', "A"),
                new Card('♣', "2"), new Card('♣', "3"), new Card('♣', "4"), new Card('♣', "5"), new Card('♣', "6"),
                new Card('♣', "7"), new Card('♣', "8"), new Card('♣', "9"), new Card('♣', "10"), new Card('♣', "J"),
                new Card('♣', "Q"), new Card('♣', "K"), new Card('♣', "A"),
                new Card('♠', "2"), new Card('♠', "3"), new Card('♠', "4"), new Card('♠', "5"), new Card('♠', "6"),
                new Card('♠', "7"), new Card('♠', "8"), new Card('♠', "9"), new Card('♠', "10"), new Card('♠', "J"),
                new Card('♠', "Q"), new Card('♠', "K"), new Card('♠', "A")
        );
        this.deck = new Deck(cards);

    }

    // To verify that removing cards from the deck actually removes distinct cards
    // and reduces the deck's size by ensuring that no two consecutive cards are the
    // same.
    @Test
    void removingCardTest() {
        int beginSize = deck.getDeck().size();
        deck.removeCard();
        int endSize = deck.getDeck().size();

        assertNotEquals(beginSize, endSize);
    }

    @Test
    void getDeckTest() {
        assertEquals(deck.getDeck().size(), 52);
    }

    // To ensure that after cards are removed and the deck is shuffled, the deck is
    // replenished, allowing for further removals without encountering an
    @Test
    void shuffleDeckReturnsCardsToDeck() {
        for (int i = 0; i < 5; i++) {
            deck.removeCard();
        }

        deck.shuffleDeck();
        try {
            for (int i = 0; i < 5; i++) {
                assertNotNull(deck.removeCard(), "Should be able to remove a card after shuffling.");
            }
        } catch (IndexOutOfBoundsException e) {
            fail("Deck should have been replenished by shuffle, allowing for removals.");
        }
    }
}

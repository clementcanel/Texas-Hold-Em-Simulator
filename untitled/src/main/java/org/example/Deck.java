package org.example;

import java.util.*;

/**
 * Represents a deck of playing cards. It allows cards to be removed from the
 * deck,
 * collected into a discard pile, and the entire deck to be shuffled.
 */
public class Deck {
    private List<Card> deck;
    private List<Card> discards;

    /**
     * Constructs a new deck with a predefined list of cards.
     *
     * @param cards the initial list of cards to be included in the deck
     */
    public Deck(List<Card> cards) {
        this.deck = new ArrayList<>(cards);
        this.discards = new ArrayList<>();

    }

    /**
     * Removes the top card from the deck and adds it to the discard pile.
     *
     * @return the card that was removed from the deck
     */
    public Card removeCard() {
        Card returnCard = deck.get(0);
        deck.remove(0);
        discards.add(returnCard);
        return returnCard;
    }

    /**
     * Shuffles the deck by reincorporating cards from the discard pile back into
     * the deck
     * and then randomly reordering all the cards.
     */
    public void shuffleDeck() {
        deck.addAll(discards);
        discards.clear();
        Collections.shuffle(deck);
    }

    /**
     * Retrieves the current list of cards in the deck.
     *
     * @return a list of cards currently in the deck
     */
    public List<Card> getDeck() {
        return deck;
    }
}

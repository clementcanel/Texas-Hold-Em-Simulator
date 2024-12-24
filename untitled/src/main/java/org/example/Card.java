package org.example;

/**
 * Represents a single playing card with a suit and a rank.
 */
public class Card {
    private char suit;
    private String rank;

    /**
     * Constructs a new card with the specified suit and rank.
     *
     * @param suit the suit of the card, typically represented as a single character
     *             (e.g., 'H' for Hearts)
     * @param rank the rank of the card (e.g., "Ace", "2", "10", "King")
     */
    public Card(char suit, String rank) {
        this.suit = suit;
        this.rank = rank;
    }

    /**
     * Retrieves the suit of the card.
     *
     * @return the suit of the card as a character
     */
    public Character getSuit() {
        return suit;
    }

    /**
     * Retrieves the rank of the card.
     *
     * @return the rank of the card as a string
     */
    public String getRank() {
        return rank;
    }
}

package org.example;

import java.util.*;

/**
 * Represents a player in a poker game. This class holds details about the
 * player's cards,
 * money, betting status, and other game-related attributes.
 */
public class Player {
    private List<Card> cards;
    private Integer money;
    // Frequency of ranks and suits
    private Map<String, Integer> rankFrequency;
    private Map<Character, Integer> suitFrequency;
    private Boolean inHand;
    private String name;
    private int personality;
    private Boolean isDealer;
    private int currentBet;

    /**
     * Constructs a player with a specified amount of money and a name.
     *
     * @param money the starting amount of money the player has
     * @param name  the name of the player
     */
    public Player(int money, String name) {
        this.inHand = true;
        this.cards = new ArrayList<>();
        this.money = money; // Starting money
        this.rankFrequency = new HashMap<String, Integer>();
        this.suitFrequency = new HashMap<>();
        this.name = name;
        this.currentBet = 0;
        this.personality = new Random().nextInt(1, 6);
        this.isDealer = false;
    }

    /**
     * Sets the dealer status for this player.
     *
     * @param dealer true if the player is to be the dealer, false otherwise
     */
    public void setDealer(Boolean dealer) {
        isDealer = dealer;
    }

    /**
     * Returns the dealer status of this player.
     *
     * @return true if this player is the dealer, false otherwise
     */
    public Boolean getDealer() {
        return isDealer;
    }

    /**
     * Returns the current bet amount of the player.
     *
     * @return the current bet amount
     */
    public int getCurrentBet() {
        return currentBet;
    }

    /**
     * Sets the current bet amount for the player.
     *
     * @param bet the amount to be set as the current bet
     */
    public void setCurrentBet(int bet) {
        currentBet = bet;
    }

    /**
     * Returns the personality index of the player, which may affect gameplay
     * decisions.
     *
     * @return the personality index of the player
     */
    public int getPersonality() {
        return personality;
    }

    /**
     * Returns the amount of money the player currently has.
     *
     * @return the amount of money
     */
    public int getMoney() {
        return money;
    }

    /**
     * Returns the name of the player.
     *
     * @return the player's name
     */
    public String getName() {
        return name;
    }

    /**
     * Adds a card to the player's hand.
     *
     * @param card the card to add
     */
    public void addCards(Card card) {
        cards.add(card);
    }

    /**
     * Returns the list of cards the player is currently holding.
     *
     * @return the list of cards
     */
    public List<Card> getCards() {
        return cards;
    }

    /**
     * Removes all cards from the player's hand and sets their in-hand status to
     * false.
     */
    public void foldCards() {
        inHand = false;
        cards.clear();
    }

    // public void setDealer(Boolean dealer) {
    // isDealer = dealer;
    // }

    /**
     * Sets the player's in-hand status.
     *
     * @param value true if the player is still in the hand, false otherwise
     */
    public void setInHand(Boolean value) {
        inHand = value;
    }

    /**
     * Returns whether the player is still in the current hand.
     *
     * @return true if the player is still in the hand, false otherwise
     */
    public Boolean getInHand() {
        return inHand;
    }

    /**
     * Adds money to the player's total amount.
     *
     * @param amount the amount of money to add
     */
    public void addMoney(int amount) {
        money += amount;
    }

    /**
     * Places a bet by deducting the specified amount from the player's total money,
     * not exceeding the player's total money.
     *
     * @param amount the amount to bet
     * @return the actual bet amount, which could be less than the requested amount
     *         if the player's money is insufficient
     */
    public int betMoney(int amount) {
        int betAmount = Math.min(amount, money);
        money -= betAmount;
        return betAmount;
    }

    /**
     * Updates the frequency maps for ranks and suits based on the provided card.
     *
     * @param card the card to analyze and update frequencies for
     */
    private void updateFrequencies(Card card) {
        String rank = card.getRank();
        char suit = card.getSuit();

        rankFrequency.put(rank, rankFrequency.getOrDefault(rank, 0) + 1);
        suitFrequency.put(suit, suitFrequency.getOrDefault(suit, 0) + 1);
    }

    /**
     * Populates the frequency maps for ranks and suits based on the player's
     * current hand
     * and the table cards.
     *
     * @param tableCards the cards currently on the table to be considered in the
     *                   frequency calculation
     */
    public void populateFrequencies(List<Card> tableCards) {
        clearFrequencies();
        for (Card card : cards) {
            updateFrequencies(card);
        }
        for (Card card : tableCards) {
            updateFrequencies(card);
        }
    }

    /**
     * Clears the frequency maps for card ranks and suits.
     */
    private void clearFrequencies() {
        rankFrequency.clear();
        suitFrequency.clear();
    }

    /**
     * Checks if the player has a pair by examining the frequency of ranks in their
     * hand and table cards.
     *
     * @param tableCards the cards currently on the table
     * @return true if a pair is found, otherwise false
     */
    public boolean hasPair(List<Card> tableCards) {
        populateFrequencies(tableCards);

        for (int count : rankFrequency.values()) {
            if (count == 2) {
                return true; // Found a pair
            }
        }
        return false;
    }

    /**
     * Checks if the player has two pairs by examining the frequency of ranks in
     * their hand and table cards.
     *
     * @param tableCards the cards currently on the table
     * @return true if two pairs are found, otherwise false
     */
    public boolean hasTwoPair(List<Card> tableCards) {
        populateFrequencies(tableCards);

        int numPairs = 0;
        for (int count : rankFrequency.values()) {
            if (count == 2) {
                numPairs++;
            }
        }
        return numPairs == 2;
    }

    /**
     * Checks if the player has three of a kind by examining the frequency of ranks
     * in their hand and table cards.
     *
     * @param tableCards the cards currently on the table
     * @return true if three of a kind is found, otherwise false
     */
    public boolean hasThreeOfAKind(List<Card> tableCards) {
        populateFrequencies(tableCards);

        for (int count : rankFrequency.values()) {
            if (count == 3) {
                return true; // Found three of a kind
            }
        }
        return false;
    }

    /**
     * Checks if the player has a straight by examining the sequence of ranks in
     * their hand and table cards.
     *
     * @param tableCards the cards currently on the table
     * @return true if a straight is found, otherwise false
     */
    public boolean hasStraight(List<Card> tableCards) {
        populateFrequencies(tableCards);

        int straightCount = 0;
        for (int i = '2'; i <= 'A'; i++) {
            if (rankFrequency.containsKey((char) i)) {
                straightCount++;
            } else {
                straightCount = 0;
            }
            if (straightCount == 5) {
                return true; // Found a straight
            }
        }
        return false;
    }

    /**
     * Checks if the player has a flush by examining the frequency of suits in their
     * hand and table cards.
     *
     * @param tableCards the cards currently on the table
     * @return true if a flush is found (five or more cards of the same suit),
     *         otherwise false
     */
    public boolean hasFlush(List<Card> tableCards) {
        populateFrequencies(tableCards);

        for (int count : suitFrequency.values()) {
            if (count >= 5) {
                return true; // Found a flush
            }
        }
        return false;
    }

    /**
     * Checks if the player has a full house by examining the frequencies of ranks
     * to find a three of a kind and a pair.
     *
     * @param tableCards the cards currently on the table
     * @return true if a full house is found, otherwise false
     */
    public boolean hasFullHouse(List<Card> tableCards) {
        populateFrequencies(tableCards);

        boolean hasThree = false;
        boolean hasPair = false;

        for (int count : rankFrequency.values()) {
            if (count == 3) {
                hasThree = true;
            } else if (count == 2) {
                hasPair = true;
            }
        }

        return hasThree && hasPair;
    }

    /**
     * Checks if the player has four of a kind by examining the frequency of ranks
     * in their hand and table cards.
     *
     * @param tableCards the cards currently on the table
     * @return true if four of a kind is found, otherwise false
     */
    public boolean hasFourOfAKind(List<Card> tableCards) {
        populateFrequencies(tableCards);

        for (int count : rankFrequency.values()) {
            if (count == 4) {
                return true; // Found four of a kind
            }
        }
        return false;
    }

    /**
     * Checks if the player has a straight flush by examining the frequencies of
     * ranks and suits in their hand and table cards.
     *
     * @param tableCards the cards currently on the table
     * @return true if a straight flush is found, otherwise false
     */
    public boolean hasStraightFlush(List<Card> tableCards) {
        if (hasStraight(tableCards) && hasFlush(tableCards)) {
            // Check for a straight flush
            for (char suit : suitFrequency.keySet()) {
                int count = 0;
                for (int i = '2'; i <= 'A'; i++) {
                    char rank = (char) i;
                    if (rankFrequency.containsKey(rank) && suitFrequency.get(suit) >= 5) {
                        count++;
                    } else {
                        count = 0;
                    }
                    if (count == 5) {
                        return true; // Found a straight flush
                    }
                }
            }
        }
        return false;
    }

}

package org.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

import static org.example.EventType.Bet;
import static org.example.EventType.Fold;

/**
 * Represents a poker table in a game, managing players, deck, bets, and game
 * states.
 */
public class Table {

    private static final Logger logger = LoggerFactory.getLogger("csci.ooad.holdEm");

    Scanner scanner = new Scanner(System.in);

    private EventBus eventBus = EventBus.getInstance();
    private List<Player> players = new ArrayList<>();
    private int pot;
    private List<Card> tableCards = new ArrayList<>();
    private Deck deck;
    private Map<Player, Integer> playerRanks = new HashMap<>();

    /**
     * Creates a builder instance for constructing a Table.
     * 
     * @return A new instance of tableBuilder.
     */
    public static tableBuilder newBuilder() {
        return new tableBuilder();
    }

    public static class tableBuilder {
        private static final Logger logger = LoggerFactory.getLogger("csci.ooad.holdEm");
        private final Table table = new Table();
        private final playerFactory playerFactory;
        private final deckFactory deckFactory;

        public tableBuilder() {

            this.playerFactory = createPlayerFactory();
            this.deckFactory = createDeckFactory();

        }

        /**
         * Creates a playerFactory instance to manage the creation of players.
         * 
         * @return A new playerFactory instance.
         */
        public static playerFactory createPlayerFactory() {
            return new playerFactory();
        }

        /**
         * Creates a deckFactory instance to manage the creation of decks.
         * 
         * @return A new deckFactory instance.
         */
        public static deckFactory createDeckFactory() {
            return new deckFactory();
        }

        /**
         * Constructs the deck for the table using the deckFactory.
         * 
         * @return The builder instance with a constructed deck.
         */
        public tableBuilder createDeck() {

            Deck deck = deckFactory.createDeck();
            this.table.setDeck(deck);

            return this;

        }

        /**
         * Constructs the list of players for the table using the playerFactory based on
         * the specified number.
         * 
         * @param numPlayers The number of players to create.
         * @return The builder instance with a list of constructed players.
         */
        public tableBuilder createPlayers(int numPlayers) {
            List<Player> players = playerFactory.createPlayers(numPlayers);
            this.table.setPlayers(players);
            return this;
        }

        /**
         * Finalizes the construction of the Table and returns the constructed object.
         * 
         * @return The fully constructed Table.
         */
        public Table build() {
            return this.table;
        }

    }

    /**
     * Sets the players at the table.
     * 
     * @param players The list of players to set.
     */
    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    /**
     * Sets the deck to be used at the table.
     * 
     * @param deckToAdd The deck to set.
     */
    public void setDeck(Deck deckToAdd) {
        this.deck = deckToAdd;
    }

    /**
     * Returns the list of players at the table.
     * 
     * @return A list of players.
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Deals two cards to each player at the table.
     */
    public void dealPlayers() {

        for (Player player : players) {
            // give 1 cards
            player.addCards(deck.removeCard());
        }
        for (Player player : players) {
            // give 1 cards
            player.addCards(deck.removeCard());
        }
    }

    /**
     * Deals three cards to the table, known as the "flop" in poker.
     */
    public void dealFlop() {
        // Burn One Card
        deck.removeCard();
        // Deal the flop (3 cards)
        for (int i = 0; i < 3; i++) {
            tableCards.add(deck.removeCard());
        }
    }

    /**
     * Deals one card to the table, known as either the "turn" or "river" in poker.
     */
    public void dealTurnOrRiver() {
        // Burn One Card
        deck.removeCard();
        // Deal one card (turn or river)
        tableCards.add(deck.removeCard());
    }

    /**
     * Gets the current cards on the table.
     * 
     * @return A list of cards on the table.
     */
    public List<Card> getTableCards() {
        return tableCards;
    }

    public Deck getDeck() {
        return deck;
    }

    /**
     * Resets the table for a new hand, clearing players' hands, the table cards,
     * and the pot.
     */
    public void resetTable() {
        for (Player player : players) {
            player.foldCards();
        }
        tableCards.clear();
        pot = 0;
        deck.shuffleDeck();
        playerRanks.clear();
        for (Player player : players) {
            player.setInHand(true);
        }
    }

    /**
     * Evaluates the hands of all players still in the game to determine their
     * ranks.
     */
    public void evaluateHands() {

        for (Player player : players) {
            if (player.getInHand()) {
                int rank = 9; // Default to the weakest hand (high card)
                if (player.hasStraightFlush(tableCards)) {
                    rank = 1;
                } else if (player.hasFourOfAKind(tableCards)) {
                    rank = 2;
                } else if (player.hasFullHouse(tableCards)) {
                    rank = 3;
                } else if (player.hasFlush(tableCards)) {
                    rank = 4;
                } else if (player.hasStraight(tableCards)) {
                    rank = 5;
                } else if (player.hasThreeOfAKind(tableCards)) {
                    rank = 6;
                } else if (player.hasTwoPair(tableCards)) {
                    rank = 7;
                } else if (player.hasPair(tableCards)) {
                    rank = 8;
                }
                playerRanks.put(player, rank);
            }
        }

    }

    /**
     * Organizes players in a betting queue based on the dealer's position.
     * 
     * @return A list of players ordered for betting.
     */
    public List<Player> organizeBettingQueue() {
        int dealerIndex = -1;
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getDealer()) {
                dealerIndex = i;
                break;
            }
        }

        // Reorganize the list
        List<Player> reorganizedPlayers = new ArrayList<>();
        for (int i = dealerIndex + 1; i < players.size(); i++) {
            reorganizedPlayers.add(players.get(i));
        }
        for (int i = 0; i <= dealerIndex; i++) {
            reorganizedPlayers.add(players.get(i));
        }
        return reorganizedPlayers;

    }

    /**
     * Shifts the dealer position to the next player in the sequence.
     */
    public void setNextDealer() {
        int currentDealerIndex = -1;

        // Find the index of the current dealer
        for (int i = 0; i < players.size(); i++) {
            if (players.get(i).getDealer()) {
                currentDealerIndex = i;
                break;
            }
        }

        if (currentDealerIndex != -1) {
            // Set the current dealer to false
            players.get(currentDealerIndex).setDealer(false);

            // Set the next player as the dealer
            int nextDealerIndex = (currentDealerIndex + 1) % players.size();
            players.get(nextDealerIndex).setDealer(true);
        }
    }

    /**
     * Adds the specified amount to the pot.
     * 
     * @param amount The amount to add to the pot.
     */
    public void addToPot(int amount) {
        pot += amount;
    }

    /**
     * Retrieves the current total amount in the pot.
     * 
     * @return The current amount in the pot.
     */
    public int getPot() {
        return pot;
    }

    /**
     * Displays the current state of the table including the cards on the table,
     * each player's status, and the pot.
     * This method visually represents the game to help players understand the
     * current game situation.
     * 
     * @param currentPlayer The player whose turn it is to act. This player's
     *                      information is highlighted.
     */
    private void displayTable(Player currentPlayer) {
        int currentTableBet = 0;
        System.out.println("\n");
        System.out.println("Table Cards:");
        for (Card card : tableCards) {
            System.out.print(card.getRank() + card.getSuit() + " ");
        }
        System.out.println("Pot: " + pot + "\n");

        for (Player player : players) {
            if (Objects.equals(currentPlayer.getName(), player.getName())) {
                System.out.println("Player: " + player.getName() + " <-");
                System.out.println("Money: " + player.getMoney());
            } else {
                System.out.println("Player: " + player.getName());
                System.out.println("Money: " + player.getMoney());
            }
            if (player.getInHand()) {
                if (player.getCurrentBet() > currentTableBet) {
                    currentTableBet = player.getCurrentBet();
                }
                System.out.println("Current Bet: " + player.getCurrentBet());
                /*
                 * if (Objects.equals(player.getName(), "You")){
                 * System.out.print("Cards: ");
                 * for (Card card : player.getCards()) {
                 * System.out.print(card.getRank() + card.getSuit() + " ");
                 * }
                 * }
                 */
                System.out.print("Cards: ");
                for (Card card : player.getCards()) {
                    System.out.print(card.getRank() + card.getSuit() + " ");
                }
                System.out.print("\n");
            } else {
                System.out.println("Folded \n");
            }
        }
        System.out.println(currentTableBet + " dollars to CALL current bet");
        System.out.println("_________________________");

    }

    /**
     * Simulates a betting round where each player decides to call, raise, or fold.
     * This method handles user input for betting and updates the game state
     * accordingly.
     * 
     * @return True if the round should end, else false.
     * @throws InterruptedException If the thread sleep is interrupted.
     */
    public Boolean simulateBettingRound() throws InterruptedException {

        int playersRemaining = 0;
        for (Player player : players) {
            if (player.getInHand()) {
                playersRemaining += 1;
            }
        }
        if (playersRemaining == 1) {
            return true;
        }
        String userInput;
        int tempCurrentBet = 0;
        Integer currentBet = 0; // Starting bet
        int betAmount;
        // create new list when dealer is last
        for (Player player : players) {

            displayTable(player);

            int rank = playerRanks.get(player);
            if (player.getInHand()) {
                // Personalities ranked 1 - 5
                // 1 being a cautious player
                // 5 being a very aggressive player
                if (Objects.equals(player.getName(), "You") && player.getInHand()) {
                    System.out.println("Call the Current Bet, Raise, or type '-1' to fold");
                    int input = 0;
                    do {
                        // Check if the next input is an integer
                        while (!scanner.hasNextInt()) {
                            System.out.println("Stuck");
                            String put = scanner.next();
                            System.out
                                    .println(put + " is not a valid integer. Please enter a number greater or equal to "
                                            + currentBet.toString());

                        }

                        userInput = scanner.next();
                        if (Objects.equals(userInput, "-1")) {
                            player.foldCards();
                            break;
                        }
                        input = Integer.parseInt(userInput);
                        tempCurrentBet = input;
                        System.out.print("\n");

                        // Check if the input is within the range
                        if (input == player.getMoney() && input < currentBet) {

                            System.out.println("You are All In, Good Luck!");
                            tempCurrentBet = currentBet;
                            currentBet = input;
                        }
                        if (input == player.getMoney() && input > currentBet) {

                            System.out.println("You are All In, Good Luck!");
                        }
                        if (input < currentBet) {
                            System.out.println("Please enter a number greater or equal to " + currentBet.toString());
                        }
                        if (input > player.getMoney()) {
                            System.out.println("Please enter a number less than or equal to your current stack");
                        }
                    } while ((input < currentBet || input > player.getMoney()));

                    if (player.getInHand()) {
                        player.betMoney(input);
                        player.setCurrentBet(input);
                        addToPot(input);
                        currentBet = tempCurrentBet;
                    }

                } else {
                    Thread.sleep(4000);
                    switch (player.getPersonality()) {
                        case 1, 2, 3:
                            int personality = player.getPersonality();
                            betAmount = calculateBetAmount(personality, currentBet, rank, player);
                            if (betAmount > currentBet) {
                                if ((betAmount / (currentBet + 1)) < 2) {
                                    // If calculated bet amount is less than double the current bet
                                    // Player just calls
                                    player.betMoney(currentBet);
                                    player.setCurrentBet(currentBet);
                                    addToPot(currentBet);
                                } else {
                                    // If more than double the current bet
                                    // player raises to the calculated amount
                                    player.betMoney(betAmount);
                                    currentBet = betAmount;
                                    addToPot(currentBet);
                                    player.setCurrentBet(currentBet);

                                }
                            } else if ((currentBet / betAmount) > 1) {
                                if (personality == 1 && rank < 3) {
                                    // personality 1 never folds full house or better
                                    player.betMoney(currentBet);
                                    addToPot(currentBet);
                                    player.setCurrentBet(currentBet);
                                } else if (personality == 2 && rank < 4) {
                                    // personality 2 never folds flush or better
                                    player.betMoney(currentBet);
                                    addToPot(currentBet);
                                    player.setCurrentBet(currentBet);
                                } else if (personality == 3 && rank < 5) {
                                    // personality 2 never folds straight or better
                                    player.betMoney(currentBet);
                                    addToPot(currentBet);
                                    player.setCurrentBet(currentBet);
                                } else {
                                    // if current bet is more than double the calculated amount
                                    // and player does not have a strong hand, player folds
                                    player.foldCards();
                                    logger.info("Player " + player.getName() + " folds.");
                                    eventBus.postMessage(Fold, "Player " + player.getName() + " folded");
                                }
                            } else {
                                // player calls the current bet
                                player.betMoney(currentBet);
                                addToPot(currentBet);
                                player.setCurrentBet(currentBet);
                            }

                            break;
                        case 4:
                            betAmount = calculateBetAmount(4, currentBet, rank, player);
                            if (betAmount > currentBet) {
                                if ((betAmount / (currentBet + 1)) < 2) {
                                    // If calculated bet amount is less than double the current bet
                                    // Player just calls
                                    player.betMoney(currentBet);
                                    addToPot(currentBet);
                                    player.setCurrentBet(currentBet);
                                } else {
                                    // If more than double the current bet
                                    // player raises to the calculated amount
                                    player.betMoney(betAmount);
                                    currentBet = betAmount;
                                    addToPot(currentBet);
                                    player.setCurrentBet(currentBet);
                                }
                            } else if ((currentBet / betAmount) > 1) {
                                // player folds of current bet is more than double the calculated bet amount
                                player.foldCards();
                                logger.info("Player " + player.getName() + " folds.");
                                eventBus.postMessage(Fold, "Player " + player.getName() + " folded");
                            } else {
                                // player calls the current bet
                                player.betMoney(currentBet);
                                addToPot(currentBet);
                                player.setCurrentBet(currentBet);
                            }
                            break;
                        default:
                            betAmount = calculateBetAmount(5, currentBet, rank, player);
                            if (betAmount == 0) {
                                // player folds if the pot is larger than half player's stack
                                // and player has nothing
                                // See calculate bet amount for logic
                                player.foldCards();
                                logger.info("Player " + player.getName() + " folds.");
                                eventBus.postMessage(Fold, "Player " + player.getName() + " folded");
                            } else {
                                player.betMoney(betAmount);
                                currentBet = betAmount;
                                addToPot(currentBet);
                                player.setCurrentBet(currentBet);
                            }

                    }
                    if (player.getInHand()) {
                        eventBus.postMessage(Bet, "Player " + player.getName() + " bet " + currentBet);
                    }

                }
            }
        }

        return checkReRaise(currentBet);
    }

    /**
     * Calculates the bet amount for a player based on their personality and hand
     * strength.
     * 
     * @param personality The personality identifier of the player, affecting their
     *                    betting behavior.
     * @param currentBet  The current highest bet that has been made.
     * @param rank        The rank of the player's hand.
     * @param player      The player object.
     * @return The calculated bet amount.
     */
    private int calculateBetAmount(int personality, int currentBet, int rank, Player player) {
        int betAmount = 0;

        switch (personality) {
            case 1:
                // Cautious player: bets less with weaker hands
                if (rank > 5) {
                    betAmount = (int) (0.05 * player.getMoney());
                } else {
                    betAmount = (int) (0.1 * player.getMoney());
                }
                break;
            case 2:
                // Moderate player: bets a bit more than cautious player
                if (rank > 5) {
                    betAmount = (int) (0.1 * player.getMoney());
                } else {
                    betAmount = (int) (0.15 * player.getMoney());
                }
                break;
            case 3:
                // Balanced player: adjusts bet based on hand strength and current bet
                if (rank > 5) {
                    betAmount = (int) (0.15 * player.getMoney());
                } else {
                    betAmount = (int) (0.2 * player.getMoney());
                }
                break;
            case 4:
                // Aggressive player: bets aggressively with weaker hands
                if (rank < 3) {
                    betAmount = (int) (0.15 * player.getMoney());
                } else if (rank <= 5) {
                    betAmount = (int) (0.25 * player.getMoney());
                } else {
                    betAmount = (int) (0.3 * player.getMoney());
                }
                break;
            default:
                // Very aggressive player: adjusts bet based on current bet
                // Only folds if player has nothing and there is a large pot
                if (currentBet < 0.5 * player.getMoney()) {
                    betAmount = currentBet + 50; // Increment from current bet
                } else if (rank == 9) {
                    betAmount = 0;
                } else {
                    betAmount = currentBet; // Call instead of raise
                }
                break;
        }
        return betAmount;
    }

    /**
     * Checks if a re-raise condition is met and handles re-raising or folding
     * decisions.
     * 
     * @param currentBet The current bet amount to consider for re-raising.
     * @return True if the round should end, else false.
     * @throws InterruptedException If the thread sleep is interrupted.
     */
    private Boolean checkReRaise(int currentBet) throws InterruptedException {
        int playersRemaining = 0;
        for (Player player : players) {
            if (player.getInHand()) {
                playersRemaining += 1;
            }
        }
        if (playersRemaining == 1) {
            return true;
        }
        int input = 0;
        for (Player player : players) {
            int rank = playerRanks.get(player);
            if (player.getInHand()) {
                displayTable(player);
                String userInput;
                int betDiff = currentBet - player.getCurrentBet();
                if (Objects.equals(player.getName(), "You") && betDiff != 0) {
                    System.out.println("Call " + betDiff + " dollars or type '-1' to fold");
                    do {
                        // Check if the next input is an integer

                        userInput = scanner.next();
                        if (Objects.equals(userInput, "-1")) {
                            player.foldCards();
                            logger.info("Player " + player.getName() + " folds.");
                            eventBus.postMessage(Fold, "Player " + player.getName() + " folded");
                            break;
                        }
                        input = Integer.parseInt(userInput);
                        System.out.print("\n");

                        // Check if the input is within the range
                        if (input + player.getCurrentBet() != currentBet) {
                            System.out.println("Please enter a number equal to the Re-Raise");
                        }

                    } while (input + player.getCurrentBet() != currentBet);
                    if (player.getInHand()) {
                        player.betMoney(input);
                        player.setCurrentBet(input);
                        addToPot(input);
                    }

                } else {
                    Thread.sleep(4000);
                    if (player.getCurrentBet() < currentBet) {
                        switch (player.getPersonality()) {
                            case 1:
                                // personality 1 will not call a re-raise
                                // unless they have a straight or better
                                if (rank < 6) {
                                    player.betMoney(currentBet - player.getCurrentBet());
                                    player.setCurrentBet(currentBet);
                                } else {
                                    player.foldCards();
                                    logger.info("Player " + player.getName() + " folds.");
                                    eventBus.postMessage(Fold, "Player " + player.getName() + " folded");
                                }
                                break;
                            case 2:
                                // personality 2 will not call a re-raise
                                // unless they have a three of a kind or better
                                if (rank < 7) {
                                    player.betMoney(currentBet - player.getCurrentBet());
                                    player.setCurrentBet(currentBet);
                                } else {
                                    player.foldCards();
                                    logger.info("Player " + player.getName() + " folds.");
                                    eventBus.postMessage(Fold, "Player " + player.getName() + " folded");
                                }
                                break;
                            case 3:
                                // personality 3 will not call a re-raise
                                // unless they have a two pair or better
                                if (rank < 8) {
                                    player.betMoney(currentBet - player.getCurrentBet());
                                    player.setCurrentBet(currentBet);
                                } else {
                                    player.foldCards();
                                    logger.info("Player " + player.getName() + " folds.");
                                    eventBus.postMessage(Fold, "Player " + player.getName() + " folded");
                                }
                                break;
                            case 4:
                                // personality 4 will not call a re-raise
                                // unless they have a pair or better
                                if (rank < 9) {
                                    player.betMoney(currentBet - player.getCurrentBet());
                                    player.setCurrentBet(currentBet);
                                } else {
                                    player.foldCards();
                                    logger.info("Player " + player.getName() + " folds.");
                                    eventBus.postMessage(Fold, "Player " + player.getName() + " folded");
                                }
                                break;
                            default:
                                // 5 will always call the re-raise
                                player.betMoney(currentBet - player.getCurrentBet());
                                player.setCurrentBet(currentBet);
                        }
                        if (player.getInHand()) {
                            eventBus.postMessage(Bet, "Player " + player.getName() + " bet " + currentBet);
                        }
                    }
                }
            }

            player.setCurrentBet(0);
        }

        return false;
    }

    /**
     * Distributes the pot to the winning player at the end of a round.
     */
    public void awardPotToWinner() {
        Player winningPlayer = null;
        int winningRank = -1;
        String name = null;

        for (Player player : players) {
            if (player.getInHand()) {
                int playerRank = playerRanks.get(player);
                if (playerRank > winningRank) {
                    winningRank = playerRank;
                    winningPlayer = player;
                    name = player.getName();
                }
            }
        }

        if (winningPlayer != null) {
            winningPlayer.addMoney(pot); // Give the pot to the winning player
            logger.info("Player " + winningPlayer.getName() + " wins the pot of " + pot);
            eventBus.postMessage(EventType.Win, "Player " + name + " won " + pot + " dollars");
        } else {
            logger.info("No winner. Pot remains.");
            eventBus.postMessage(EventType.Win, "No winner, pot remains");
        }
    }

    /**
     * Creates a list of players for the game.
     * 
     * @param numPlayers The number of players to create.
     * @return A list of newly created players.
     */
    static class playerFactory {
        public List<Player> createPlayers(Integer numPlayers) {
            List<Player> players = new ArrayList<>();
            List<String> playerNames = Arrays.asList(
                    "You", "Phil", "Daniel", "Johnny", "Miki", "Stu", "Chris", "Erik", "Jennifer", "Bryn");
            if (numPlayers > 10) {
                numPlayers = 10;
            }
            for (int i = 0; i < numPlayers; i++) {
                players.add(new Player(500, playerNames.get(i)));

            }
            players.get(0).setDealer(true);
            return players;
        }
    }

    /**
     * Creates a standard 52-card deck.
     * 
     * @return A new deck of cards.
     */
    static class deckFactory {
        public Deck createDeck() {
            List<Card> cards = new ArrayList<>();
            List<Character> suits = List.of('♥', '♣', '♦', '♠');
            // List<Character> suits = List.of('H', 'C', 'D', 'S');
            List<String> ranks = Arrays.asList("2", "3", "4", "5", "6", "7", "8", "9", "10", "J", "Q", "K", "A");
            for (Character suit : suits) {
                for (String rank : ranks) {
                    cards.add(createCard(suit, rank));
                }
            }

            return new Deck(cards);
        }

        /**
         * Constructs a card with a specified suit and rank.
         * 
         * @param suit The suit of the card.
         * @param rank The rank of the card.
         * @return A newly created card.
         */
        public Card createCard(char suit, String rank) {
            return new Card(suit, rank);
        }

    }

    /**
     * Removes a player from the table based on their name.
     * 
     * @param playerName The name of the player to be removed.
     */
    public void removePlayer(String playerName) {
        int index = 0;
        for (Player player : players) {
            if (Objects.equals(player.getName(), playerName)) {
                players.remove(index);
            }
            index += 1;
        }
    }

}

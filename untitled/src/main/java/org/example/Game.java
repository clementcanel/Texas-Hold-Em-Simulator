package org.example;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
/**
 * Represents a game of Texas Hold'em poker. This class manages the game flow,
 * tracks game state, and communicates game events to observers.
 */
public class Game implements IObservable {

    private static final Logger logger = LoggerFactory.getLogger("csci.ooad.holdEm");
    private final EventBus eventBus;

    private Table table;
    private Boolean gameOver;
    private Integer hand;

    /**
     * Constructs a new Game initializing the game over flag, event bus, and
     * starting hand.
     */
    public Game() {
        this.gameOver = false;
        this.eventBus = EventBus.getInstance();
        this.hand = 1;
    }

    /**
     * Sets the table where the game will be played.
     *
     * @param tableToPlay the table to set for playing the game
     */
    public void setTable(Table tableToPlay) {
        this.table = tableToPlay;
    }

    /**
     * Starts and manages the main game loop and event posting.
     * It also handles game start and end conditions, notifying observers of
     * relevant events.
     *
     * @throws InterruptedException if thread sleeping is interrupted
     */
    public void play() throws InterruptedException {
        audibleObserver audibleObserver = new audibleObserver(
                this, List.of(EventType.GameStart, EventType.Win, EventType.Lose, EventType.Fold, EventType.Bet,
                        EventType.NewHand),
                2);
        registerObserver(audibleObserver);
        eventBus.attach(audibleObserver, EventType.GameStart);
        eventBus.attach(audibleObserver, EventType.Win);
        eventBus.attach(audibleObserver, EventType.Lose);
        eventBus.attach(audibleObserver, EventType.Fold);
        eventBus.attach(audibleObserver, EventType.Bet);
        eventBus.attach(audibleObserver, EventType.NewHand);
        eventBus.postMessage(EventType.GameStart, "The game has started! Good Luck!");
        System.out.print("Welcome to our Texas Hold Em Simulator! Be the last man Standing!\n");

        while (!gameOver) {
            playHand();
        }
        List<Player> WinnerList = table.getPlayers();
        Player Winner = WinnerList.get(0);

        logger.info("Game Over! Thanks For Playing!");
        if (Objects.equals(Winner.getName(), "You")) {
            eventBus.postMessage(EventType.Win, "Congradulations! You Won!");
        } else {
            eventBus.postMessage(EventType.Lose, "Player " + Winner.getName() + " won the poker game!");
        }
        eventBus.postMessage(EventType.Lose, "Player " + Winner.getName() + " won the poker game!");

    }

    /**
     * Executes the logic for a single hand of poker.
     * This includes dealing cards, evaluating hands, simulating betting rounds, and
     * managing game state transitions.
     *
     * @throws InterruptedException if thread sleeping is interrupted
     */
    private void playHand() throws InterruptedException {

        logger.info("Starting hand " + hand + "... \n");
        eventBus.postMessage(EventType.NewHand, "Starting hand " + hand);
        table.resetTable();
        table.setPlayers(table.organizeBettingQueue());

        table.dealPlayers();
        table.evaluateHands();

        if (table.simulateBettingRound()) {
            table.evaluateHands();
            table.awardPotToWinner();
            table.setNextDealer();
            gameOver = checkGameOver();
            hand += 1;
            return;
        }

        table.dealFlop();
        table.evaluateHands();

        if (table.simulateBettingRound()) {
            table.evaluateHands();
            table.awardPotToWinner();
            table.setNextDealer();
            gameOver = checkGameOver();
            hand += 1;
            return;
        }

        table.dealTurnOrRiver();
        table.evaluateHands();

        if (table.simulateBettingRound()) {
            table.evaluateHands();
            table.awardPotToWinner();
            table.setNextDealer();
            gameOver = checkGameOver();
            hand += 1;
            return;
        }

        table.dealTurnOrRiver();
        table.evaluateHands();

        if (table.simulateBettingRound()) {
            // print who won
            table.evaluateHands();
            table.awardPotToWinner();
            table.setNextDealer();
            gameOver = checkGameOver();
            hand += 1;
            return;
        }

        table.evaluateHands();

        table.awardPotToWinner();

        gameOver = checkGameOver();

        table.setNextDealer();
        hand += 1;
    }

    /**
     * Checks if the game should end based on the number of players remaining and
     * their monetary status.
     *
     * @return true if the game is over, false otherwise
     */
    private Boolean checkGameOver() {
        List<Player> players = table.getPlayers();
        for (Player player : players) {
            if (player.getMoney() == 0) {
                table.removePlayer(player.getName());
            }
        }
        return table.getPlayers().size() == 1;
    }

    // Registers an observer to this game
    @Override
    public void registerObserver(IObserver observer) {

    }

    // Removes an observer from this game
    @Override
    public void removeObserver(IObserver observer) {

    }
}

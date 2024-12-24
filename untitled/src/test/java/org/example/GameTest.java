package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

public class GameTest {


    @BeforeEach
        // Resets the EventBus instance before each test to ensure a clean state.
    void setUp() {
        Game game = new Game();
        Table table = Table.newBuilder().createPlayers(4).createDeck().build();
        game.setTable(table);

    }
    @Test // find out how to simulate user input to get 80% line coverage. At
    // 67% if running this for a few hands and stopping it
    void gameplayTest() throws InterruptedException {
        Game game = new Game();
        Table table = Table.newBuilder().createPlayers(3).createDeck().build();
        table.getPlayers().remove(0); // removes "You" player
        game.setTable(table);

        game.play();

        System.setIn(System.in);

    }






}

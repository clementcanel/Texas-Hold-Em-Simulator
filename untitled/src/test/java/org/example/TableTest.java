package org.example;

import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TableTest {

    private Table table;

    @BeforeEach
    public void setUp() {

        this.table = Table.newBuilder().createPlayers(3).createDeck().build();

    }

    @Test
    public void testDealPlayers() {
        table.dealPlayers();
        for (Player player : table.getPlayers()) {
            assertEquals(2, player.getCards().size());
        }
    }

    @Test
    public void testDealFlop() {
        table.dealFlop();
        assertEquals(3, table.getTableCards().size());
    }

    @Test
    public void testDealTurnOrRiver() {
        table.dealTurnOrRiver();
        assertEquals(1, table.getTableCards().size());
    }

    @Test
    public void testResetTable() {
        table.dealPlayers();
        table.dealFlop();
        table.resetTable();
        assertEquals(0, table.getTableCards().size());
        for (Player player : table.getPlayers()) {
            assertEquals(0, player.getCards().size());
        }
    }

    @Test
    public void testAwardPotToWinner() {
        table.addToPot(100);
        Player player1 = table.getPlayers().get(0);
        Player player2 = table.getPlayers().get(1);
        table.evaluateHands();
        table.awardPotToWinner();
        table.resetTable();
        assertEquals(600, player1.getMoney());
        assertEquals(0, table.getPot());
    }

    @Test
    public void testSimulateBettingRound() throws InterruptedException {
        List<Player> list = new ArrayList<>();
        list.add(new Player(500, "John"));
        list.add(new Player(500, "James"));
        table.setPlayers(list);
        table.dealPlayers();
        table.dealFlop();
        table.evaluateHands();
        table.simulateBettingRound();
        assertNotEquals(table.getPot(), 0);
    }
}

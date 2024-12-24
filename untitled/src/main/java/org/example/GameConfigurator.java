package org.example;

import java.util.Scanner;

/**
 * Configures and launches the Texas Hold Em game simulator.
 * This class is responsible for setting up the game environment based on user
 * input,
 * including the number of players participating in the game.
 */
public class GameConfigurator {

    /**
     * The main method that starts the setup of the Texas Hold Em Simulator.
     * It prompts the user to specify the number of players and initializes the game
     * with the specified number of players.
     *
     * @param args the command line arguments (not used in this application)
     * @throws InterruptedException if the game play is interrupted
     */
    public static void main(String[] args) throws InterruptedException {
        Scanner scanner = new Scanner(System.in);
        int userInput;
        System.out.print(
                "\nWelcome to our Texas Hold Em Simulator! \n Choose number of players (2-10) including yourself : ");

        do {
            // Check if the next input is an integer
            while (!scanner.hasNextInt()) {
                String input = scanner.next();
                System.out.println(input + " is not a valid integer. Please enter a number between 2 and 10.");
                System.out.print("Enter an integer between 2 and 10: ");
            }
            userInput = scanner.nextInt();
            System.out.print("\n");

            // Check if the input is within the range
            if (userInput < 2 || userInput > 10) {
                System.out.println("Number must be between 2 and 10.");
            }
        } while (userInput < 1 || userInput > 10);

        Game game = new Game();
        Table table = Table.newBuilder().createPlayers(userInput).createDeck().build();
        game.setTable(table);
        game.play();

    }

}
// defines the structure for any game (initialization, loop, result display, and input handling)
package general;

import dotsandboxes.DotsandBoxesGame;
import slidingpuzzle.SlidingPuzzleGame;
import quoridor.QuoridorGame;

// abstract class for all games (Sliding Puzzle, Dots and Boxes, etc.)
public abstract class Game {

    protected static final java.util.Scanner INPUT = new java.util.Scanner(System.in);

    // main entry for all games
    public static void start() {
        System.out.println("Welcome!");

        boolean running = true; // loop control for main menu
        while (running) {
            System.out.println("\nChoose a game:");
            System.out.println("1. Dots and Boxes");
            System.out.println("2. Sliding Puzzle");
            System.out.println("3. Quoridor");
            System.out.println("4. Exit");

            int choice = getValidatedInt("Enter your choice: ");

            Game game = null;

            switch (choice) {
                case 1:
                    System.out.println("You have chosen Dots and Boxes!");
                    game = new DotsandBoxesGame();
                    break;
                case 2:
                    System.out.println("You have chosen Sliding Puzzle!");
                    game = new SlidingPuzzleGame();
                    break;
                case 3:
                    System.out.println("You have chosen Quoridor!");
                    game = new QuoridorGame();
                    break;
                case 4:
                    System.out.println("Exiting... Goodbye!");
                    running = false;
                    continue; // skip game loop
                default:
                    System.out.println("Invalid choice! Please enter 1, 2, 3 or 4.");
                    continue; // re-show menu
            }

            // if a valid game is selected
            if (game != null) {
                game.initialize();
                game.runGameLoop();
                game.displayResult();
            }

            // ask if user wants to play another game or exit
            System.out.print("\nDo you want to play another game? (y/n): ");
            String again = "";
            while (again.isEmpty()) {
                again = INPUT.nextLine().trim().toLowerCase();
                if (again.isEmpty()) {
                    System.out.print("Please enter 'y' or 'n': ");
                }
            }
            if (!again.equals("y")) {
                System.out.println("Thanks for playing! Goodbye!");
                running = false;
            }
        }
    }

    protected static int getValidatedInt(String prompt) {
        while (true) {
            if (prompt != null && !prompt.isEmpty()) {
                System.out.print(prompt);
            }

            String line = INPUT.nextLine().trim();

            if (line.isEmpty()) {
                System.out.println("Please enter a number.");
                continue;
            }

            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Invalid input! Please enter a valid number.");
            }
        }
    }

    // abstract methods to be implemented by each game
    protected abstract void initialize();

    protected abstract void runGameLoop();

    protected abstract boolean isGameOver();

    protected abstract void displayResult();
}

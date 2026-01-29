// implements gameplay logic, alternating turns, and score handling
package dotsandboxes;

import general.Game;
import general.Player;

public class DotsandBoxesGame extends Game {
    private DotsandBoxesBoard board;
    private Player player1;
    private Player player2;

    @Override
    protected void initialize() {
        System.out.println("\n--- Dots and Boxes ---");

        System.out.print("Enter name for Player 1: ");
        String name1 = INPUT.nextLine().trim();
        while (name1.isEmpty()) {
            System.out.print("Name cannot be empty. Enter name for Player 1: ");
            name1 = INPUT.nextLine().trim();
        }

        System.out.print("Enter name for Player 2: ");
        String name2 = INPUT.nextLine().trim();
        while (name2.isEmpty()) {
            System.out.print("Name cannot be empty. Enter name for Player 2: ");
            name2 = INPUT.nextLine().trim();
        }

        player1 = new Player(name1, 1);
        player2 = new Player(name2, 2);

        int rows = 0, cols = 0;
        while (rows <= 0) {
            rows = getValidatedInt("Enter number of rows for the board: ");
            if (rows <= 0)
                System.out.println("Number of rows must be greater than 0.");
        }

        while (cols <= 0) {
            cols = getValidatedInt("Enter number of columns for the board: ");
            if (cols <= 0)
                System.out.println("Number of columns must be greater than 0.");
        }

        board = new DotsandBoxesBoard(rows, cols);

        System.out.println("Board: " + board.getRows() + " x " + board.getCols() + " dots.");
        System.out.println("Enter each coordinate separately");
        System.out.println("Enter 0 at any point to exit the game.\n");
    }

    @Override
    protected void runGameLoop() {
        Player current = player1;
        boolean exitGame = false;

        while (!isGameOver() && !exitGame) {
            board.display();
            System.out.println("\n" + current.getName() + "'s turn (" + (current.getId() == 1 ? "X" : "O") + ")");

            int r1 = askCoord("Enter row for point 1 (1-" + board.getRows() + ", or 0 to exit): ", 0, board.getRows());
            if (r1 == 0) {
                exitGame = true;
                break;
            }
            int c1 = askCoord("Enter col for point 1 (1-" + board.getCols() + ", or 0 to exit): ", 0, board.getCols());
            if (c1 == 0) {
                exitGame = true;
                break;
            }
            int r2 = askCoord("Enter row for point 2 (1-" + board.getRows() + ", or 0 to exit): ", 0, board.getRows());
            if (r2 == 0) {
                exitGame = true;
                break;
            }
            int c2 = askCoord("Enter col for point 2 (1-" + board.getCols() + ", or 0 to exit): ", 0, board.getCols());
            if (c2 == 0) {
                exitGame = true;
                break;
            }

            // convert 1-based -> 0-based for board methods
            r1--;
            c1--;
            r2--;
            c2--;

            if (!board.isValidLine(r1, c1, r2, c2)) {
                System.out.println("Invalid line! Dots must be adjacent and the line must be unused. Try again.\n");
                continue;
            }

            boolean madeBox = board.drawLine(r1, c1, r2, c2, current);
            if (madeBox) {
                System.out.println(current.getName() + " completed a box!");
                // same player goes again
            } else {
                // switch players
                current = (current == player1) ? player2 : player1;
            }
        }

        if (exitGame) {
            System.out.println("Exiting current game...\n");
        } else {
            displayResult();
        }
    }

    // helper to get a validated integer in [min,max] (inclusive)
    private int askCoord(String prompt, int min, int max) {
        while (true) {
            int v = getValidatedInt(prompt); // uses shared Scanner in Game
            if (v == 0)
                return 0; // exit
            if (v < min + 1 || v > max) { // we expect 1..max from user
                System.out.println("Please enter a number between " + (min + 1) + " and " + max + " (or 0 to exit).");
                continue;
            }
            return v;
        }
    }

    @Override
    protected boolean isGameOver() {
        return board.isFull();
    }

    @Override
    protected void displayResult() {
        System.out.println("\nAll boxes claimed!");
        board.display();

        int p1 = board.getScore(1);
        int p2 = board.getScore(2);

        System.out.println("\nFinal Scores:");
        System.out.println(player1.getName() + ": " + p1);
        System.out.println(player2.getName() + ": " + p2);

        if (p1 > p2) {
            System.out.println(player1.getName() + " wins!");
        } else if (p2 > p1) {
            System.out.println(player2.getName() + " wins!");
        } else {
            System.out.println("It's a tie!");
        }
    }
}

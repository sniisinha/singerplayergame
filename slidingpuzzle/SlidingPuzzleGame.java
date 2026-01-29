// Main game for SlidingPuzzle
package slidingpuzzle;

import general.Game;

public class SlidingPuzzleGame extends Game {
    private SlidingPuzzleBoard board;
    private int moves;
    private static final int MAX_MOVES = 100;

    @Override
    protected void initialize() {
        System.out.println("\n--- Sliding Puzzle ---");
        int m = getValidatedInt("Enter number of rows (m): ");
        int n = getValidatedInt("Enter number of columns (n): ");

        if (m < 2 || n < 2) {
            System.out.println("Invalid size. Defaulting to 3x3.");
            m = 3;
            n = 3;
        }

        // instantiate subclass, not abstract parent
        board = new SlidingPuzzleBoard(m, n);
        board.shuffle();
        moves = 0;
    }

    @Override
    protected void runGameLoop() {
        while (!isGameOver()) {
            board.display();
            System.out.println("Moves made: " + moves + "/" + MAX_MOVES);
            int tileNum = getValidatedInt("Enter tile number to move (0 to exit): ");

            if (tileNum == 0) {
                System.out.println("Exiting puzzle...");
                break;
            }

            // validate tile range
            if (tileNum < 0 || tileNum >= board.getRows() * board.getCols()) {
                System.out.println("Invalid tile number! Try again.");
                continue;
            }

            // attempt move
            if (!board.moveTile(tileNum)) {
                System.out.println("Invalid move! Try again.");
            } else {
                moves++;
            }

            // check for solved condition
            if (board.isSolved()) {
                System.out.println("🎉 Congratulations! You solved the puzzle in " + moves + " moves!");
                return;
            }

            // check for move limit
            if (moves >= MAX_MOVES) {
                System.out.println("Out of moves! Better luck next time.");
                return;
            }
        }
    }

    @Override
    protected boolean isGameOver() {
        return board.isSolved() || moves >= MAX_MOVES;
    }

    @Override
    protected void displayResult() {
        board.display();
        if (board.isSolved()) {
            System.out.println("You solved the puzzle!");
        } else {
            System.out.println("Puzzle unsolved after " + moves + " moves.");
        }
    }
}
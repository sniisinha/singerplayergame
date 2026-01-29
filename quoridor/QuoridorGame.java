// main Quoridor game (supports multiplayer)
package quoridor;

import general.Game;
import general.Player;
import java.util.ArrayList;
import java.util.List;


public class QuoridorGame extends Game {

    private QuoridorBoard board;
    private List<Player> players;
    private int currentIdx;

    @Override
    protected void initialize() {
        System.out.println("\n--- Quoridor ---");
        int n;
        while (true) {
            System.out.print("How many players (2-4)? ");
            n = getValidatedInt("");
            if (n >= 2 && n <= 4)
                break;
            System.out.println("Please enter 2, 3, or 4.");
        }

        players = new ArrayList<>();
        char[] syms = { 'A', 'B', 'C', 'D' };
        for (int i = 0; i < n; i++) {
            players.add(new Player("Player " + (i + 1) + " [" + syms[i] + "]"));
        }

        board = new QuoridorBoard(n);
        currentIdx = 0;

        System.out.println(board.getRows() + "x" + board.getCols() + " board initialized! "
                + players.get(0).getName() + " starts.");
    }

    @Override
    protected void runGameLoop() {
        while (!isGameOver()) {
            board.display();
            Player cur = players.get(currentIdx);
            System.out.println("\n" + cur.getName() + "'s turn.");
            System.out.println("1) Move pawn");
            System.out.println("2) Place wall");
            System.out.println("0) Exit");

            int choice = getValidatedInt("Enter your choice: ");
            if (choice == 0) {
                System.out.println("Exiting game...");
                break;
            }

            boolean success = false;
            int pid = currentIdx + 1; // 1-based for board

            switch (choice) {
                case 1:
                    System.out.print("Enter direction (up/down/left/right): ");
                    String dir = INPUT.nextLine().trim().toLowerCase();
                    success = board.movePawn(pid, dir);
                    break;

                case 2:
                    int r = getValidatedInt("Enter wall row: ");
                    int c = getValidatedInt("Enter wall column: ");
                    System.out.print("Enter orientation (h/v): ");
                    String orient = INPUT.nextLine().trim().toLowerCase();
                    success = board.placeWall(r, c, orient, pid);
                    break;

                default:
                    System.out.println("Invalid choice! Try again.");
                    continue;
            }

            if (!success) {
                System.out.println("Invalid move. Try again.");
                continue;
            }

            // check win (current player)
            if (board.hasPlayerWon(pid)) {
                System.out.println(cur.getName() + " wins!");
                break;
            }

            // rotate to next player
            currentIdx = (currentIdx + 1) % players.size();
        }
    }

    @Override
    protected boolean isGameOver() {
        for (int i = 0; i < players.size(); i++) {
            if (board.hasPlayerWon(i + 1))
                return true;
        }
        return false;
    }

    @Override
    protected void displayResult() {
        board.display();
        for (int i = 0; i < players.size(); i++) {
            if (board.hasPlayerWon(i + 1)) {
                System.out.println(players.get(i).getName() + " reached the goal!");
                return;
            }
        }
        System.out.println("Game ended early.");
    }
}
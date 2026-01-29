package dotsandboxes;

import general.Board;
import general.Player;

/**
 * Board class for Dots and Boxes, extending the abstract Board.
 * Handles line drawing, box completion, and player scoring.
 */
public class DotsandBoxesBoard extends Board {
    private LinePiece[][] horizontalLines;
    private LinePiece[][] verticalLines;
    private int[][] boxes; // 0 = unclaimed, 1 = Player1, 2 = Player2

    public DotsandBoxesBoard(int rows, int cols) {
        super(rows, cols);

        horizontalLines = new LinePiece[rows][cols - 1];
        verticalLines = new LinePiece[rows - 1][cols];
        boxes = new int[rows - 1][cols - 1];

        int id = 1;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols - 1; c++)
                horizontalLines[r][c] = new LinePiece(id++);

        for (int r = 0; r < rows - 1; r++)
            for (int c = 0; c < cols; c++)
                verticalLines[r][c] = new LinePiece(id++);
    }

    public boolean isValidLine(int r1, int c1, int r2, int c2) {
        if (!isInside(r1, c1) || !isInside(r2, c2))
            return false;

        int rowDiff = Math.abs(r1 - r2);
        int colDiff = Math.abs(c1 - c2);
        if (rowDiff + colDiff != 1) return false;

        if (r1 == r2) {
            int row = r1;
            int col = Math.min(c1, c2);
            return !horizontalLines[row][col].isActive();
        } else {
            int row = Math.min(r1, r2);
            int col = c1;
            return !verticalLines[row][col].isActive();
        }
    }

    public boolean drawLine(int r1, int c1, int r2, int c2, Player player) {
        boolean completedBox = false;
        int playerId = player.getId();
        String symbol = (playerId == 1) ? "X" : "O";

        if (r1 == r2) { // Horizontal line
            int row = r1;
            int col = Math.min(c1, c2);
            horizontalLines[row][col].setOwner(player.getName());
            horizontalLines[row][col].setActive(true);
            horizontalLines[row][col].setSymbol(symbol);

            if (row > 0 && isBoxComplete(row - 1, col)) {
                boxes[row - 1][col] = playerId;
                completedBox = true;
            }
            if (row < rows - 1 && isBoxComplete(row, col)) {
                boxes[row][col] = playerId;
                completedBox = true;
            }
        } else { // Vertical line
            int row = Math.min(r1, r2);
            int col = c1;
            verticalLines[row][col].setOwner(player.getName());
            verticalLines[row][col].setActive(true);
            verticalLines[row][col].setSymbol(symbol);

            if (col > 0 && isBoxComplete(row, col - 1)) {
                boxes[row][col - 1] = playerId;
                completedBox = true;
            }
            if (col < cols - 1 && isBoxComplete(row, col)) {
                boxes[row][col] = playerId;
                completedBox = true;
            }
        }
        return completedBox;
    }

    private boolean isBoxComplete(int row, int col) {
        if (row < 0 || col < 0 || row >= rows - 1 || col >= cols - 1)
            return false;

        return horizontalLines[row][col].isActive() &&
               horizontalLines[row + 1][col].isActive() &&
               verticalLines[row][col].isActive() &&
               verticalLines[row][col + 1].isActive();
    }

    public boolean isFull() {
        for (int[] row : boxes)
            for (int b : row)
                if (b == 0) return false;
        return true;
    }

    @Override
    public void display() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols - 1; c++) {
                System.out.print("•");
                System.out.print(horizontalLines[r][c].isActive() ? "──" : "  ");
            }
            System.out.println("•");

            if (r < rows - 1) {
                for (int c = 0; c < cols; c++) {
                    System.out.print(verticalLines[r][c].isActive() ? "|" : " ");
                    if (c < cols - 1) {
                        int box = boxes[r][c];
                        char mark = box == 0 ? ' ' : (box == 1 ? 'X' : 'O');
                        System.out.print(" " + mark + " ");
                    }
                }
                System.out.println();
            }
        }
    }

    public int getScore(int playerId) {
        int score = 0;
        for (int[] row : boxes)
            for (int cell : row)
                if (cell == playerId) score++;
        return score;
    }
}

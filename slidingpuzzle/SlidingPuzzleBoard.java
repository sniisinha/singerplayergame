// board for the Sliding Puzzle game
package slidingpuzzle;

import general.Board;
import general.Piece;
import java.util.Random;

// extends the generic Board class and uses Tiles that contain Pieces.

public class SlidingPuzzleBoard extends Board {
    private Tile[][] grid;
    private int emptyRow, emptyCol;

    public SlidingPuzzleBoard(int rows, int cols) {
        super(rows, cols);
        grid = new Tile[rows][cols];

        int num = 1;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (num == rows * cols) {
                    grid[r][c] = new Tile(null);
                    emptyRow = r;
                    emptyCol = c;
                } else {
                    grid[r][c] = new Tile(new NumberPiece(num, String.valueOf(num)));
                }
                num++;
            }
        }
    }

    public void shuffle() {
        Random rand = new Random();
        for (int i = 0; i < rows * cols * 10; i++) {
            int r = rand.nextInt(rows);
            int c = rand.nextInt(cols);
            moveTile(r, c);
        }
    }

    public boolean moveTile(int tileNumber) {
        int tileRow = -1, tileCol = -1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Piece p = grid[i][j].getPiece();
                if (p != null && p.getId() == tileNumber) {
                    tileRow = i;
                    tileCol = j;
                }
            }
        }

        if (tileRow == -1) return false;

        if ((Math.abs(tileRow - emptyRow) == 1 && tileCol == emptyCol) ||
            (Math.abs(tileCol - emptyCol) == 1 && tileRow == emptyRow)) {

            grid[emptyRow][emptyCol].setPiece(grid[tileRow][tileCol].getPiece());
            grid[tileRow][tileCol].setPiece(null);
            emptyRow = tileRow;
            emptyCol = tileCol;
            return true;
        }
        return false;
    }

    public boolean isSolved() {
        int num = 1;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                Piece p = grid[i][j].getPiece();
                if (i == rows - 1 && j == cols - 1) return p == null;
                if (p == null || p.getId() != num++) return false;
            }
        }
        return true;
    }

    @Override
    public void display() {
        System.out.println();
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                Piece p = grid[r][c].getPiece();
                System.out.print((p == null ? "   " : String.format("%2s ", p.getSymbol())));
            }
            System.out.println();
        }
        System.out.println();
    }

    // helper method for random shuffling
    private void moveTile(int r, int c) {
        Piece p = grid[r][c].getPiece();
        if (p != null) moveTile(p.getId());
    }
}

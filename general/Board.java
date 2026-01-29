package general;

/**
 * Abstract base class representing a generic game board.
 * All specific game boards (Dots and Boxes, Sliding Puzzle, etc.)
 * extend this to reuse structure, dimensions, and utility methods.
 */
public abstract class Board {
    protected int rows;
    protected int cols;

    public Board(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    /**
     * Abstract display method to be implemented
     * by each specific game board.
     */
    public abstract void display();

    /**
     * Utility method to check if coordinates are inside the board.
     */
    protected boolean isInside(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }
}
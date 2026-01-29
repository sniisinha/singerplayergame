// represents a single cell in the sliding puzzle grid
package slidingpuzzle;

import general.Piece;


 // Tile contains a Piece or is empty.
public class Tile {
    private Piece piece;

    public Tile(Piece piece) {
        this.piece = piece;
    }

    public Piece getPiece() {
        return piece;
    }

    public void setPiece(Piece piece) {
        this.piece = piece;
    }

    public boolean isEmpty() {
        return piece == null;
    }

    @Override
    public String toString() {
        return (piece == null) ? " " : piece.toString();
    }
}
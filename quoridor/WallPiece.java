// represents a wall placed by a player
package quoridor;

import general.Piece;

public class WallPiece extends Piece {
    public WallPiece(int id, String symbol) {
        super(id, symbol);
    }

    @Override
    public boolean interact() {
        // Walls don't interact actively with players
        return false;
    }
}
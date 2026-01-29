// represents a player pawn on the Quoridor board
package quoridor;

import general.Piece;

public class PawnPiece extends Piece {
    public PawnPiece(int id, String symbol) {
        super(id, symbol);
    }

    @Override
    public boolean interact() {
        // Placeholder: could define jump or capture behavior later
        return false;
    }
}

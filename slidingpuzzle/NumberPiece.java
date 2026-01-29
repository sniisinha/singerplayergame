// represents a numbered tile piece for the Sliding Puzzle
package slidingpuzzle;

import general.Piece;

public class NumberPiece extends Piece {
    public NumberPiece(int id, String symbol) {
        super(id, symbol);
    }

    @Override
    public boolean interact(){
        return false;
    }
}

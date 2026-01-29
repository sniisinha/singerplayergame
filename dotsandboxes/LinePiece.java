// represents an individual line that can be claimed by a player
package dotsandboxes;

import general.Piece;

public class LinePiece extends Piece {
    private boolean claimed;

    public LinePiece(int id) {
        super(id, "-");
        this.claimed = false;
        this.active = false;
        this.owner = null;   
    }

    // claims with player name
    public void claim(String playerName) {
        this.claimed = true;
        this.active = true;      // line is now drawn/active
        this.owner = playerName; 
    }

    // claims with numeric id
    public void claim(int playerId) {
        claim(String.valueOf(playerId));
    }

    @Override
    public boolean interact() {
        if (!claimed) {
            claimed = true;
            active = true;
            return true;
        }
        return false;
    }

    public boolean isClaimed() {
        return claimed;
    }

    @Override
    public String getOwner() {
        return owner;
    }
}
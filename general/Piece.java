// base class for any movable or claimable unit
package general;
// generic abstract Piece class — used by all board-based games
public abstract class Piece {
    protected int id;             // unique identifier for the piece (e.g., tile number or line id)
    protected String symbol;      // what to display (e.g., "5" or "X" or "O")
    protected String owner;       // player name or team that owns the piece (if any)
    protected boolean active;     // can represent if piece is placed, claimed, or active in gameplay

    // Constructor
    public Piece(int id, String symbol) {
        this.id = id;
        this.symbol = symbol;
        this.owner = null;
        this.active = false;
    }

    // Accessors and mutators
    public int getId() {
        return id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // determines if two pieces are the same (e.g., same id)
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Piece other = (Piece) obj;
        return this.id == other.id;
    }

    // generic representation of a piece
    @Override
    public String toString() {
        return (symbol == null || symbol.equals("0")) ? " " : symbol;
    }

    // abstract method — how this piece interacts in the game
    // (e.g., sliding in Sliding Puzzle, claiming in Dots and Boxes)
    public abstract boolean interact();
}

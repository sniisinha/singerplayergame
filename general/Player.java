// generic Player class usable by any turn-based game.
package general;


 // supports player names, IDs, scores, and active-turn tracking.

public class Player {
    private String name;
    private int id;           // unique player identifier
    private int score;
    private boolean active;   // marks whose turn it is (if applicable)

    public Player(String name) {
        this(name, 0);
    }

    // constructor for multiplayer games.
    public Player(String name, int id) {
        this.name = name;
        this.id = id;
        this.score = 0;
        this.active = false;
    }

    // getters and setters

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getScore() {
        return score;
    }

    public void addScore(int points) {
        this.score += points;
    }

    public void resetScore() {
        this.score = 0;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    // utility Methods

    // toggle whose turn it is
    public void toggleTurn() {
        this.active = !this.active;
    }

    // compare players by score for end-game ranking
    public int compareTo(Player other) {
        return Integer.compare(this.score, other.score);
    }

    @Override
    public String toString() {
        return name + " (ID: " + id + ") — Score: " + score + (active ? " (Current Turn)" : "");
    }
}
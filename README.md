# Sliding Puzzle, Dots and Boxes & Quoridor

## Overview
This project implements a modular, object-oriented Java framework for turn-based board games, including:
- **Sliding Puzzle** (single-player)
- **Dots and Boxes** (two-player)
- **Quoridor** (two-player)

The design is built for **scalability** and **extensibility**, allowing additional games to be added easily with minimal code duplication.

---

## File Information

### General
| File | Description |
|------|--------------|
| `general/Game.java` | Abstract parent class that defines the structure for any game (initialization, loop, result display, and input handling). |
| `general/Player.java` | Represents a player (name, score, and active state). |
| `general/Piece.java` | Base class for any movable or claimable unit (used by all games). |
| `general/Board.java` | Abstract superclass for any grid-based board (provides dimensions and helper validation). |

### Sliding Puzzle
| File | Description |
|------|--------------|
| `slidingpuzzle/SlidingPuzzleGame.java` | Implements the game logic for Sliding Puzzle (initialization, moves, and win detection). |
| `slidingpuzzle/SlidingPuzzleBoard.java` | Manages the grid, shuffling, and tile movement. |
| `slidingpuzzle/Tile.java` | Represents a single numbered tile on the puzzle board. |
| `slidingpuzzle/NumberPiece.java` | Represents a numbered piece within the puzzle (extends `Piece`). |

### Dots and Boxes
| File | Description |
|------|--------------|
| `dotsandboxes/DotsandBoxesGame.java` | Implements gameplay logic, alternating turns, and score handling. |
| `dotsandboxes/DotsandBoxesBoard.java` | Manages board structure (dots, lines, boxes) and handles drawing lines and checking box completion. |
| `dotsandboxes/LinePiece.java` | Represents an individual line that can be claimed by a player. |

### Quoridor
| File | Description |
|------|--------------|
| `quoridor/QuoridorGame.java` | Main game controller that manages turns, player movement, and wall placement. |
| `quoridor/QuoridorBoard.java` | Implements a fixed 9×9 board (as per Quoridor rules), pawn movement, and wall placement logic with wall count tracking. |
| `quoridor/PawnPiece.java` | Represents a player pawn on the Quoridor board (extends `Piece`). |
| `quoridor/WallPiece.java` | Represents a wall placed by a player (extends `Piece`). |

### Main
| File | Description |
|------|--------------|
| `Main.java` | Entry point. Simply calls `Game.start()` to launch the game selection menu. |

---

## Compilation and Execution

### Compile All Files
Open a terminal in the project root and run:
```bash
mkdir bin
```
- run this command only if the bin directory is not already present

```bash
javac -d bin general/*.java dotsandboxes/*.java slidingpuzzle/*.java quoridor/*.java Main.java
```

- `-d bin` places compiled `.class` files into the `bin/` directory.
- Make sure the folder structure matches the package structure:
  ```
  src/
  ├── general/
  ├── dotsandboxes/
  ├── slidingpuzzle/
  ├── quoridor/
  └── Main.java
  ```

### Run the Program

```bash
java -cp bin Main
```

---

## Sample Input/Output

### Example: Running the Game

```
🎮 Welcome!
Choose a game:
[Dots and Boxes, Sliding Puzzle, Quoridor]
Enter your choice: Quoridor
You have chosen Quoridor!

--- Quoridor ---
Board size is fixed at 9x9 (standard Quoridor rules).
Board initialized! Player 1 starts.

Walls remaining — Player 1: 10, Player 2: 10

Player 1's turn:
1) Move pawn
2) Place wall
0) Exit
Enter your choice: 2
Enter wall row: 5
Enter wall column: 4
Enter orientation (h/v): h
✅ Wall placed at row 5, column 4 by Player 1.
Walls remaining — Player 1: 9, Player 2: 10

Player 2's turn:
1) Move pawn
2) Place wall
0) Exit
Enter your choice: 1
Enter direction (up/down/left/right): up
Move successful.

...
🏆 Player 1 wins!
```

---

```
Choose a game:
[Dots and Boxes, Sliding Puzzle, Quoridor]
Enter your choice: Sliding Puzzle
You have chosen Sliding Puzzle!

--- Sliding Puzzle ---
Enter number of rows (m): 3
Enter number of columns (n): 3

• The puzzle board shuffles •

Moves made: 0/100
Enter tile number to move (0 to exit): 5
Invalid move! Try again.
Enter tile number to move (0 to exit): 8
Move accepted!
Moves made: 1/100
...
🎉 Congratulations! You solved the puzzle in 12 moves!

Do you want to play another game? (y/n): y
Choose a game:
[Dots and Boxes, Sliding Puzzle, Quoridor]
Enter your choice: Dots and Boxes
You have chosen Dots and Boxes!

Enter Player 1 name: Alice
Enter Player 2 name: Bob
Enter number of rows: 3
Enter number of columns: 3

• Board initialized •

Alice's turn:
Enter first dot row: 1
Enter first dot column: 2
Enter second dot row: 1
Enter second dot column: 3
Line drawn successfully.

Bob's turn:
Enter first dot row: ...
...
Game over!
Alice — Score: 3
Bob — Score: 2
Alice wins!

Do you want to play another game? (y/n): n
Goodbye!
```
---

# Italian Draughts - Rules

* **The domain.Board:** An 8x8 grid of 64 squares, alternating black and white.
* **Orientation:** The board must be positioned so that the bottom-right square is **black** for both players.
* **Players:** Two players: White and Black.

## domain.Board Notation
|       | 0  | 1  | 2  | 3  | 4  | 5  | 6  | 7  |
|:-----:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|
| **0** | 1  |    | 2  |    | 3  |    | 4  |    |
| **1** |    | 5  |    | 6  |    | 7  |    | 8  |
| **2** | 9  |    | 10 |    | 11 |    | 12 |    |
| **3** |    | 13 |    | 14 |    | 15 |    | 16 |
| **4** | 17 |    | 18 |    | 19 |    | 20 |    |
| **5** |    | 21 |    | 22 |    | 23 |    | 24 |
| **6** | 25 |    | 26 |    | 27 |    | 28 |    |
| **7** |    | 29 |    | 30 |    | 31 |    | 32 |

# Gameplay
* **Assignments:** White and Black are assigned randomly.
* **First domain.Move:** White always takes the first turn.
* **Outcomes:** A game ends in a Win for one player or a Draw.

## Turn Actions
Each turn, a player may perform one of the following:
1. **Simple domain.Move:** Moving a piece diagonally to an adjacent empty square.
2. **Capture:** Jumping over an opponent's piece into an empty square, resulting in the removal of the captured piece.

* **Selection Rule:** Once a player selects a piece to move, they are obligated to complete their turn with that specific piece.

## domain.Piece Movement
* **Standard Pieces:** May only move forward diagonally.
* **General Movement:** All pieces (standards and Kings) move only to empty, adjacent black squares.
* **Kings:** May move both forward and backward diagonally.
* **Promotion:** A standard piece that reaches the farthest row on the opponent's side is immediately promoted to a King.

## domain.Piece capture
* **Standard domain.Piece Restrictions:** A standard piece (Man) can only capture other Men; it cannot capture Kings.
* **Standard domain.Piece Direction:** A Man can only capture forward by jumping diagonally into an empty square.
* **King Capabilities:** A King can capture both Men and other Kings.
* **King Direction:** A King can capture in both directions (forward and backward) diagonally.
* **Sequencing:** Both single and multiple captures are allowed. A multiple capture sequence is treated as a single turn.

## logic.Game Constraints (Priority Rules)
Implementation must enforce the following priorities for legal moves:
* **Mandatory Capture:** If a capture is possible, the player must take it.
* **Friendly Fire:** Players cannot capture their own pieces.
* **Obstacles:** During a multiple capture, a player cannot jump over their own pieces.
* **The "One Jump" Rule:** During a multiple capture, a square may be passed over more than once, but an opponent's piece cannot be captured more than once.
* **Quantity Rule:** If there are multiple capture paths, the player must choose the path that captures the **maximum number of pieces**.
* **Rank Rule:** If the number of capturable pieces is equal, the player must capture using the **highest-ranking piece** (a King must capture instead of a Standard domain.Piece).
* **Quality Rule:** If the number of pieces and the rank of the capturing piece are equal, the player must choose the path that captures the **highest-ranking pieces** (capturing a King is prioritized over a Standard domain.Piece).
* **Player Choice:** If all priority conditions are equal, the player may choose freely.

## Endgame
* **Resignation:** A player wins if the opponent retires.
* **Blocked:** A player wins if the opponent has no legal moves remaining on their turn.
* **Elimination:** A player wins if the opponent has no pieces left on the board.

### Draw Conditions
A draw occurs when:
* Both players agree to a draw.
* The **domain.Move-Count Rule** is triggered.

#### domain.Move-Count Rule
* Triggered when both players possess at least one King.
* A draw is declared if **40 consecutive moves** are made by each player without any captures occurring.
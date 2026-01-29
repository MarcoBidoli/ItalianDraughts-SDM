# Development Journal & Agile Process

## Sprint 1: Requirements Analysis & Core Architecture
**Status: ✅**

### 1. First analysis
During the initial kickoff, we analyzed the official **Italian Checkers** rules. We identified the following critical logic requirements:
* **Movement:** Diagonal progression.
* **Capturing:** Implementation of mandatory jump rules (the "blow" rule is not applied, jumps are strictly required).
* **Promotion:** Logic for transforming a Piece into a King (Dama) upon reaching the last row.
* **Win/Loss Conditions:** Tracking game states including WIN, DRAW, and LOSE.

### 2. Design
We defined a modular structure to separate objects and responsibilities:
* **Cell Class:** Represents an individual square on the board.
* **Board Class:** A matrix-based model (`Cell[8][8]`) managing the spatial layout.
* **Piece Class:** Models the attributes and behavior of single pieces and Kings.

### 3. Team Organization & Task Distribution
To optimize development, we assigned one core class per team member with a git strategy of one branch per feature strategy.

---

## Sprint 2: Move Logic & Rule Enforcement
**Status:** ✅

In this second meeting we decided to focus on a set of legal moves computed for each turn of a player instead of checking the possibilities after piece selection.
We opted for modelling moves as a class and use `List<Move>` to model a single move (can be multiple).

### 1. Game logic design
* We implemented the `Action` class to act as a central engine.
* **Decoupling:** By separating `Move` into its own class, we ensured that `Piece` remains a simple data model, while `Action` handles the job of listing all the legal moves.

### 2. Implementation of `moving()` & `eating()`
This sprint focused on the core mechanics of the game:
* **Move Object:** Created a dedicated `Move` class to encapsulate coordinates and capture data, moving away from simple integer arrays.
* **Implementation of "eating" logic**.
* **Implementation of "moving" logic** + unit tests.
* **Mandatory Moves:** We drafted the constraint logic. If a capture is available, the `Action` class will returns only the eating available moves. Otherwise, it will return a list of all possible moves for that player.



### 3. Other
* **Boundary checks:** Following multiple edge-case failures, we standardized the `isOnBoard()` method in the `Board` class  to prevent coordinate overflows during diagonal scans. This check is delegated to the `Action` and all other components of the game will threat a `Move` without any other check.
* **Visual Debugging:** Added `printBoard()` and `getBoardRepresentation()` to the `Board` class. This allowed us to verify the board state via console before the GUI was ready.
---

## Sprint 3: Game Management
**Status:** 🏗️ In Progress

### 1. The "Game" Controller Layer
We introduced the `Game` class to act as the central orchestrator. This layer bridges the gap between the board data and the movement rules.
* **Responsibilities:** Managing turn rotation (White/Black), tracking game status (WIN/DRAW/LOSE), and handling the move execution loop.
* **Tactical Design Decision:** We chose to centralize responsibilities in this class for the moment to reach a functional MVP faster. This technical debt will be addressed in future refactoring.

### 2. Game class
The `Game` class will `board` object where to perform all actions:
1. **Request:** Consults the `Action` class to retrieve the list of legal moves.
2. **Select:** Interfaces with the user (currently via console) to select a move.
3. **Enforce:** Only moves provided by the `Action` class are accepted.
4. **Update:** Modifies the `board` and promotes pieces to King (Dama) if they reach the final row.

### 3. Action class - King eating logic
We are expanding the `Action` class to handle the specific complexities of the Italian King:
* **King-Specific Eating:** Implementation of a new method to manage King captures. This requires checking for multi-directional jumps that regular pieces cannot perform.
* **Constraint:** Integrating the King's logic to follow the mandatory capture behavior.
* Add tests for both `eating()` and `kingEating()`

### 4. Documentation
* **Process Traceability:** Initialized this development journal and updated the `README.md` to help all team members understand what's going on.

---
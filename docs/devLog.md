# Methodology & Agile Process

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
package italian_draughts.logic;

import italian_draughts.domain.*;

import java.util.ArrayList;
import java.util.List;

public class LegalMoves {
    private final Board gameBoard;
    private final GameColor player;
    private final int direction;

    public LegalMoves(Board gameBoard, GameColor player) {
        this.gameBoard = gameBoard;
        this.player = player;
        this.direction = this.player.equals(GameColor.BLACK) ? 1 : -1;
    }

    public List<List<Move>> getSinglePieceLegalMoves(int row, int col) throws InvalidMoveException {
        List<List<Move>> allMoves = getLegalMoves();
        return allMoves.stream()
                .filter(m -> m.getFirst().fromRow == row && m.getFirst().fromCol == col)
                .toList();
    }

    public List<List<Move>> getLegalMoves() throws InvalidMoveException {
        List<List<Move>> moves = new ArrayList<>(eating()); // initialize the moves with legal eatings

        // if none, check for legal simple moves
        /* the complex return is a workaround behind the fact that this returns a
         * [[moves for piece 1], [moves for piece 2], ...]
         * instead of
         * [[move 1 for piece 1], [move 2 for piece 1], [move 1 for piece 2], ...]
         */
        if (moves.isEmpty()) {
            return moving();
        }
        return moves;
    }

    public List<List<Move>> eating() throws InvalidMoveException {
        List<List<Move>> allEatings = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (gameBoard.isPieceOwnedBy(player, i, j)) {
                    findEatings(i, j, new ArrayList<>(), allEatings);
                }
            }
        }

        if (!allEatings.isEmpty())
            sortEatings(allEatings);

        return allEatings.stream()
                .filter(eating -> checkBest(eating, allEatings.getFirst()))
                .toList();
    }

    private boolean checkBest(List<Move> e1, List<Move> e2) {
        if(e1.size() != e2.size())
            return false;

        boolean isE1K = gameBoard.isPieceWithCoordinatesKing(e1.getFirst().fromRow, e1.getFirst().fromCol);
        boolean isE2K = gameBoard.isPieceWithCoordinatesKing(e2.getFirst().fromRow, e2.getFirst().fromCol);
        if(isE1K != isE2K)
            return false;

        return countKingsEaten(e1) == countKingsEaten(e2);
    }
    private void sortEatings(List<List<Move>> allEatings) {
        allEatings.sort((e1, e2) -> {
            //eating length first
            int l = Integer.compare(e2.size(), e1.size());
            if (l != 0)
                return l;

            //who eats
            boolean isE1K = gameBoard.isPieceWithCoordinatesKing(e1.getFirst().fromRow, e1.getFirst().fromCol);
            boolean isE2K = gameBoard.isPieceWithCoordinatesKing(e2.getFirst().fromRow, e2.getFirst().fromCol);
            l = Boolean.compare(isE2K, isE1K);
            if (l != 0)
                return l;

            //#kings eaten
            int k1 = countKingsEaten(e1);
            int k2 = countKingsEaten(e2);
            return Integer.compare(k1, k2);
        });
    }

    private int countKingsEaten(List<Move> eatings) {
        int c = 0;
        for (Move m : eatings) {
            if (m instanceof EatingMove em)
                if (em.kingEaten())
                    c++;
        }
        return c;
    }

    private void findEatings(int x, int y, List<Move> eatings, List<List<Move>> allEatings) throws InvalidMoveException {
        boolean hasEat = false;
        int[][] dirs;
        if (gameBoard.isPieceWithCoordinatesKing(x,y))
            dirs = new int[][]{{1, -1}, {1, 1}, {-1, -1}, {-1, 1}};
        else
            dirs = new int[][]{{direction, -1}, {direction, 1}};

        for (int[] i : dirs) { //do it for every direction
            //eating "positions" - opponent and "over opponent"
            int xOpp = x + i[0];
            int yOpp = y + i[1];
            int finX = x + 2 * i[0];
            int finY = y + 2 * i[1];

            if (canEat(x, y, xOpp, yOpp, finX, finY)) { //check if an eating is doable
                hasEat = true;
                eatings.add(new EatingMove(x, y, finX, finY, gameBoard.isPieceWithCoordinatesKing(xOpp, yOpp)));

                //simulation of the new status of the board (with the opponent's piece eaten and the player's piece moved)
                Piece eaten = gameBoard.getPieceAt(xOpp, yOpp);
                gameBoard.emptyCell(xOpp, yOpp);
                Piece moved = gameBoard.getPieceAt(x,y);
                gameBoard.emptyCell(x,y);
                /* TODO: move this logic inside placePiece() and remove placeKing(), then replace the if block with
                 * board.placePiece(pieceToMove, currentMove.toRow, currentMove.toCol);
                 * */
                if(moved.isKing()) {
                    gameBoard.placeKing(moved.getColor(), finX, finY);
                } else {
                    gameBoard.placePiece(moved.getColor(), finX, finY);
                }

                //recursive call
                findEatings(finX, finY, eatings, allEatings);

                //restore the board
                gameBoard.emptyCell(finX, finY);
                /* TODO: move this logic inside placePiece() and remove placeKing(), then replace the if block with
                 * board.placePiece(pieceToMove, currentMove.toRow, currentMove.toCol);
                 * */
                if(moved.isKing()) {
                    gameBoard.placeKing(moved.getColor(), x, y);
                } else {
                    gameBoard.placePiece(moved.getColor(), x, y);
                }
                eatings.removeLast();
                /* TODO: move this logic inside placePiece() and remove placeKing(), then replace the if block with
                 * board.placePiece(pieceToMove, currentMove.toRow, currentMove.toCol);
                 * */
                if(eaten.isKing()) {
                    gameBoard.placeKing(eaten.getColor(), xOpp, yOpp);
                } else {
                    gameBoard.placePiece(eaten.getColor(), xOpp, yOpp);
                }
            }
        }

        //add the possible eating to the total possible eats list
        if (!hasEat && !eatings.isEmpty())
            allEatings.add(new ArrayList<>(eatings));
    }

    private boolean canEat(int x, int y, int xOpp, int yOpp, int finX, int finY) {
        //compute whether the eating is doable or not
        if (Board.positionIsOffBoard(finX, finY))
            return false;

        if (gameBoard.isEmptyCell(xOpp, yOpp) || gameBoard.isPieceOwnedBy(player, xOpp, yOpp))
            return false;

        if (!gameBoard.isEmptyCell(finX, finY))
            return false;

        return gameBoard.getPieceAt(x, y) == null || gameBoard.isPieceWithCoordinatesKing(x, y) || !gameBoard.isPieceWithCoordinatesKing(xOpp, yOpp);
    }

    public List<List<Move>> moving() {
        List<List<Move>> listOfAllMoves = new ArrayList<>();

        // Putting all player pieces coordinates in a list
        List<Coords> myPiecesCoords = new ArrayList<>();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if(gameBoard.isPieceOwnedBy(player, i, j))
                    myPiecesCoords.add(new Coords(i, j));

        // For each save coordinate, check what legal moves are associated with that piece
        // If player == BLACK front moves are increasing i
        for (Coords coord : myPiecesCoords) {
            List<Move> movesForThisPiece = new ArrayList<>();

            // for all pieces, checking the frontwards moves

            // (coord) -> (row+direction, col+1)
            addCoordToLegalMoves(coord, new Coords(coord.row() + direction, coord.col() + 1), movesForThisPiece);
            // (coord) -> (row+direction, col-1)
            addCoordToLegalMoves(coord, new Coords(coord.row() + direction, coord.col() - 1), movesForThisPiece);

            // if king, checking the backwards moves
            if (gameBoard.isPieceWithCoordinatesKing(coord.row(), coord.col())) {
                // (coord) -> (row-direction, col+1)
                addCoordToLegalMoves(coord, new Coords(coord.row() - direction, coord.col() + 1), movesForThisPiece);
                // (coord) -> (row-direction, col-1)
                addCoordToLegalMoves(coord, new Coords(coord.row() - direction, coord.col() - 1), movesForThisPiece);
            }

            if (!movesForThisPiece.isEmpty())
                movesForThisPiece.forEach(m -> listOfAllMoves.add(new ArrayList<>(List.of(m))));
        }
        return listOfAllMoves;
    }

    private void addCoordToLegalMoves(Coords fromCoord, Coords toCoord, List<Move> movesForThisPiece) {
        // Avoid out of board moves
        if (Board.positionIsOffBoard(toCoord.row(), toCoord.col())) return;

        // check destination is empty
        if (gameBoard.isEmptyCell(toCoord.row(), toCoord.col()))
            movesForThisPiece.add(new Move(fromCoord.row(), fromCoord.col(), toCoord.row(), toCoord.col()));
    }


}

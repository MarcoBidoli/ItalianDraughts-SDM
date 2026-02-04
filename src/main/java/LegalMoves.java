import java.util.ArrayList;
import java.util.List;

record Coords(int i, int j) {
}

public class LegalMoves {
    private Board gameBoard;
    private GameColor player;
    private int direction;

    public LegalMoves(Board gameBoard, GameColor player) {
        this.gameBoard = gameBoard;
        this.player = player;
        this.direction = this.player.equals(GameColor.BLACK) ? 1 : -1;
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

    protected List<List<Move>> eating() throws InvalidMoveException {
        List<List<Move>> allEatings = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Cell c = gameBoard.getCell(i, j);
                if (!c.isEmpty() && c.getPiece().getColor().equals(player)) {
                    findEatings(i, j, new ArrayList<>(), allEatings);
                }
            }
        }

        if (!allEatings.isEmpty())
            sortEatings(allEatings);

        return allEatings.stream()
                .filter(eating -> checkBest(eating, allEatings.get(0)))
                .toList();
    }

    private boolean checkBest(List<Move> e1, List<Move> e2) {
        if(e1.size() != e2.size())
            return false;

        boolean isE1K = gameBoard.getCell(e1.get(0).fromRow, e1.get(0).fromCol).getPiece().isKing();
        boolean isE2K = gameBoard.getCell(e2.get(0).fromRow, e2.get(0).fromCol).getPiece().isKing();
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
            boolean isE1K = gameBoard.getCell(e1.get(0).fromRow, e1.get(0).fromCol).getPiece().isKing();
            boolean isE2K = gameBoard.getCell(e2.get(0).fromRow, e2.get(0).fromCol).getPiece().isKing();
            l = Boolean.compare(isE2K, isE1K);
            if (l != 0)
                return l;

            //#kings eaten
            int k1 = countKingsEaten(e1);
            int k2 = countKingsEaten(e2);
            int cmpK = Integer.compare(k1, k2);
            if (cmpK != 0)
                return cmpK;

            return 0;
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
        if (gameBoard.getCell(x, y).getPiece().isKing())
            dirs = new int[][]{{1, -1}, {1, 1}, {-1, -1}, {-1, 1}};
        else
            dirs = new int[][]{{direction, -1}, {direction, 1}};

        for (int i[] : dirs) { //do it for every direction
            //eating "positions" - opponent and "over opponent"
            int xOpp = x + i[0];
            int yOpp = y + i[1];
            int finX = x + 2 * i[0];
            int finY = y + 2 * i[1];

            if (canEat(x, y, xOpp, yOpp, finX, finY)) { //check if an eating is doable
                hasEat = true;
                eatings.add(new EatingMove(x, y, finX, finY, gameBoard.getCell(xOpp, yOpp).getPiece().isKing()));

                //simulation of the new status of the board (with the opponent's piece eaten and the player's piece moved)
                Piece eaten = gameBoard.getCell(xOpp, yOpp).getPiece();
                gameBoard.getCell(xOpp, yOpp).empty();
                Piece moved = gameBoard.getCell(x, y).getPiece();
                gameBoard.getCell(x, y).empty();
                gameBoard.getCell(finX, finY).putPieceOn(moved);

                //recursive call
                findEatings(finX, finY, eatings, allEatings);

                //restore the board
                gameBoard.getCell(finX, finY).empty();
                gameBoard.getCell(x, y).putPieceOn(moved);
                eatings.removeLast();
                gameBoard.getCell(xOpp, yOpp).putPieceOn(eaten);
            }
        }

        //add the possible eating to the total possible eats list
        if (!hasEat && !eatings.isEmpty())
            allEatings.add(new ArrayList<>(eatings));
    }

    private boolean canEat(int x, int y, int xOpp, int yOpp, int finX, int finY) {
        //compute whether the eating is doable or not
        if (!gameBoard.isOnBoard(finX, finY))
            return false;

        if (gameBoard.getCell(xOpp, yOpp).isEmpty() || gameBoard.getCell(xOpp, yOpp).getPiece().getColor().equals(player))
            return false;

        if (!gameBoard.getCell(finX, finY).isEmpty())
            return false;

        if (gameBoard.getCell(x, y).getPiece() != null && !gameBoard.getCell(x, y).getPiece().isKing() && gameBoard.getCell(xOpp, yOpp).getPiece().isKing())
            return false;

        return true;
    }

    protected List<List<Move>> moving() {
        List<List<Move>> listOfAllMoves = new ArrayList<>();

        // Putting all player pieces coordinates in a list
        List<Coords> myPiecesCoords = new ArrayList<>();
        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++)
                if (gameBoard.getCell(i, j).getPiece() != null)
                    if (gameBoard.getCell(i, j).getPiece().getColor().equals(player))
                        myPiecesCoords.add(new Coords(i, j));

        // For each save coordinate, check what legal moves are associated with that piece
        // If player == BLACK front moves are increasing i
        for (Coords coord : myPiecesCoords) {
            List<Move> movesForThisPiece = new ArrayList<>();

            // for all pieces, checking the frontwards moves

            // (coord) -> (i+direction, j+1)
            addCoordToLegalMoves(coord, new Coords(coord.i() + direction, coord.j() + 1), movesForThisPiece);
            // (coord) -> (i+direction, j-1)
            addCoordToLegalMoves(coord, new Coords(coord.i() + direction, coord.j() - 1), movesForThisPiece);

            // if king, checking the backwards moves
            if (gameBoard.getCell(coord.i(), coord.j()).getPiece().isKing()) {
                // (coord) -> (i-direction, j+1)
                addCoordToLegalMoves(coord, new Coords(coord.i() - direction, coord.j() + 1), movesForThisPiece);
                // (coord) -> (i-direction, j-1)
                addCoordToLegalMoves(coord, new Coords(coord.i() - direction, coord.j() - 1), movesForThisPiece);
            }

            if (!movesForThisPiece.isEmpty())
                movesForThisPiece.forEach(m -> listOfAllMoves.add(new ArrayList<>(List.of(m))));
        }
        return listOfAllMoves;
    }

    private void addCoordToLegalMoves(Coords fromCoord, Coords toCoord, List<Move> movesForThisPiece) {
        // Avoid out of board moves
        if (!gameBoard.isOnBoard(toCoord.i(), toCoord.j())) return;

        // check destination is empty
        if (gameBoard.getCell(toCoord.i(), toCoord.j()).isEmpty())
            movesForThisPiece.add(new Move(fromCoord.i(), fromCoord.j(), toCoord.i(), toCoord.j()));
    }


}

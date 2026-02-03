import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
record TileEnc(int pieceEnc, int positionEnc) {}
public class Game {
    private final Board gameBoard;
    private GameColor currentPlayer;
    private GameStatus status;
    private int quietMovesWhite;  //turno in cui non avviene nessuna cattura da parte del bianco
    private int quietMovesBlack;  //turno in cui non avviene nessuna cattura da parte del nero
    private final Map<List<TileEnc>, Integer> visits;

    public Game() {
        this.gameBoard = new Board();      // crea una nuova scacchiera vuota
        this.gameBoard.setGame();          // imposta la configurazione iniziale
        this.currentPlayer = GameColor.WHITE;  // scelta: parte il bianco
        this.status = GameStatus.ONGOING;
        this.visits = new HashMap<>();
    }

    public GameColor getCurrentPlayer() {
        return currentPlayer;
    }

    public Board getBoard() {
        return gameBoard;
    }

    public void nextTurn() {
        currentPlayer = opposite(currentPlayer);
    }

    // opposite() = dato un colore, ritorna l'altro
    public static GameColor opposite(GameColor player) {
        if (player == null) {
            throw new IllegalArgumentException("player cannot be null");
        }
        return (player == GameColor.BLACK) ? GameColor.WHITE : GameColor.BLACK;
    }

    public void applyTurn(List<Move> moves) throws InvalidMoveException {
        // Salviamo chi sta giocando ORA (prima di eventuali cambi turno)
        GameColor playerWhoMoved = currentPlayer;

        // 1) Il turno contiene almeno una cattura?
        boolean captureOccurred = moves.stream()
                .anyMatch(m -> Math.abs(m.fromRow - m.toRow) == 2);

        // 2) Applica le mosse alla board (riutilizzo il codice di Federico)
        movePieces(moves, this.gameBoard);

        // Se qualcuno ha vinto per eliminazione pezzi, non ha senso calcolare draw o cambiare turno
        if (status != GameStatus.ONGOING) {
            return;
        }

        // 3) Update contatori draw (Move-Count Rule)
        updateDrawCounters(playerWhoMoved, captureOccurred);

        // Se la regola draw ha dichiarato DRAW, non cambia turno
        if (status != GameStatus.ONGOING) {
            return;
        }

        // 4) Turno successivo
        nextTurn();
    }


    private boolean hasKing(GameColor color) {
        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece piece = gameBoard.getCell(r, c).getPiece();
                if (piece != null &&
                        piece.getColor() == color &&
                        piece.isKing()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void updateDrawCounters(GameColor playerWhoMoved, boolean captureOccurred) {
        // Se c'è stata una cattura, la sequenza "senza catture" si interrompe: reset totale
        if (captureOccurred) {
            quietMovesWhite = 0;
            quietMovesBlack = 0;
            return;
        }

        // La Move-Count Rule si applica solo se entrambi hanno almeno un king
        boolean bothHaveKings = hasKing(GameColor.WHITE) && hasKing(GameColor.BLACK);
        if (!bothHaveKings) {
            return;
        }

        // Incremento il contatore del giocatore che ha appena mosso
        if (playerWhoMoved == GameColor.WHITE) {
            quietMovesWhite++;
        } else if (playerWhoMoved == GameColor.BLACK) {
            quietMovesBlack++;
        }

        // Trigger draw: 40 mosse consecutive per ciascun giocatore senza catture
        if (quietMovesWhite >= 40 && quietMovesBlack >= 40) {
            status = GameStatus.DRAW;
        }
    }

    public void movePieces(List<Move> move, Board board) throws InvalidMoveException {
        while (!move.isEmpty()) {
            Move currentMove = move.removeFirst();
            Piece pieceToMove = board.getCell(currentMove.fromRow, currentMove.fromCol).getPiece();
            promotionCheck(currentMove, pieceToMove);
            board.getCell(currentMove.toRow, currentMove.toCol).putPieceOn(pieceToMove);
            board.getCell(currentMove.fromRow, currentMove.fromCol).empty();
            if (Math.abs(currentMove.fromRow - currentMove.toRow) == 2) {
                board.getCell((currentMove.fromRow + currentMove.toRow) / 2, (currentMove.toCol + currentMove.fromCol) / 2).empty();
            visits.clear();
            }
        }
        boardEncoder(board);
        updateStatusByPieces(board);
    }

    public void boardEncoder(Board board) {
        List<TileEnc> encoding = new ArrayList<>();
        int counter = 0;
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < 8; j++) {
                if((i + j) % 2 == 0){
                    int value;
                    counter++;
                    if (board.getCell(i,j).getPiece() != null) {
                        if (board.getCell(i, j).getPiece().getColor() == GameColor.BLACK) {
                            value = board.getCell(i, j).getPiece().isKing() ? 4 : 2;
                        } else {
                            value = board.getCell(i, j).getPiece().isKing() ? 3 : 1;
                        }
                        encoding.add(new TileEnc(value, counter));
                    }
                }
            }
        }
        visits.merge(encoding, 1, Integer::sum);
    }

    public boolean checkRepetition() {
        for (int count : visits.values()) {
            if (count >= 3) {
                return true;
            }
        }
        return false;
    }

    protected Map<List<TileEnc>, Integer> getVisits() {
        return visits;
    }

    private static void promotionCheck(Move currentMove, Piece pieceToMove) {
        if((currentMove.toRow == 0 || currentMove.toRow == 7) && !pieceToMove.isKing()){
            pieceToMove.setKing(true);
        }
    }

    public GameStatus getStatus() {
        return status;
    }

    private void updateStatusByPieces(Board board) {
        int white = 0;
        int black = 0;

        for (int r = 0; r < 8; r++) {
            for (int c = 0; c < 8; c++) {
                Piece p = board.getCell(r, c).getPiece();
                if (p != null) {
                    if (p.getColor() == GameColor.WHITE) white++;
                    else if (p.getColor() == GameColor.BLACK) black++;
                }
            }
        }

        if (white == 0) status = GameStatus.BLACK_WINS;
        else if (black == 0) status = GameStatus.WHITE_WINS;
        else status = GameStatus.ONGOING;
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class BoardPanel extends JComponent {
    private final int TILE_SIZE = 80;
    private Game game;
    private List<List<Move>> filteredMoves = new ArrayList<>();
    private Coords selectedCoords = null;
    private DashboardPanel dashboardPanel;
    private final int OFFSET = 30;

    private final Color WHITE;
    private final Color BLACK;
    private final Color MARGIN_COLOR;

    public BoardPanel(Game game, DashboardPanel dashboardPanel) {
        this.game = game;
        this.dashboardPanel = dashboardPanel;
        this.dashboardPanel.updateInfo(game);
        this.BLACK = new Color(0, 0, 0);
        this.WHITE = new Color(255, 255, 255);
        this.MARGIN_COLOR = new Color(115, 74, 33);
        this.setPreferredSize(new Dimension(80 * 8 + OFFSET, 80 * 8 + OFFSET));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = (e.getX() - OFFSET) / TILE_SIZE;
                int row = e.getY() / TILE_SIZE;
                if (col >= 0 && col < 8 && row >= 0 && row < 8) {
                    handleLogic(row, col);
                }
            }
        });
    }

    protected void handleLogic(int row, int col) {
        //click on a suggersted move
        for (List<Move> move : filteredMoves) {
            Move lastMove = move.getLast();
            if (lastMove.toRow == row && lastMove.toCol == col) {
                try {
                    game.applyTurn((new ArrayList<>(move)));
                    if (dashboardPanel != null)
                        dashboardPanel.updateInfo(game);
                    selectedCoords = null;
                    filteredMoves = new ArrayList<>();
                    repaint();

                    checkGameOver();

                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        //select a piece to suggest moves
        List<List<Move>> allMoves = game.getCurrentLegalMoves();
        List<List<Move>> pieceMoves = allMoves.stream()
                .filter(m -> m.get(0).fromRow == row && m.get(0).fromCol == col)
                .toList();

        if (!pieceMoves.isEmpty()) {
            selectedCoords = new Coords(row, col);
            filteredMoves = pieceMoves;
        } else {
            selectedCoords = null;
            filteredMoves = new ArrayList<>();
        }
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2.setColor(MARGIN_COLOR);
        g2.fillRect(0, 0, OFFSET, 8 * TILE_SIZE);
        g2.fillRect(0, 8 * TILE_SIZE, 8 * TILE_SIZE + OFFSET, OFFSET);
        for (int i = 0; i < 8; i++) {
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("SansSerif", Font.BOLD, 14));
            g2.drawString(String.valueOf(8 - i), 10, i * TILE_SIZE + TILE_SIZE / 2 + 5);
            g2.drawString(String.valueOf((char) ('A' + i)), i * TILE_SIZE + OFFSET + TILE_SIZE / 2 - 5, 8 * TILE_SIZE + OFFSET - 10);

            for (int j = 0; j < 8; j++) {
                int x = j * TILE_SIZE + OFFSET;
                int y = i * TILE_SIZE;
                g.setColor((i + j) % 2 == 0 ? new Color(153, 102, 51) : new Color(223, 191, 159));
                g.fillRect(x, y, TILE_SIZE, TILE_SIZE);

                //selected piece
                if (selectedCoords != null && selectedCoords.i() == i && selectedCoords.j() == j) {
                    g.setColor(new Color(255, 255, 0, 150));
                    g.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                }

                //piece drawn
                Cell cell = game.getBoard().getCell(i, j);
                if (!cell.isEmpty()) {
                    drawPiece(g2, i, j, cell.getPiece());
                }
            }
        }

       //draw possible destinations
        g.setColor(new Color(46, 204, 113, 180));
        for (List<Move> move : filteredMoves) {
            Move lastMove = move.getLast();
            g.fillOval(lastMove.toCol * TILE_SIZE + OFFSET + 30, lastMove.toRow * TILE_SIZE + 30, 20, 20);
        }
    }

    private void drawPiece(Graphics2D g, int row, int col, Piece p) {
        Color pieceColor = p.getColor() == GameColor.WHITE ? this.WHITE : this.BLACK;
        int x = col * TILE_SIZE + OFFSET + 10;
        int y = row * TILE_SIZE + 10;
        int size = TILE_SIZE - 20;

        //piece shadow
        g.setColor(new Color(0, 0, 0, 50));
        g.fillOval(x + 2, y + 4, size, size);
        //piece
        g.setColor(pieceColor);
        g.fillOval(x, y, size, size);

        if (p.getColor() == GameColor.WHITE) {
            g.setColor(Color.LIGHT_GRAY);
            g.drawOval(x, y, size, size);
        }
        if (p.getColor() == GameColor.BLACK) {
            g.setColor(Color.DARK_GRAY);
            g.drawOval(x, y, size, size);
        }

        Stroke oldStroke = g.getStroke();
        //inner gold circle if king
        if (p.isKing()) {
            g.setColor(new Color(255, 215, 0));
            g.setStroke(new BasicStroke(3));
            g.drawOval(x + 15, y + 15, size - 30, size - 30);
        }
        g.setStroke(oldStroke);
    }

    public Coords getSelectedCoords() {
        return selectedCoords;
    }

    public void setSelectedCoords(Coords selectedCoords) {
        this.selectedCoords = selectedCoords;
    }

    public List<List<Move>> getFilteredMoves() {
        return filteredMoves;
    }

    private void checkGameOver() {
        GameStatus status = game.getStatus();

        if (status != GameStatus.ONGOING) {
            String msg = "";

            switch (status) {
                case GameStatus.WHITE_WINS -> msg = "WHITE WINS!";
                case GameStatus.BLACK_WINS -> msg = "BLACK WINS!";
                case GameStatus.DRAW -> msg = "DRAW!";
            }

            JOptionPane.showMessageDialog(this, msg, "End of the game", JOptionPane.INFORMATION_MESSAGE);

            System.exit(0);
        }
    }

    public void setDashboardPanel(DashboardPanel dBP) {
        this.dashboardPanel = dBP;
        this.dashboardPanel.updateInfo(game);
    }
}

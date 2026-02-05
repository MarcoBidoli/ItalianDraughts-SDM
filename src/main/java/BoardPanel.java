import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class BoardPanel extends JComponent {
    private final int TILE_SIZE = 80;
    private final int OFFSET = 30;
    private final Game game;
    private List<List<Move>> filteredMoves = new ArrayList<>();
    private Coords selectedCoords = null;
    private DashboardPanel dashboardPanel;

    // --- Wood Style Palette ---
    private final Color WOOD_LIGHT = new Color(223, 191, 159);
    private final Color WOOD_DARK = new Color(153, 102, 51);
    private final Color WOOD_MARGIN = new Color(115, 74, 33);
    private final Color PIECE_WHITE = new Color(245, 245, 245);
    private final Color PIECE_BLACK = new Color(35, 35, 35);
    private final Color HIGHLIGHT_MOVE = new Color(46, 204, 113, 180);
    private final Color SELECTED_TILE = new Color(255, 255, 0, 120);

    public BoardPanel(Game game, DashboardPanel dashboardPanel) {
        this.game = game;
        this.dashboardPanel = dashboardPanel;
        if (this.dashboardPanel != null) {
            this.dashboardPanel.updateInfo(game);
        }

        this.setPreferredSize(new Dimension(TILE_SIZE * 8 + OFFSET, TILE_SIZE * 8 + OFFSET));

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

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int col = (e.getX() - OFFSET) / TILE_SIZE;
                int row = e.getY() / TILE_SIZE;

                boolean overSelectable = game.getCurrentLegalMoves().stream()
                        .anyMatch(m -> m.getFirst().fromRow == row && m.getFirst().fromCol == col);

                setCursor(overSelectable ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
            }
        });
    }

    protected void handleLogic(int row, int col) {
        for (List<Move> move : filteredMoves) {
            Move lastMove = move.getLast();
            if (lastMove.toRow == row && lastMove.toCol == col) {
                try {
                    game.applyTurn(new ArrayList<>(move));
                    if (dashboardPanel != null) dashboardPanel.updateInfo(game);
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

        // Handle Piece Selection
        List<List<Move>> allMoves = game.getCurrentLegalMoves();
        List<List<Move>> pieceMoves = allMoves.stream()
                .filter(m -> m.getFirst().fromRow == row && m.getFirst().fromCol == col)
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

        // Enable high-quality rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Get all legal moves once per repaint cycle for performance
        List<List<Move>> allMoves = game.getCurrentLegalMoves();

        // Draw Wooden Margins/Frame
        g2.setColor(WOOD_MARGIN);
        g2.fillRect(0, 0, OFFSET, 8 * TILE_SIZE + OFFSET); // Left vertical bar
        g2.fillRect(0, 8 * TILE_SIZE, 8 * TILE_SIZE + OFFSET, OFFSET); // Bottom horizontal bar

        // Iterate through the grid
        for (int i = 0; i < 8; i++) {
            // Draw Coordinate Labels (8-1 and A-H)
            g2.setColor(new Color(240, 240, 240));
            g2.setFont(new Font("SansSerif", Font.BOLD, 16));
            g2.drawString(String.valueOf(8 - i), 10, i * TILE_SIZE + TILE_SIZE / 2 + 7);
            g2.drawString(String.valueOf((char) ('A' + i)), i * TILE_SIZE + OFFSET + TILE_SIZE / 2 - 5, 8 * TILE_SIZE + 22);

            for (int j = 0; j < 8; j++) {
                int x = j * TILE_SIZE + OFFSET;
                int y = i * TILE_SIZE;

                // A. Draw Checkerboard Tiles
                g2.setColor((i + j) % 2 == 0 ? WOOD_DARK : WOOD_LIGHT);
                g2.fillRect(x, y, TILE_SIZE, TILE_SIZE);

                // Create a "hit zone" visual to tell the player which pieces can move
                final int currentRow = i;
                final int currentCol = j;
                boolean isSelectable = allMoves.stream()
                        .anyMatch(m -> m.getFirst().fromRow == currentRow && m.getFirst().fromCol == currentCol);

                if (isSelectable) {
                    Stroke oldStroke = g2.getStroke();

                    g2.setColor(new Color(0, 0, 0, 60));
                    g2.setStroke(new BasicStroke(5.0f));
                    int padding = 8;
                    g2.drawOval(x + padding, y + padding, TILE_SIZE - (padding * 2), TILE_SIZE - (padding * 2));

                    // Draw the main Gold ring
                    g2.setColor(new Color(255, 215, 0)); // (Classic Gold)
                    g2.setStroke(new BasicStroke(3.5f));
                    g2.drawOval(x + padding, y + padding, TILE_SIZE - (padding * 2), TILE_SIZE - (padding * 2));

                    // Add a "Shine" (A small white arc on the top-left of the ring)
                    g2.setColor(new Color(255, 255, 255, 180));
                    g2.setStroke(new BasicStroke(1.5f));
                    g2.drawArc(x + padding, y + padding, TILE_SIZE - (padding * 2), TILE_SIZE - (padding * 2), 110, 60);

                    g2.setStroke(oldStroke);
                }

                // Highlight the currently selected square (Yellow overlay)
                if (selectedCoords != null && selectedCoords.i() == i && selectedCoords.j() == j) {
                    g2.setColor(SELECTED_TILE);
                    g2.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                }

                // Draw the Piece (Must be drawn after highlights to appear on top)
                Cell cell = game.getBoard().getCell(i, j);
                if (!cell.isEmpty()) {
                    drawPiece(g2, i, j, cell.getPiece());
                }
            }
        }

        // Draw suggested move indicators (Green Dots)
        g2.setColor(HIGHLIGHT_MOVE);
        for (List<Move> move : filteredMoves) {
            Move lastMove = move.getLast();
            int cx = lastMove.toCol * TILE_SIZE + OFFSET + TILE_SIZE / 2;
            int cy = lastMove.toRow * TILE_SIZE + TILE_SIZE / 2;
            g2.fillOval(cx - 10, cy - 10, 20, 20);
        }
    }

    private void drawPiece(Graphics2D g2, int row, int col, Piece p) {
        int x = col * TILE_SIZE + OFFSET + 12;
        int y = row * TILE_SIZE + 12;
        int size = TILE_SIZE - 24;

        // 1. Soft Shadow
        g2.setColor(new Color(0, 0, 0, 70));
        g2.fillOval(x + 2, y + 4, size, size);

        // 2. Modern Gradient Body
        boolean isWhite = (p.getColor() == GameColor.WHITE);
        Color base = isWhite ? PIECE_WHITE : PIECE_BLACK;
        Color top = isWhite ? Color.WHITE : new Color(70, 70, 70);

        RadialGradientPaint rgp = new RadialGradientPaint(
                new Point2D.Float(x + size * 0.35f, y + size * 0.35f),
                size,
                new float[]{0f, 1f},
                new Color[]{top, base}
        );
        g2.setPaint(rgp);
        g2.fillOval(x, y, size, size);

        // 3. Physical Edge Highlight
        g2.setColor(isWhite ? new Color(200, 200, 200) : new Color(10, 10, 10));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x, y, size, size);

        // 4. King Icon or Piece Pattern
        if (p.isKing()) {
            drawCrown(g2, x + size / 2, y + size / 2, size / 2);
        } else {
            // Subtle inner ring for regular checkers
            g2.setColor(new Color(128, 128, 128, 80));
            g2.setStroke(new BasicStroke(1));
            g2.drawOval(x + size / 4, y + size / 4, size / 2, size / 2);
        }
    }

    private void drawCrown(Graphics2D g2, int cx, int cy, int size) {
        g2.setColor(new Color(255, 215, 0)); // Gold
        int w = size / 2;
        int h = size / 3;

        // Simple modern 3-point crown
        int[] px = {cx - w, cx - w, cx - w/2, cx, cx + w/2, cx + w, cx + w};
        int[] py = {cy + h, cy - h/2, cy, cy - h, cy, cy - h/2, cy + h};

        g2.fillPolygon(px, py, 7);

        // Shine on the crown
        g2.setColor(new Color(255, 255, 255, 120));
        g2.setStroke(new BasicStroke(1));
        g2.drawPolygon(px, py, 7);
    }

    private void checkGameOver() {
        GameStatus status = game.getStatus();
        if (status != GameStatus.ONGOING) {
            String msg = switch (status) {
                case WHITE_WINS -> "WHITE WINS!";
                case BLACK_WINS -> "BLACK WINS!";
                case DRAW -> "DRAW!";
                default -> "";
            };
            JOptionPane.showMessageDialog(this, msg, "Game Over", JOptionPane.INFORMATION_MESSAGE);
            System.exit(0);
        }
    }

    public void setDashboardPanel(DashboardPanel dp) {
        this.dashboardPanel = dp;
        if (this.dashboardPanel != null) this.dashboardPanel.updateInfo(game);
    }

    public Coords getSelectedCoords() { return selectedCoords; }
}
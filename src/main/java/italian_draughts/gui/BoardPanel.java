package italian_draughts.gui;

import italian_draughts.domain.*;
import italian_draughts.logic.Game;
import italian_draughts.logic.GameObserver;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.List;
import java.util.stream.Stream;

public class BoardPanel extends JComponent implements GameObserver {
    private final int TILE_SIZE = 80;
    private final int OFFSET = 30;
    private final BoardController controller;
    private final PaletteColors colors;
    private final Game game;

    public BoardPanel(BoardController controller, Game game) {
        this.controller = controller;
        this.colors = new PaletteColors();
        this.game = game;
        this.setPreferredSize(new Dimension(TILE_SIZE * 8 + OFFSET, TILE_SIZE * 8 + OFFSET));

        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int col = (e.getX() - OFFSET) / TILE_SIZE;
                int row = e.getY() / TILE_SIZE;
                if (Board.onBoard(row, col)) {
                    controller.actionPerformed(row, col);
                }
            }
        });

        this.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                int col = (e.getX() - OFFSET) / TILE_SIZE;
                int row = e.getY() / TILE_SIZE;

                List<List<Move>> currentLegalMoves = controller.getGameCurrentLegalMoves();
                Stream<List<Move>> legalMovesStream = currentLegalMoves.stream();
                boolean isSelectable = legalMovesStream
                        .anyMatch(m -> m.getFirst().fromRow == row && m.getFirst().fromCol == col);

                setCursor(isSelectable ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
            }
        });
    }

    @Override
    public void paintComponent(Graphics g) {
        final int HIGHLIGHT_OVAL_SIZE = 20;
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Enable high-quality rendering
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Get all legal moves once per repaint cycle for performance
        List<List<Move>> allMoves = game.getCurrentLegalMoves();
        Board board = game.getBoard();
        Square selectedSquare = game.getSelectedSquare();
        List<List<Move>> selectedPieceMoves = game.getSelectedPieceMoves();


        // Draw Wooden Margins/Frame
        g2.setColor(colors.WOOD_MARGIN);
        g2.fillRect(0, 0, OFFSET, 8 * TILE_SIZE + OFFSET); // Left vertical bar
        g2.fillRect(0, 8 * TILE_SIZE, 8 * TILE_SIZE + OFFSET, OFFSET); // Bottom horizontal bar

        // Iterate through the grid
        for (int row = 0; row < 8; row++) {
            // Draw Coordinate Labels (8-1 and A-H)
            drawCoordinates(g2, row);
            for (int col = 0; col < 8; col++) {
                int x = col * TILE_SIZE + OFFSET;
                int y = row * TILE_SIZE;

                // A. Draw Checkerboard Tiles
                g2.setColor((row + col) % 2 == 0 ? colors.WOOD_DARK : colors.WOOD_LIGHT);
                g2.fillRect(x, y, TILE_SIZE, TILE_SIZE);

                // Create a "hit zone" visual to tell the player which pieces can move
                highlightPlayablePieces(row, col, allMoves, g2, x, y);

                // Highlight the currently selected square (Yellow overlay)
                if (selectedSquare != null && selectedSquare.row() == row && selectedSquare.col() == col) {
                    g2.setColor(colors.SELECTED_TILE);
                    g2.fillRect(x, y, TILE_SIZE, TILE_SIZE);
                }

                // Draw the Piece (Must be drawn after highlights to appear on top)
                Cell cell = board.getCell(row, col);
                if (!cell.isEmpty()) {
                    drawPiece(g2, row, col, cell.getPiece());
                }
            }
        }

        drawMoveLandings(g2, selectedPieceMoves);
    }

    private void drawMoveLandings(Graphics2D g2, List<List<Move>> moves) {
        final int DOT_SIZE = 20;
        // Draw suggested move indicators (Green Dots)
        g2.setColor(colors.HIGHLIGHT_MOVE);
        for (List<Move> seq : moves) {
            Move lastMove = seq.getLast();
            int cx = lastMove.toCol * TILE_SIZE + OFFSET + TILE_SIZE / 2;
            int cy = lastMove.toRow * TILE_SIZE + TILE_SIZE / 2;
            g2.fillOval(cx - DOT_SIZE / 2, cy - DOT_SIZE / 2, DOT_SIZE, DOT_SIZE);
        }
    }

    private void drawPiece(Graphics2D g2, int row, int col, Piece p) {
        final int OVAL_SIZE = 24;
        int x = col * TILE_SIZE + OFFSET + OVAL_SIZE/2;
        int y = row * TILE_SIZE + OVAL_SIZE/2;
        int size = TILE_SIZE - OVAL_SIZE;

        // 1. Soft Shadow
        //noinspection MagicNumber
        g2.setColor(new Color(0, 0, 0, 70));
        g2.fillOval(x + 2, y + 4, size, size);

        // 2. Modern Gradient Body
        boolean isWhite = (p.getColor() == GameColor.WHITE);
        Color base = isWhite ? colors.PIECE_WHITE : colors.PIECE_BLACK;
        //noinspection MagicNumber
        Color top = isWhite ? Color.WHITE : new Color(70, 70, 70);

        //noinspection MagicNumber
        RadialGradientPaint rgp = new RadialGradientPaint(
                new Point2D.Float(x + size * 0.35f, y + size * 0.35f),
                size,
                new float[]{0f, 1f},
                new Color[]{top, base}
        );
        g2.setPaint(rgp);
        g2.fillOval(x, y, size, size);

        // 3. Physical Edge Highlight
        //noinspection MagicNumber
        g2.setColor(isWhite ? new Color(200, 200, 200) : new Color(10, 10, 10));
        g2.setStroke(new BasicStroke(2));
        g2.drawOval(x, y, size, size);

        // 4. King Icon or domain.Piece Pattern
        if (p.isKing()) {
            drawCrown(g2, x + size / 2, y + size / 2, size / 2);
        } else {
            // Subtle inner ring for regular checkers
            //noinspection MagicNumber
            g2.setColor(new Color(128, 128, 128, 80));
            g2.setStroke(new BasicStroke(1));
            g2.drawOval(x + size / 4, y + size / 4, size / 2, size / 2);
        }
    }

    private void drawCrown(Graphics2D g2, int cx, int cy, int size) {
        g2.setColor(colors.GOLD); // Gold
        int w = size / 2;
        int h = size / 3;

        // Simple modern 3-point crown
        int[] px = {cx - w, cx - w, cx - w/2, cx, cx + w/2, cx + w, cx + w};
        int[] py = {cy + h, cy - h/2, cy, cy - h, cy, cy - h/2, cy + h};

        g2.fillPolygon(px, py, 7);

        // Shine on the crown
        //noinspection MagicNumber
        g2.setColor(new Color(255, 255, 255, 120));
        g2.setStroke(new BasicStroke(1));
        g2.drawPolygon(px, py, 7);
    }

    private void highlightPlayablePieces(int i, int j, List<List<Move>> allMoves, Graphics2D g2, int x, int y) {
        final int currentRow = i;
        final int currentCol = j;

        Stream<List<Move>> allMovesStream = allMoves.stream();
        boolean isSelectable = allMovesStream.anyMatch(m -> {
            Move firstMove = m.getFirst();
            return firstMove.fromRow == currentRow && firstMove.fromCol == currentCol;
        });

        if (isSelectable) {
            Stroke oldStroke = g2.getStroke();

            //noinspection MagicNumber
            g2.setColor(new Color(0, 0, 0, 60));
            //noinspection MagicNumber
            g2.setStroke(new BasicStroke(5.0f));
            int padding = 8;
            g2.drawOval(x + padding, y + padding, TILE_SIZE - (padding * 2), TILE_SIZE - (padding * 2));

            // Draw the main Gold ring
            g2.setColor(colors.GOLD); // (Classic Gold)
            //noinspection MagicNumber
            g2.setStroke(new BasicStroke(3.5f));
            g2.drawOval(x + padding, y + padding, TILE_SIZE - (padding * 2), TILE_SIZE - (padding * 2));

            // Add a "Shine" (A small white arc on the top-left of the ring)
            //noinspection MagicNumber
            g2.setColor(new Color(255, 255, 255, 180));
            //noinspection MagicNumber
            g2.setStroke(new BasicStroke(1.5f));
            //noinspection MagicNumber
            g2.drawArc(x + padding, y + padding, TILE_SIZE - (padding * 2), TILE_SIZE - (padding * 2), 110, 60);

            g2.setStroke(oldStroke);
        }
    }

    public void drawCoordinates(Graphics2D g2, int i) {
        //noinspection MagicNumber
        g2.setColor(new Color(240, 240, 240));
        //noinspection MagicNumber
        g2.setFont(new Font("SansSerif", Font.BOLD, 16));
        g2.drawString(String.valueOf(8 - i), 10, i * TILE_SIZE + TILE_SIZE / 2 + 7);
        //noinspection MagicNumber
        g2.drawString(String.valueOf((char) ('A' + i)), i * TILE_SIZE + OFFSET + TILE_SIZE / 2 - 5, 8 * TILE_SIZE + 22);
    }

    @Override
    public void modelChanged() {
        repaint();
    }
}
import javax.swing.*;
import java.awt.*;

public class BoardPanel extends JComponent {
    private Board gameBoard;
    private final int TILE_SIZE = 80;

    public BoardPanel(Board gameBoard) {
        this.gameBoard = gameBoard;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                g.setColor((i + j) % 2 == 0 ? java.awt.Color.LIGHT_GRAY : java.awt.Color.DARK_GRAY);
                g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                Cell cell = gameBoard.getCell(i, j);
                if (!cell.isEmpty()) {
                    drawPiece(g, i, j, cell.getPiece());
                }
            }
        }
    }

    private void drawPiece(Graphics g, int row, int col, Piece p) {
        g.setColor(p.getColor() == Color.WHITE ? java.awt.Color.WHITE :java.awt.Color.BLACK);
        g.fillOval(col * TILE_SIZE + 10, row * TILE_SIZE + 10, TILE_SIZE - 20, TILE_SIZE - 20);
    }
}

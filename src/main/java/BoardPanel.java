import javax.swing.*;
import java.awt.*;
import java.awt.Color;

public class BoardPanel extends JComponent implements GameListener{
    private Board gameBoard;
    private final int TILE_SIZE = 80;
    private final Color WHITE;
    private final Color BLACK;

    public BoardPanel(Board gameBoard) {
        this.gameBoard = gameBoard;
        this.BLACK = new Color(0,0,0);
        this.WHITE = new Color(255, 255, 255);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                g.setColor((i + j) % 2 == 0 ? new Color(153, 102, 51) : new Color(223, 191, 159));
                g.fillRect(j * TILE_SIZE, i * TILE_SIZE, TILE_SIZE, TILE_SIZE);

                Cell cell = gameBoard.getCell(i, j);
                if (!cell.isEmpty()) {
                    drawPiece(g, i, j, cell.getPiece());
                }
            }
        }
    }

    private void drawPiece(Graphics g, int row, int col, Piece p) {
        g.setColor((p.getColor().equals(GameColor.WHITE)) ? this.WHITE : this.BLACK);
        g.fillOval(col * TILE_SIZE + 10, row * TILE_SIZE + 10, TILE_SIZE - 20, TILE_SIZE - 20);
    }


    @Override
    public void onBoardChanged() {
        this.repaint();
    }
}

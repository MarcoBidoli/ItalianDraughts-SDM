package italian_draughts.gui;

import italian_draughts.domain.Board;
import italian_draughts.domain.GameColor;
import italian_draughts.domain.GameStatus;
import italian_draughts.logic.Game;
import italian_draughts.logic.GameObserver;

import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JComponent implements GameObserver {
    private final JLabel status;
    private final JLabel count;
    private final Game game;
    private final PaletteColors colors;
    private final BoardController controller;

    public DashboardPanel(Game game, BoardController controller) {
        this.game = game;
        this.colors = new PaletteColors();
        this.controller = controller;
        //noinspection MagicNumber
        this.setLayout(new BorderLayout(20, 0));
        //noinspection MagicNumber
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        this.setBackground(colors.DASHBOARD_BG);

        JPanel txtPan = new JPanel(new GridLayout(2, 1));
        txtPan.setOpaque(false);
        status = new JLabel("WHITE'S TURN");
        //noinspection MagicNumber
        status.setFont(new Font("SansSerif", Font.BOLD, 16));

        count = new JLabel("WHITES: 12 | BLACKS: 12");
        //noinspection MagicNumber
        count.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPan.add(status);
        txtPan.add(count);

        JPanel btnPan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnPan.setOpaque(false);
        JButton resign = new ModernButton("Resign", colors.RESIGN_BTT, Color.WHITE);
        JButton draw = new ModernButton("Draw", colors.DRAW_BTT, Color.WHITE);


        resign.addActionListener(_ -> handleResign());
        draw.addActionListener(_ -> handleDrawRequest());

        btnPan.add(draw);
        btnPan.add(resign);

        add(txtPan, BorderLayout.WEST);
        add(btnPan, BorderLayout.EAST);
    }

    private void handleDrawRequest() {
        GameColor req = game.getCurrentPlayer().getColor();
        GameColor opp = (req == GameColor.WHITE) ? GameColor.BLACK : GameColor.WHITE;

        int resp = JOptionPane.showConfirmDialog(this, req + " asks for DRAW. " + opp + " do you agree?", "Draw request", JOptionPane.YES_NO_OPTION);

        if(resp == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Game ends in an AGREED DRAW.");
            game.agreedDrawHandling();
            System.exit(0);
        }
    }

    private void handleResign() {
        GameColor loser = game.getCurrentPlayer().getColor();
        String winner = (loser == GameColor.WHITE) ? "BLACK" : "WHITE";

        int conf = JOptionPane.showConfirmDialog(this, loser + ", are you sure you want to call RESIGN?", "Resign", JOptionPane.YES_NO_OPTION);

        if(conf == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, loser + " RESIGNED, " + winner + " WINS!");
            controller.resign(loser);
        }
    }


    @Override
    public void modelChanged() {
        updateInfo();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        g2d.setColor(colors.DASHBOARD_BG);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        g2d.setColor(Color.LIGHT_GRAY);
        g2d.drawLine(0, getHeight() - 1, getWidth(), getHeight() - 1);

    }

    private void updateInfo() {
        String turn = this.game.getCurrentPlayer().getColor() == GameColor.WHITE ? "WHITE" : "BLACK";
        status.setText(turn + "'S TURN");
        status.setForeground(this.game.getCurrentPlayer().getColor() == GameColor.WHITE ? Color.GRAY : Color.BLACK);
        Board board = this.game.getBoard();
        int w = board.countColorPieces(GameColor.WHITE), b = board.countColorPieces(GameColor.BLACK);
        count.setText("PIECES COUNT: WHITE " + w + " | BLACK " + b);

        if(game.getStatus() == GameStatus.ONGOING) {
            String turn = this.game.getCurrentPlayer() == GameColor.WHITE ? "WHITE" : "BLACK";
            status.setText(turn + "'S TURN");
            status.setForeground(this.game.getCurrentPlayer() == GameColor.WHITE ? Color.GRAY : Color.BLACK);
        } else {
            status.setText("GAME OVER: " + getStringStatus(game.getStatus()));
            status.setForeground(Color.DARK_GRAY);
        }
    }

    public String getStatusText() {
        return status.getText();
    }

    public String getCountText() {
        return count.getText();
    }

    private String getStringStatus(GameStatus gs) {
        String stringStatus;
        switch (gs) {
            case DRAW -> stringStatus = "DRAW!";
            case BLACK_WINS -> stringStatus = "BLACK WINS!";
            case WHITE_WINS -> stringStatus = "WHITE WINS!";
            default -> stringStatus = "";
        }
        return stringStatus;
    }
}

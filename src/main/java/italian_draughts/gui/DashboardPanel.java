package italian_draughts.gui;

import italian_draughts.domain.Board;
import italian_draughts.domain.GameColor;
import italian_draughts.domain.GameStatus;
import italian_draughts.logic.Game;
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private final JLabel status;
    private final JLabel count;
    private final Game game;
    private final PaletteColors colors;

    public DashboardPanel(Game game) {
        this.game = game;
        this.colors = new PaletteColors();
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
        GameColor req = game.getCurrentPlayer();
        GameColor opp = (req == GameColor.WHITE) ? GameColor.BLACK : GameColor.WHITE;

        int resp = JOptionPane.showConfirmDialog(this, req + " asks for DRAW. " + opp + " do you agree?", "Draw request", JOptionPane.YES_NO_OPTION);

        if(resp == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, "Game ends in an AGREED DRAW.");
            game.agreedDrawHandling();
            System.exit(0);
        }
    }

    private void handleResign() {
        GameColor loser = game.getCurrentPlayer();
        String winner = (loser == GameColor.WHITE) ? "BLACK" : "WHITE";

        int conf = JOptionPane.showConfirmDialog(this, loser + ", are you sure you want to call RESIGN?", "Resign", JOptionPane.YES_NO_OPTION);

        if(conf == JOptionPane.YES_OPTION) {
            JOptionPane.showMessageDialog(this, loser + " RESIGNED, " + winner + " WINS!");
            game.resignHandling(loser);
            System.exit(0);
        }
    }

    public void updateInfo() {
        String turn = this.game.getCurrentPlayer() == GameColor.WHITE ? "WHITE" : "BLACK";
        status.setText(turn + "'S TURN");
        status.setForeground(this.game.getCurrentPlayer() == GameColor.WHITE ? Color.GRAY : Color.BLACK);
        Board board = this.game.getBoard();
        int w = board.countColorPieces(GameColor.WHITE), b = board.countColorPieces(GameColor.BLACK);
        count.setText("PIECES COUNT: WHITE " + w + " | BLACK " + b);

        if(game.getStatus() != GameStatus.ONGOING) {
            status.setText("GAME STATUS: " + game.getStatus());
            status.setForeground(Color.DARK_GRAY);
        }
    }

    public String getStatusText() {
        return status.getText();
    }

    public String getCountText() {
        return count.getText();
    }
}

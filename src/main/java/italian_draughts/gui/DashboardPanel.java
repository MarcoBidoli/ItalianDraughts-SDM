package italian_draughts.gui;

import italian_draughts.domain.Board;
import italian_draughts.domain.GameColor;
import italian_draughts.domain.GameStatus;
import italian_draughts.domain.Piece;
import italian_draughts.logic.Game;
import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private JLabel status;
    private JLabel count;
    private JButton resign;
    private JButton draw;
    private final Game game;

    public DashboardPanel(Game game) {
        this.game = game;
        this.setLayout(new BorderLayout(20, 0));
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        this.setBackground(new Color(236, 240, 241));

        JPanel txtPan = new JPanel(new GridLayout(2, 1));
        txtPan.setOpaque(false);
        status = new JLabel("WHITE'S TURN");
        status.setFont(new Font("SansSerif", Font.BOLD, 16));

        count = new JLabel("WHITES: 12 | BLACKS: 12");
        count.setFont(new Font("SansSerif", Font.PLAIN, 14));
        txtPan.add(status);
        txtPan.add(count);

        JPanel btnPan = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        btnPan.setOpaque(false);
        resign = new ModernButton("Resign", new Color(231, 76, 60), Color.WHITE);
        draw = new ModernButton("Draw", new Color(52, 152, 219), Color.WHITE);


        resign.addActionListener(e -> handleResign());
        draw.addActionListener(e -> handleDrawRequest());

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

        int w = 0, b = 0;
        Board board = this.game.getBoard();
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                Piece p = board.getCell(i,j).getPiece();
                if(p != null) {
                    if(p.getColor() == GameColor.WHITE) w++;
                    else b++;
                }
            }
        }
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

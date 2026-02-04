import javax.swing.*;
import java.awt.*;

public class DashboardPanel extends JPanel {
    private JLabel status;
    private JLabel count;

    public DashboardPanel() {
        this.setLayout(new GridLayout(1, 2));
        this.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        this.setBackground(new Color(236, 240, 241));

        status = new JLabel("WHITE'S TURN");
        status.setFont(new Font("SansSerif", Font.BOLD, 16));

        count = new JLabel("WHITES: 12 | BLACKS: 12");
        count.setFont(new Font("SansSerif", Font.PLAIN, 14));
        count.setHorizontalAlignment(SwingConstants.RIGHT);

        add(status);
        add(count);
    }

    public void updateInfo(Game game) {
        String turn = game.getCurrentPlayer() == GameColor.WHITE ? "WHITE" : "BLACK";
        status.setText(turn + "'S TURN");
        status.setForeground(game.getCurrentPlayer() == GameColor.WHITE ? Color.BLUE : Color.RED);

        int w = 0, b = 0;
        Board board = game.getBoard();
        for(int i=0; i<8; i++) {
            for(int j=0; j<8; j++) {
                Piece p = board.getCell(i,j).getPiece();
                if(p != null) {
                    if(p.getColor() == GameColor.WHITE) w++;
                    else b++;
                }
            }
        }
        count.setText("WHITES: " + w + " | BLACKS: " + b);

        if(game.getStatus() != GameStatus.ONGOING) {
            status.setText("GAME STATUS: " + game.getStatus());
            status.setForeground(Color.DARK_GRAY);
        }
    }
}

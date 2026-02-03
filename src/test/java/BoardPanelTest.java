import org.junit.jupiter.api.Test;

import javax.swing.*;

public class BoardPanelTest {

    @Test
    public void showCorrectBoard() {
        Board b = new Board();
        b.setGame();

        JFrame frame = new JFrame("Dama Java");
        BoardPanel bP = new BoardPanel(b);

        frame.add(bP);
        frame.setSize(655, 680);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);


    }
}

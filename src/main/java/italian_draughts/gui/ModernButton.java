package italian_draughts.gui;

import javax.swing.*;
import java.awt.*;

/**
 * A modern button with rounded corners.
 */
public class ModernButton extends JButton {

    /**
     * Creates a new ModernButton.
     * @param text The text of the button.
     * @param bg The background color of the button.
     * @param fg The foreground color of the button.
     */
    public ModernButton(String text, Color bg, Color fg) {
        super(text);
        setBackground(bg);
        setForeground(fg);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setOpaque(false);
        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Paint the background
        g2.setColor(getBackground());
        // Adjust for more/less roundness
        //noinspection MagicNumber
        int cornerRadius = 15;
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Paint the text
        FontMetrics metrics = g2.getFontMetrics(getFont());
        int x = (getWidth() - metrics.stringWidth(getText())) / 2;
        int y = ((getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
        g2.setColor(getForeground());
        g2.setFont(getFont());
        g2.drawString(getText(), x, y);

        g2.dispose();
    }
}
package UserDSB;

import AdminDSB.*;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Insets;
import java.awt.geom.RoundRectangle2D;
import javax.swing.border.Border;

/**
 *
 * @author Daiplatinue
 */
public class RoundedBorderss implements Border {

    private final int radius;

    RoundedBorderss(int radius) {
        this.radius = radius;
    }

    @Override
    public Insets getBorderInsets(Component c) {
        return new Insets(this.radius + 1, this.radius + 1, this.radius + 2, this.radius);
    }

    @Override
    public boolean isBorderOpaque() {
        return false;
    }

    @Override
    public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int adjustedWidth = width - 1;
        int adjustedHeight = height - 1;

        RoundRectangle2D border = new RoundRectangle2D.Float(x, y, adjustedWidth, adjustedHeight, radius, radius);
        g2.draw(border);
    }
}
